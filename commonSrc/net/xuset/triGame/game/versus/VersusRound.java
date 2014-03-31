package net.xuset.triGame.game.versus;

import net.xuset.objectIO.netObject.NetObjUpdater;
import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.triGame.game.Draw;
import net.xuset.triGame.game.GameRound;
import net.xuset.triGame.game.ManagerService;
import net.xuset.triGame.game.PlayerInfoContainer;
import net.xuset.triGame.game.GameMode.IsGameOver;
import net.xuset.triGame.game.ui.gameInput.IRoundInput;


class VersusRound extends GameRound {
	private static final int roundDelay = 2000;

	private final IRoundInput roundInput;
	private final boolean isServer;
	private final VersusMap gameMap;
	private final IsGameOver isGameOver;
	
	private long nextRoundStartTime = 0;
	private ManagerService managers;
	
	final VersusSpawner spawner;

	VersusRound(NetObjUpdater objController, boolean isServer,
			IRoundInput roundInput, VersusMap gameMap, IsGameOver isGameOver,
			PlayerInfoContainer playerContainer) {
		
		super(objController, playerContainer);
		this.isServer = isServer;
		this.roundInput = roundInput;
		this.gameMap = gameMap;
		this.isGameOver = isGameOver;
		spawner = new VersusSpawner(objController, isServer);
		roundInput.setNewRoundRequestable(true);
	}
	
	void setDependencies(ManagerService managers) {
		this.managers = managers;
	}

	@Override
	protected void onRoundStart() {
		super.onRoundStart();
		roundInput.setNewRoundRequestable(false);
		spawner.startNewSpawnRound(getZombiesPerRound(), getZombieSpawnDelta(), false);
	}
	
	@Override
	public void draw(IGraphics g) {
		super.draw(g);
		if (getRoundNumber() == 0)
			Draw.drawPickASide(g);
	}

	@Override
	protected int getZombiesPerRound() {
		int number = roundNumber.get();
		int players = managers.person.list.size();
		return ((number * number) / 10 + number) * players;
	}

	@Override
	protected int getZombieSpawnDelta() { return 300; }

	@Override
	protected void handleRoundNotOnGoing() {
		if (shouldBeginGame())
			setReadyForNextRound();
		
		if (!isServer)
			return;
		
		if (nextRoundStartTime == 0)
			nextRoundStartTime = System.currentTimeMillis() + roundDelay;
		if (shouldStartNextRound())
			setRound(getRoundNumber() + 1);
		if (!hasGameStarted() && areAllPlayersReadyForNewRound()) {
			resetAllPlayersRoundRequest();
			gameMap.createMissingWalls(managers);
			setRound(1);
		}
	}
	
	private boolean shouldStartNextRound() {
		return hasGameStarted() && !isGameOver.value &&
				nextRoundStartTime < System.currentTimeMillis();
	}
	
	private boolean hasGameStarted() {
		return getRoundNumber() != 0;
	}
	
	private boolean shouldBeginGame() {
		return getRoundNumber() == 0 && roundInput.newRoundRequested();
	}

	@Override
	protected void handleRoundOnGoing() {
		if (!isServer)
			return;
		
		nextRoundStartTime = 0;
		spawner.update(managers.zombie);
		roundOnGoing.set(
				!spawner.finishedSpawn() ||
				managers.zombie.getZombiesAlive() > 0);
	}

}
