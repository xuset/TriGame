package triGame.game.versus;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import objectIO.netObject.ObjControllerI;
import tSquare.game.GameBoard.ViewRect;
import tSquare.game.entity.Entity;
import tSquare.math.Point;
import tSquare.system.PeripheralInput;
import tSquare.util.Observer;
import triGame.game.Draw;
import triGame.game.GameMode;
import triGame.game.GameRound;
import triGame.game.ManagerService;
import triGame.game.Params;
import triGame.game.SafeBoard;
import triGame.game.entities.Person;
import triGame.game.entities.buildings.Building;
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

	public VersusGameMode(ShopManager shop, ObjControllerI objController, boolean isServer, 
			PeripheralInput.Keyboard keyboard) {
		
		super(isServer);
		this.shop = shop;
		gameMap = new VersusMap();
		safeBoard = new VersusSafeBoard(gameMap.playableArea);
		zombieHandler = new VersusZombie();
		gameRound = new VersusRound(objController, keyboard);
		
		gameRound.onNewRound.watch(new RoundObserver());
	}

	@Override public boolean isGameOver() { return didLoose(); }
	@Override public SafeBoard getSafeBoard() { return safeBoard; }
	@Override public ZombieHandler getZombieHandler() { return zombieHandler; }
	@Override protected void createMap() { gameMap.createMap(managers); }
	@Override public ZombieTargeter getZombieTargeter() { return zombieTargeter; }
	@Override protected GameRound getGameRound() { return gameRound; }

	@Override
	protected void setDependencies(ManagerService managers, UserInterface ui) {
		this.managers = managers;
		zombieTargeter.setZombies(managers.zombie.list);
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
	
	public boolean didLoose() {
		int zone = gameMap.getZoneNumber(player);
		return gameMap.isZoneHQDead(zone, managers);
	}
	
	public boolean didWin() {
		int myZone = gameMap.getZoneNumber(player);
		int opposingZone = (myZone == 0) ? 1 : 0;
		return gameMap.isZoneHQDead(opposingZone, managers) &&
				!gameMap.isZoneHQDead(myZone, managers);
	}
	
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
	
	private class RoundObserver implements Observer.Change<Integer> {
		@Override
		public void observeChange(Integer round) {
			if (round == 1) {
				int playerZone = gameMap.getZoneNumber(player);
				safeBoard.setPlayableArea(gameMap.playables[playerZone]);
			}
		}
	}
	
	private class VersusZombie extends ZombieHandler {
		@Override protected ManagerService getManagers() { return VersusGameMode.this.managers; }
		@Override protected int getRoundNumber() { return VersusGameMode.this.getRoundNumber(); }
		
		@Override
		protected Point setSpawnPoint(Entity target) {
			int zone = gameMap.getZoneNumber(target.getX(), target.getY());
			Point[] locations = gameMap.getSpawns(zone);
			int index = (int) (Math.random() * locations.length);
			return locations[index];
		}
		
		int spawnZone = -1;
		
		@Override
		protected Entity findTarget(Zombie z) {
			if (z == null) {
				spawnZone = (int) (Math.random() * 2);
			}
			Entity target =  super.findTarget(z);
			spawnZone = -1;
			return target;
		}
		
		@Override
		protected boolean isPersonValid(Person p, Zombie z) {
			return sameZone(p, z) && super.isPersonValid(p, z);
		}
		
		@Override
		protected boolean isBuildingValid(Building b, Zombie z) {
			return sameZone(b, z) && super.isBuildingValid(b, z);
		}
		
		private boolean sameZone(Entity a, Entity b) {
			int aZone = gameMap.getZoneNumber(a);
			if (spawnZone != -1)
				return spawnZone == aZone;
			
			return (gameMap.getZoneNumber(a) == gameMap.getZoneNumber(b));
		}
		
		@Override
		protected int determinePathBuildingG() {
			return 80;
		}
		
	}
	
	private class VersusRound extends GameRound {
		private final PeripheralInput.Keyboard keyboard;
		private final VersusSpawner spawner;
		private long nextRoundStartTime = 0;
		private boolean gameStarted = false;

		public VersusRound(ObjControllerI objController, PeripheralInput.Keyboard keyboard) {
			super(objController);
			this.keyboard = keyboard;
			spawner = new VersusSpawner(objController);
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
			if (gameStarted && nextRoundStartTime < System.currentTimeMillis())
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
	}
	
	private class VersusTargeter extends ZombieTargeter {
		@Override
		protected boolean isValidZombie(double x, double y, Zombie z) {
			return (gameMap.getZoneNumber(x, y) == gameMap.getZoneNumber(z));
		}
	}

}
