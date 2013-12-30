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
import triGame.game.GameMode.IsGameOver;

class SurvivalRound extends GameRound {
	private final boolean isServer;
	private final PeripheralInput.Keyboard keyboard;
	private final ZombieSpawner zombieSpawner = new ZombieSpawner();
	private final IsGameOver isGameOver;
	
	private ManagerService managers;

	public SurvivalRound(ObjControllerI objController, boolean isServer,
			PeripheralInput.Keyboard keyboard, IsGameOver isGameOver) {
		
		super(objController);
		this.isServer = isServer;
		this.keyboard = keyboard;
		this.isGameOver = isGameOver;
	}
	
	public void setDependencies(ManagerService managers) {
		this.managers = managers;
	}

	@Override
	protected void handleRoundOnGoing() {
		if (!isServer)
			return;
		
		final ZombieManager manager = managers.zombie;
		zombieSpawner.update(manager);
		
		roundOnGoing.set(!zombieSpawner.finishedSpawn() || manager.getZombiesAlive() > 0);
	}
	
	@Override
	protected void handleRoundNotOnGoing() {
		if (isServer && !isGameOver.value && keyboard.isPressed(KeyEvent.VK_ENTER)) {
			int next = getRoundNumber() + 1;
			setRound(next);
		}
	}
	
	@Override
	public void draw(Graphics2D g, ViewRect rect) {
		super.draw(g, rect);
		if (isServer && !isRoundOnGoing() && !isGameOver.value)
			Draw.drawEnterToStart(g, rect);
	}
	
	@Override
	protected void onRoundStart() {
		super.onRoundStart();
		if (getRoundNumber() % 10 == 0)
			zombieSpawner.startNewSpawnRound(1, 0, true);
		else
			zombieSpawner.startNewSpawnRound(getZombiesPerRound(), getZombieSpawnDelta(), false);
	}

	@Override
	public int getZombiesPerRound() {
		final int players = managers.person.list.size();
		final int number = roundNumber.get();
		
		return ((number * number) / 10 + number) * players;
	}
	
	@Override
	public int getZombieSpawnDelta() {
		final int players = managers.person.list.size();
		final int number = roundNumber.get();
		int d =  (int) ((700 - 100 * players) - number * 10.0);
		d = d < 100 ? 100 : d;
		return d;
	}

}
