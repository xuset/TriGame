package net.xuset.triGame.game.versus;

import net.xuset.objectIO.netObject.ObjControllerI;
import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.math.point.IPointR;
import net.xuset.tSquare.util.Observer;
import net.xuset.triGame.game.Draw;
import net.xuset.triGame.game.GameMode;
import net.xuset.triGame.game.GameRound;
import net.xuset.triGame.game.ManagerService;
import net.xuset.triGame.game.PlayerInfoContainer;
import net.xuset.triGame.game.SafeBoard;
import net.xuset.triGame.game.entities.Person;
import net.xuset.triGame.game.entities.zombies.Zombie;
import net.xuset.triGame.game.entities.zombies.ZombieHandler;
import net.xuset.triGame.game.entities.zombies.ZombieTargeter;
import net.xuset.triGame.game.shopping.ShopManager;
import net.xuset.triGame.game.ui.gameInput.IRoundInput;



public class VersusGameMode extends GameMode {
	private final ShopManager shop;
	private final VersusMap gameMap;
	private final VersusSafeBoard safeBoard;
	private final VersusZombie zombieHandler;
	private final VersusRound gameRound;
	private final VersusTargeter zombieTargeter = new VersusTargeter();
	private final IsGameOver entireGameOver = new IsGameOver();
	
	private ManagerService managers;
	private Person player;
	private boolean iAmAWinner = false;

	public VersusGameMode(ShopManager shop, boolean isServer,
			ObjControllerI objController, IRoundInput roundInput,
			PlayerInfoContainer playerContainer) {

		this.shop = shop;
		gameMap = new VersusMap();
		safeBoard = new VersusSafeBoard(gameMap.playableArea);
		gameRound = new VersusRound(objController, isServer, roundInput, gameMap,
				entireGameOver, playerContainer);
		zombieHandler = new VersusZombie(gameMap, gameRound.onNewRound);
		
		gameRound.onNewRound.watch(new RoundObserver());
	}

	@Override public boolean isGameOver() { return didLoose(); }
	@Override public SafeBoard getSafeBoard() { return safeBoard; }
	@Override public ZombieHandler getZombieHandler() { return zombieHandler; }
	@Override public ZombieTargeter getZombieTargeter() { return zombieTargeter; }
	@Override protected GameRound getGameRound() { return gameRound; }
	

	@Override
	protected void createMap(double wallGenCoefficient) {
		gameMap.createMap(managers);
	}

	@Override
	public void update(int frameDelta) {
		super.update(frameDelta);
		if (didLoose())
			isGameOver.value = true;
		if (gameMap.headQuarters[0].removeRequested() &&
				gameMap.headQuarters[1].removeRequested())
			entireGameOver.value = true;
	}
	
	@Override
	public void draw(IGraphics g) {
		super.draw(g);
		if (didWin())
			iAmAWinner = true;
		if (iAmAWinner)
			Draw.drawYouWin(g);
	}

	@Override
	protected void setDependencies(ManagerService managers) {
		this.managers = managers;
		gameRound.setDependencies(managers);
		zombieHandler.setDependencies(managers);
		zombieTargeter.setDependencies(managers.zombie.list);
	}
	
	@Override
	protected void onGameStart() {
		gameMap.findAndSetHq(managers.building.list);
	}
	
	@Override
	protected Person spawnInPlayer() {
		IPointR spawn = gameMap.getSpawnForPlayer();
		player = managers.person.create(spawn.getX(), spawn.getY());
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
	
	private void onZoneFinalize() {
		if (player.hitbox.isInside(gameMap.missingWallZone)) {
			double sign = (player.getCenterX() > gameMap.missingWallZone.getCenterX()) ?
					1 : -1;
			double dist = sign * (gameMap.missingWallZone.getWidth());
			player.setX(player.getX() + dist);
		}
		
		int playerZone = gameMap.getZoneNumber(player);
		safeBoard.setPlayableArea(gameMap.playables[playerZone]);
		
		gameRound.spawner.setPlayerZoneAndTargets(
				gameMap.getZoneNumber(player),
				gameMap.headQuarters );
	}
	
	private void onRoundStart() {
		if (player.isDead() && !didLoose())
			player.giveFullHealth();
		else if (getRoundNumber() != 1)
			shop.addPoints(65);
	}
	
	private class RoundObserver implements Observer.Change<Integer> {
		@Override
		public void observeChange(Integer round) {
			if (round == 1)
				onZoneFinalize();
			onRoundStart();
			
		}
	}
	
	private class VersusTargeter extends ZombieTargeter {
		@Override
		protected boolean isValidZombie(double x, double y, Zombie z) {
			return (gameMap.getZoneNumber(x, y) == gameMap.getZoneNumber(z));
		}
	}
}
