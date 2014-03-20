package net.xuset.triGame.game.survival;

import net.xuset.objectIO.netObject.ObjControllerI;
import net.xuset.tSquare.imaging.IImageFactory;
import net.xuset.tSquare.util.Observer;
import net.xuset.triGame.game.GameGrid;
import net.xuset.triGame.game.GameMode;
import net.xuset.triGame.game.GameRound;
import net.xuset.triGame.game.ManagerService;
import net.xuset.triGame.game.PlayerInfoContainer;
import net.xuset.triGame.game.SafeBoard;
import net.xuset.triGame.game.entities.Person;
import net.xuset.triGame.game.entities.zombies.ZombieHandler;
import net.xuset.triGame.game.entities.zombies.ZombieTargeter;
import net.xuset.triGame.game.shopping.ShopManager;
import net.xuset.triGame.game.survival.safeArea.SurvivalSafeBoard;
import net.xuset.triGame.game.ui.gameInput.IRoundInput;



public class SurvivalGameMode extends GameMode {
	private ManagerService managers;
	private final ShopManager shop;
	private final GameGrid gameGrid;
	private final SurvivalSafeBoard safeBoard;
	private final ZombieHandler zombieHandler;
	private final ZombieTargeter zombieTargeter;
	private final SurvivalRound gameRound;
	private Person player;

	public SurvivalGameMode(ShopManager shop, boolean isServer, GameGrid gameGrid,
			ObjControllerI objController, IRoundInput roundInput,
			IImageFactory imageFactory, PlayerInfoContainer playerContainer) {
		
		this.shop = shop;
		this.gameGrid = gameGrid;
		safeBoard = new SurvivalSafeBoard(gameGrid.getGridWidth() / 2.0 + 0.5,
				gameGrid.getGridHeight() / 2.0 + 0.5, imageFactory);
		zombieTargeter = new ZombieTargeter();
		gameRound = new SurvivalRound(objController, isServer, roundInput, 
				isGameOver, playerContainer);
		zombieHandler = new ZombieHandler(gameRound.onNewRound);
		gameRound.onNewRound.watch(new OnNewRound());
	}

	@Override public SafeBoard getSafeBoard() { return safeBoard; }
	@Override public ZombieHandler getZombieHandler() { return zombieHandler; }
	@Override public ZombieTargeter getZombieTargeter() { return zombieTargeter; }
	@Override protected GameRound getGameRound() { return gameRound; }
	
	@Override protected void createMap(double wallGenCoefficient) {
		SurvivalMap.createRandomMap(managers, safeBoard, gameGrid, wallGenCoefficient);
	}
	
	@Override
	public void update(int frameDelta) {
		super.update(frameDelta);
		if (managers.building.getBuildingGetter().getHQ() == null)
			isGameOver.value = true;
	}

	@Override
	protected void setDependencies(ManagerService managers) {
		this.managers = managers;
		gameRound.setDependencies(managers);
		zombieTargeter.setDependencies(managers.zombie.list);
		zombieHandler.setDependencies(managers);
	}

	@Override
	protected Person spawnInPlayer() {
		player = managers.person.create(gameGrid.getGridWidth() / 2 - 1,
				gameGrid.getGridHeight() / 2 - 2);
		return player;
	}
	
	private class OnNewRound implements Observer.Change<Integer> {
		@Override
		public void observeChange(Integer roundNumber) {
			if (roundNumber > 1) {
				if (roundNumber % 10 == 0)
					shop.addPoints(150);
				if (player.isDead() && !isGameOver()) {
					player.giveFullHealth();
				} else {
					shop.addPoints(50);
				}
			}
		}
	}

}
