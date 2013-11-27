package triGame.game.entities.zombies;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;

import tSquare.game.GameBoard.ViewRect;
import tSquare.game.entity.Entity;
import tSquare.game.entity.EntityKey;
import tSquare.game.entity.LocationCreator;
import tSquare.game.entity.Manager;
import tSquare.game.entity.ManagerController;
import tSquare.game.particles.ParticleController;
import tSquare.imaging.Sprite;
import tSquare.math.Point;
import triGame.game.ManagerService;
import triGame.game.entities.PointParticle;
import triGame.game.entities.SpawnHole;
import triGame.game.entities.buildings.Building;
import triGame.game.entities.buildings.BuildingManager;


public class ZombieManager extends Manager<Zombie> {
	public static final String HASH_MAP_KEY = "zombie";
	
	private int zombiesKilled = 0;

	private final LocationCreator<Zombie> creator;
	private final ManagerService managers;
	private final ZombiePathFinder pathFinder;
	private final boolean isServer;
	private final ParticleController particles;
	
	public int getZombiesKilled() { return zombiesKilled; }
	public int getZombiesAlive() { return list.size(); }
	
	public ZombieManager(ManagerController controller, ManagerService managers,
			BuildingManager buildingManager, boolean isServer, ParticleController particles) {
		
		super(controller, HASH_MAP_KEY);
		pathFinder = new ZombiePathFinder(managers, buildingManager);
		this.isServer = isServer;
		this.particles = particles;
		this.managers = managers;
		
		creator = new LocationCreator<Zombie>(HASH_MAP_KEY, controller.creator, 
				new LocationCreator.IFace<Zombie>() {
					@Override
					public Zombie create(double x, double y, EntityKey key) {
						return new Zombie(x, y, ZombieManager.this.managers,
								ZombieManager.this.isServer, pathFinder, key);
					}
				});
	}
	
	public Zombie create() {
		Entity target = DETERMINE_TARGET(managers);
		Point spawn = determineSpawnLocation(target);
		Sprite s = Sprite.get(Zombie.SPRITE_ID);
		int width = s.getWidth();
		int height = s.getHeight();
		Zombie z = creator.create(spawn.intX() - width / 2, spawn.intY() - height / 2, this);
		z.target = target;
		
		return z;
	}
	
	@Override
	public void draw(Graphics2D g, ViewRect rect) {
		pathFinder.getDrawer().draw(g, rect);
		super.draw(g, rect);
	}
	
	@Override
	protected void onRemove(Entity z) {
		zombiesKilled++;
		PointParticle p = new PointParticle.Floating((int) z.getCenterX(),(int)  z.getCenterY(), 800);
		particles.addParticle(p);
	}
	
	private static final int maxSpawnDistance = 600;
	private static final int maxBufferSize = 15;
	Point determineSpawnLocation(Entity target) {
		ArrayList<SpawnHole> spawnHoles = managers.spawnHole.list;
		SpawnHole[] buffer = new SpawnHole[maxBufferSize];
		
		int size = 0;
		for (SpawnHole sh : spawnHoles) {
			int distance = (int) Point.distance(sh.getX(), sh.getY(), target.getX(), target.getY());
			if (distance < maxSpawnDistance && size < maxBufferSize) {
				buffer[size] = sh;
				size++;
			}
		}
		if (size == 0) {
			SpawnHole[] initials = managers.spawnHole.initialSpawnHoles;
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
			sh = buffer[(int) index];
			return new Point(sh.getCenterX(), sh.getCenterY());
		}
	}
	
	static Entity DETERMINE_TARGET(ManagerService managers) {
		final int initialWeight = 50 * managers.person.list.size();
		Collection<Building> buildings = managers.building.interactives;
		int totalWeights = initialWeight;
		for (Building b : buildings)
			totalWeights += b.info.selectionWeight;
		int selected = (int) (Math.random() * totalWeights);
		if (selected < initialWeight) {
			int personSize = managers.person.list.size();
			int index = selected / (initialWeight / personSize);
			return managers.person.list.get(index);
		} else {
			int sum = initialWeight;
			for (Building b : buildings) {
				sum += b.info.selectionWeight;
				if (sum > selected)
					return b;
			}
		}
		return null;
	}
}
