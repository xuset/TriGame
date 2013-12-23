package triGame.game.versus;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import objectIO.netObject.ObjControllerI;
import tSquare.game.GameBoard.ViewRect;
import tSquare.system.PeripheralInput;
import triGame.game.Draw;
import triGame.game.GameMode.IsGameOver;
import triGame.game.GameRound;
import triGame.game.ManagerService;

class VersusRound extends GameRound {

	private final PeripheralInput.Keyboard keyboard;
	private final boolean isServer;
	private final VersusMap gameMap;
	private final IsGameOver isGameOver;
	
	private long nextRoundStartTime = 0;
	private boolean gameStarted = false;
	private ManagerService managers;
	
	final VersusSpawner spawner;

	VersusRound(ObjControllerI objController, boolean isServer,
			PeripheralInput.Keyboard keyboard, VersusMap gameMap, IsGameOver isGameOver) {
		
		super(objController);
		this.isServer = isServer;
		this.keyboard = keyboard;
		this.gameMap = gameMap;
		this.isGameOver = isGameOver;
		spawner = new VersusSpawner(objController);
	}
	
	void setDependencies(ManagerService managers) {
		this.managers = managers;
	}

	@Override
	public void setRound(int round) {
		super.setRound(round);
		spawner.startNewSpawnRound(getZombiesPerRound(), getZombieSpawnDelta(), false);
	}
	
	@Override
	public void draw(Graphics2D g, ViewRect rect) {
		super.draw(g, rect);
		if (getRoundNumber() == 0)
			Draw.drawPickASide(g, rect);
		if (isServer && !gameStarted)
			Draw.drawEnterToStartVersus(g, rect);
	}

	@Override
	protected int getZombiesPerRound() {
		int number = roundNumber.get();
		return ((number * number) / 10 + number);
	}

	@Override
	protected int getZombieSpawnDelta() { return 300; }

	@Override
	protected void handleRoundNotOnGoing() {
		if (!isServer)
			return;
		
		if (nextRoundStartTime == 0)
			nextRoundStartTime = System.currentTimeMillis() + 5000;
		if (gameStarted && !isGameOver.value && nextRoundStartTime < System.currentTimeMillis())
			setRound(getRoundNumber() + 1);
		if (!gameStarted && keyboard.isPressed(KeyEvent.VK_ENTER)) {
			gameStarted = true;
			gameMap.createMissingWalls(managers);
		}
	}

	@Override
	protected void handleRoundOnGoing() {
		if (!isServer)
			return;
		
		nextRoundStartTime = 0;
		spawner.update(managers.zombie);
		roundOnGoing.set(!spawner.finishedSpawn() || managers.zombie.getZombiesAlive() > 0);
	}

}
