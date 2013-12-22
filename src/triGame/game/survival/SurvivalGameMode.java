package triGame.game.survival;

import objectIO.netObject.ObjControllerI;
import tSquare.system.PeripheralInput;
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
import triGame.game.ui.UserInterface;


public class SurvivalGameMode extends GameMode {
	private ManagerService managers;
	private final ShopManager shop;
	private final SurvivalSafeBoard safeBoard;
	private final ZombieHandler zombieHandler;
	private final ZombieTargeter zombieTargeter;
	private final SurvivalRound gameRound;

	public SurvivalGameMode(ShopManager shop, boolean isServer, ObjControllerI objController, PeripheralInput.Keyboard keyboard) {
		this.shop = shop;
		safeBoard = new SurvivalSafeBoard();
		zombieTargeter = new ZombieTargeter();
		gameRound = new SurvivalRound(objController, isServer, keyboard);
		zombieHandler = new ZombieHandler(gameRound.onNewRound);
		gameRound.onNewRound.watch(new OnNewRound());
	}

	@Override public boolean isGameOver() { return (managers.building.getHQ() == null); }
	@Override public SafeBoard getSafeBoard() { return safeBoard; }
	@Override public ZombieHandler getZombieHandler() { return zombieHandler; }
	@Override public ZombieTargeter getZombieTargeter() { return zombieTargeter; }
	@Override protected GameRound getGameRound() { return gameRound; }
	@Override protected void createMap() { SurvivalMap.createRandomMap(managers, safeBoard); }

	@Override protected void setDependencies(ManagerService managers, UserInterface ui) {
		this.managers = managers;
		gameRound.setDependencies(managers);
		zombieTargeter.setDependencies(managers.zombie.list);
		zombieHandler.setDependencies(managers);
	}

	@Override
	protected Person spawnInPlayer() {
		return managers.person.create(Params.GAME_WIDTH / 2 - 50, Params.GAME_HEIGHT / 2 -100);
	}
	
	private class OnNewRound implements Observer.Change<Integer> {
		@Override
		public void observeChange(Integer roundNumber) {
			if (roundNumber > 1) {
				if (roundNumber % 10 == 0)
					shop.addPoints(150);
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
