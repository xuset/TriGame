package triGame.game.survival;

import objectIO.netObject.ObjControllerI;
import tSquare.system.PeripheralInput;
import tSquare.system.PeripheralInput.Keyboard;
import tSquare.util.Observer;
import triGame.game.GameMode;
import triGame.game.GameRound;
import triGame.game.ManagerService;
import triGame.game.Params;
import triGame.game.SafeBoard;
import triGame.game.entities.Person;
import triGame.game.entities.zombies.ZombieHandler;
import triGame.game.entities.zombies.ZombieTargeter;
import triGame.game.shopping.ShopManager;
import triGame.game.survival.safeArea.SurvivalSafeBoard;


public class SurvivalGameMode extends GameMode {
	private ManagerService managers;
	private final SurvivalSafeBoard safeBoard = new SurvivalSafeBoard();
	private final ZombieMediator zombieHandler = new ZombieMediator();
	private final ZombieTargeter zombieTargeter = new ZombieTargeter();
	private final GameRound gameRound;
	private final ShopManager shop;

	public SurvivalGameMode(ShopManager shop, ObjControllerI objController, boolean isServer, PeripheralInput.Keyboard keyboard) {
		super(isServer);
		this.shop = shop;
		gameRound = new RoundMediator(objController, isServer, keyboard);
		gameRound.onNewRound.watch(new OnNewRound());
	}

	@Override public boolean isGameOver() { return (managers.building.getHQ() == null); }
	@Override public SafeBoard getSafeBoard() { return safeBoard; }
	@Override public ZombieHandler getZombieHandler() { return zombieHandler; }
	@Override public ZombieTargeter getZombieTargeter() { return zombieTargeter; }
	@Override protected GameRound getGameRound() { return gameRound; }
	@Override protected void createMap() { SurvivalMap.createRandomMap(managers, safeBoard); }

	@Override protected void setDependencies(ManagerService managers) {
		this.managers = managers;
		zombieTargeter.setZombies(managers.zombie.list);
	}

	@Override
	protected Person spawnInPlayer() {
		return managers.person.create(Params.GAME_WIDTH / 2 - 50, Params.GAME_HEIGHT / 2 -100);
	}

	
	private class ZombieMediator extends ZombieHandler {

		@Override
		protected ManagerService getManagers() {
			return SurvivalGameMode.this.managers;
		}

		@Override
		protected int getRoundNumber() {
			return SurvivalGameMode.this.getRoundNumber();
		}
	}
	
	private class RoundMediator extends SurvivalRound {

		public RoundMediator(ObjControllerI objController, boolean isServer,
				Keyboard keyboard) {
			super(objController, isServer, keyboard);
		}

		@Override
		protected ManagerService getManagers() {
			return managers;
		}
	}
	
	private class OnNewRound implements Observer.Change<Integer> {
		@Override
		public void observeChange(Integer roundNumber) {
			if (roundNumber > 1) {
				Person player = managers.person.getPlayer();
				if (player.isDead()) {
					player.giveFullHealth();
				} else {
					shop.addPoints(50);
				}
			}
		}
	}

}
