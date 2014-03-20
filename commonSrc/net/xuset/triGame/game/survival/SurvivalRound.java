package net.xuset.triGame.game.survival;

import net.xuset.objectIO.netObject.ObjControllerI;
import net.xuset.triGame.game.GameRound;
import net.xuset.triGame.game.ManagerService;
import net.xuset.triGame.game.PlayerInfoContainer;
import net.xuset.triGame.game.GameMode.IsGameOver;
import net.xuset.triGame.game.entities.zombies.ZombieManager;
import net.xuset.triGame.game.entities.zombies.ZombieSpawner;
import net.xuset.triGame.game.ui.gameInput.IRoundInput;


class SurvivalRound extends GameRound {
	private final boolean isServer;
	private final IRoundInput roundInput;
	private final ZombieSpawner zombieSpawner = new ZombieSpawner();
	private final IsGameOver isGameOver;
	
	private ManagerService managers;

	public SurvivalRound(ObjControllerI objController, boolean isServer,
			IRoundInput roundInput, IsGameOver isGameOver,
			PlayerInfoContainer playerContainer) {
		
		super(objController, playerContainer);
		this.isServer = isServer;
		this.roundInput = roundInput;
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
		if (!isGameOver.value)
			roundInput.setNewRoundRequestable(true);
		if (!isGameOver.value && roundInput.newRoundRequested())
			setReadyForNextRound();
		
		if (isServer && areAllPlayersReadyForNewRound()) {
			resetAllPlayersRoundRequest();
			setRound(getRoundNumber() + 1);
		}
	}
	
	@Override
	protected void onRoundStart() {
		super.onRoundStart();
		roundInput.setNewRoundRequestable(false);
		if (getRoundNumber() % 10 == 0)
			zombieSpawner.startNewSpawnRound(1, 0, true);
		else
			zombieSpawner.startNewSpawnRound(getZombiesPerRound(),
					getZombieSpawnDelta(), false);
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
