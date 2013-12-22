package triGame.game.versus;

import java.awt.Graphics2D;
import objectIO.netObject.ObjControllerI;
import tSquare.game.GameBoard.ViewRect;
import tSquare.system.PeripheralInput;
import tSquare.util.Observer;
import triGame.game.Draw;
import triGame.game.GameMode;
import triGame.game.GameRound;
import triGame.game.ManagerService;
import triGame.game.Params;
import triGame.game.SafeBoard;
import triGame.game.entities.Person;
import triGame.game.entities.zombies.Zombie;
import triGame.game.entities.zombies.ZombieHandler;
import triGame.game.entities.zombies.ZombieTargeter;
import triGame.game.shopping.ShopManager;
import triGame.game.ui.UserInterface;


public class VersusGameMode extends GameMode {
	private final ShopManager shop;
	private final VersusMap gameMap;
	private final VersusSafeBoard safeBoard;
	private final VersusZombie zombieHandler;
	private final VersusRound gameRound;
	private final VersusTargeter zombieTargeter = new VersusTargeter();
	
	private ManagerService managers;
	private Person player;
	private boolean iAmAWinner = false;

	public VersusGameMode(ShopManager shop, boolean isServer, ObjControllerI objController,
			PeripheralInput.Keyboard keyboard) {

		this.shop = shop;
		gameMap = new VersusMap();
		safeBoard = new VersusSafeBoard(gameMap.playableArea);
		gameRound = new VersusRound(objController, isServer, keyboard, gameMap);
		zombieHandler = new VersusZombie(gameMap, gameRound.onNewRound);
		
		gameRound.onNewRound.watch(new RoundObserver());
	}

	@Override public boolean isGameOver() { return didLoose(); }
	@Override public SafeBoard getSafeBoard() { return safeBoard; }
	@Override public ZombieHandler getZombieHandler() { return zombieHandler; }
	@Override protected void createMap() { gameMap.createMap(managers); }
	@Override public ZombieTargeter getZombieTargeter() { return zombieTargeter; }
	@Override protected GameRound getGameRound() { return gameRound; }

	@Override
	public void performLogic(int frameDelta) {
		super.performLogic(frameDelta);
		
		gameRound.spawner.setPlayerZoneAndTargets(
				gameMap.getZoneNumber(player),
				gameMap.headQuarters );
	}
	
	@Override
	public void draw(Graphics2D g, ViewRect rect) {
		super.draw(g, rect);
		if (didWin())
			iAmAWinner = true;
		if (iAmAWinner)
			Draw.drawYouWin(g, rect);
	}

	@Override
	protected void setDependencies(ManagerService managers, UserInterface ui) {
		this.managers = managers;
		gameRound.setDependencies(managers);
		zombieHandler.setDependencies(managers);
		zombieTargeter.setDependencies(managers.zombie.list);
		ui.arsenal.panel.groups.add(new ZombieUI(ui.focus, gameRound.spawner, shop));
	}
	
	@Override
	protected void onGameStart() {
		gameMap.findAndSetHq(managers.building.list);
	}
	
	@Override
	protected Person spawnInPlayer() {
		int x = (VersusMap.OFFSET_BLOCK_X + VersusMap.ZONE_SIZE - 1)* Params.BLOCK_SIZE;
		int y = (VersusMap.OFFSET_BLOCK_Y + 6) * Params.BLOCK_SIZE;
		if (Math.random() < 0.5)
			x += Params.BLOCK_SIZE;
		player = managers.person.create(x, y);
		return player;
	}
	
	private boolean didLoose() {
		int zone = gameMap.getZoneNumber(player);
		return gameMap.isZoneHQDead(zone, managers);
	}
	
	private boolean didWin() {
		int myZone = gameMap.getZoneNumber(player);
		int opposingZone = (myZone == 0) ? 1 : 0;
		return gameMap.isZoneHQDead(opposingZone, managers) &&
				!gameMap.isZoneHQDead(myZone, managers);
	}
	
	private class RoundObserver implements Observer.Change<Integer> {
		@Override
		public void observeChange(Integer round) {
			if (round == 1) {
				int playerZone = gameMap.getZoneNumber(player);
				safeBoard.setPlayableArea(gameMap.playables[playerZone]);
			}
		}
	}
	
	private class VersusTargeter extends ZombieTargeter {
		@Override
		protected boolean isValidZombie(double x, double y, Zombie z) {
			return (gameMap.getZoneNumber(x, y) == gameMap.getZoneNumber(z));
		}
	}

}
