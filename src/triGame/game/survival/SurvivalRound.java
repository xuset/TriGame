package triGame.game.survival;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import objectIO.netObject.ObjControllerI;
import tSquare.game.GameBoard.ViewRect;
import tSquare.system.PeripheralInput;
import triGame.game.Draw;
import triGame.game.GameRound;
import triGame.game.ManagerService;
import triGame.game.entities.zombies.ZombieManager;
import triGame.game.entities.zombies.ZombieSpawner;

abstract class SurvivalRound extends GameRound {
	private final boolean isServer;
	private final PeripheralInput.Keyboard keyboard;
	private final ZombieSpawner zombieSpawner = new ZombieSpawner();
	
	protected abstract ManagerService getManagers();

	public SurvivalRound(ObjControllerI objController, boolean isServer, PeripheralInput.Keyboard keyboard) {
		super(objController);
		this.isServer = isServer;
		this.keyboard = keyboard;
	}

	@Override
	protected void handleRoundOnGoing() {
		if (!isServer)
			return;
		
		final ZombieManager manager = getManagers().zombie;
		zombieSpawner.update(manager);
		
		roundOnGoing.set(!zombieSpawner.finishedSpawn() || manager.getZombiesAlive() > 0);
	}
	
	@Override
	protected void handleRoundNotOnGoing() {
		if (isServer && keyboard.isPressed(KeyEvent.VK_ENTER)) {
			int next = getRoundNumber() + 1;
			setRound(next);
		}
	}
	
	@Override
	public void draw(Graphics2D g, ViewRect rect) {
		super.draw(g, rect);
		if (isServer && !isRoundOnGoing())
			Draw.drawEnterToStart(g, rect);
	}
	
	@Override
	public void setRound(int round) {
		super.setRound(round);
		if (round % 10 == 0)
			zombieSpawner.startNewSpawnRound(1, 0, true);
		else
			zombieSpawner.startNewSpawnRound(getZombiesPerRound(), getZombieSpawnDelta(), false);
	}

	@Override
	public int getZombiesPerRound() {
		final int players = getManagers().person.list.size();
		final int number = roundNumber.get();
		
		return ((number * number) / 10 + number) * players;
	}
	
	@Override
	public int getZombieSpawnDelta() {
		final int players = getManagers().person.list.size();
		final int number = roundNumber.get();
		int d =  (int) ((700 - 100 * players) - number * 10.0);
		d = d < 100 ? 100 : d;
		return d;
	}

}
