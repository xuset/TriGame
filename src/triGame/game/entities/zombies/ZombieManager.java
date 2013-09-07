package triGame.game.entities.zombies;

import java.util.ArrayList;

import tSquare.game.GameBoard;
import tSquare.game.Manager;
import tSquare.game.ManagerController;
import tSquare.game.entity.Entity;
import tSquare.math.Point;
import triGame.game.TriGame;
import triGame.game.entities.PersonManager;
import triGame.game.entities.SpawnHole;
import triGame.game.entities.building.BuildingManager;
import triGame.game.entities.wall.WallManager;


public class ZombieManager extends Manager<Zombie> {
	public static final String HASH_MAP_KEY = "zombie";
	
	private TriGame game;
	private int zombiesKilled = 0;
	
	ZombiePathFinder pathFinder;
	
	public BuildingManager getBuildingManager() { return game.buildingManager; }
	public WallManager getWallManager() { return game.wallManager; }
	public PersonManager getPersonManager() { return game.personManager; }
	public int getZombiesKilled() { return zombiesKilled; }
	public int getZombiesAlive() { return list.size(); }
	
	public ZombieManager(ManagerController controller, TriGame game, GameBoard gameBoard) {
		super(controller, gameBoard, HASH_MAP_KEY);
		this.game = game;
		pathFinder = new ZombiePathFinder(game);
	}
	
	public Zombie create() {
		return Zombie.create(this);
	}

	public Zombie createFromString(String parameters, long id) {
		String[] parameter = parameters.split(":");
		int x = Integer.parseInt(parameter[0]);
		int y = Integer.parseInt(parameter[1]);
		Zombie z = new Zombie(x, y, null, this, id);
		add(z);
		return z;
	}
	
	public boolean remove(Zombie z) {
		zombiesKilled++;
		return super.remove(z);
	}
	
	Entity determineTargetEntity() {
		int pORb = (int) (Math.random() * 100);
		if (pORb < 70) {
			return determinePerson();
		} else {
			return determineBuilding();
		}
	}
	
	private Entity determinePerson() {
		int personIndex =(int) (Math.random() * game.personManager.getList().size());
		return game.personManager.get(personIndex);
	}
	
	private Entity determineBuilding() {
		if (game.buildingManager.getList().isEmpty() == false) {
			int buildingIndex = (int) (Math.random() * game.buildingManager.getList().size());
			return game.buildingManager.getList().get(buildingIndex);
		}
		return determinePerson();
	}
	
	private static final int maxSpawnDistance = 600;
	private SpawnHole[] spawnHoleDistance = new SpawnHole[1];
	Point determineSpawnLocation(Entity target) {
		ArrayList<SpawnHole> spawnHoles = game.spawnHoleManager.getList();
		if (spawnHoleDistance.length != spawnHoles.size())
			spawnHoleDistance = new SpawnHole[spawnHoles.size()];
		int size = 0;
		for (SpawnHole sh : spawnHoles) {
			int distance = (int) Point.distance(sh.getX(), sh.getY(), target.getX(), target.getY());
			if (distance < maxSpawnDistance) {
				spawnHoleDistance[size] = sh;
				size++;
			}
		}
		if (size == 0) {
			SpawnHole[] initials = game.spawnHoleManager.initialSpawnHoles;
			SpawnHole shortest = initials[0];
			int shortestDistance = (int) Point.distance(shortest.getX(), shortest.getY(), target.getX(), target.getY());
			for (SpawnHole sh : initials) {
				int distance = (int) Point.distance(sh.getX(), sh.getY(), target.getX(), target.getY());
				if (distance < shortestDistance) {
					shortest = sh;
					shortestDistance = distance;
				}
			}
			return new Point(shortest.getCenter());
		} else {
			SpawnHole sh;
			double rand = Math.random();
			double index = size * rand;
			sh = spawnHoleDistance[(int) index];
			return new Point(sh.getCenterX(), sh.getCenterY());
		}
	}

}
