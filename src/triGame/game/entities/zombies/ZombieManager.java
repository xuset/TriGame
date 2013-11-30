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
import triGame.game.entities.Person;
import triGame.game.entities.PointParticle;
import triGame.game.entities.SpawnHole;
import triGame.game.entities.buildings.Building;
import triGame.game.entities.buildings.BuildingManager;
import triGame.game.shopping.ShopManager;


public class ZombieManager extends Manager<Zombie> {
	public static final String HASH_MAP_KEY = "zombie";
	
	private int zombiesKilled = 0;

	private final LocationCreator<Zombie> creator;
	private final LocationCreator<Zombie> bossCreator;
	private final ManagerService managers;
	private final ZombiePathFinder pathFinder;
	private final boolean isServer;
	private final ParticleController particles;
	private final ShopManager shop;
	
	public int getZombiesKilled() { return zombiesKilled; }
	public int getZombiesAlive() { return list.size(); }
	
	public ZombieManager(ManagerController controller, ManagerService managers,
			BuildingManager buildingManager, boolean isServer,
			ShopManager shop, ParticleController particles) {
		
		super(controller, HASH_MAP_KEY);
		pathFinder = new ZombiePathFinder(managers, buildingManager);
		this.isServer = isServer;
		this.particles = particles;
		this.managers = managers;
		this.shop = shop;
		
		creator = new LocationCreator<Zombie>(HASH_MAP_KEY, controller.creator, 
				new LocationCreator.IFace<Zombie>() {
					@Override
					public Zombie create(double x, double y, EntityKey key) {
						ZombieManager m = ZombieManager.this;
						return new Zombie(Zombie.SPRITE_ID, x, y, m.managers,
								m.isServer, m.pathFinder, key);
					}
				});
		
		bossCreator = new LocationCreator<Zombie>("BossCreator", controller.creator,
				new LocationCreator.IFace<Zombie>() {
					@Override
					public Zombie create(double x, double y, EntityKey key) {
						ZombieManager m = ZombieManager.this;
						return new BossZombie(x, y, m.managers, m.isServer, m.pathFinder, key);
					}
				});
	}
	
	public Zombie createBoss(int roundNumber) {
		final int speed = 30;
		final long spawnDelay = 0;
		
		Building hq = managers.building.getHQ();
		Point spawn = determineSpawnLocation(hq);
		
		BossZombie boss = (BossZombie) bossCreator.create(spawn.x, spawn.y, this);
		setAttributes(boss, speed, spawnDelay, hq);
		int health = (roundNumber / 10) * 300 + 100;
		boss.setMaxHealth(health);
		return boss;
	}
	
	public Zombie create(int roundNumber) {
		final long initialSpawnDelay = 3000;
		final int initialSpeed = 50;
		
		int speed = (int) (initialSpeed + 0.15 * roundNumber * roundNumber);
		speed = (speed > 350) ? 350 : speed;
		long spawnDelay = initialSpawnDelay - 75 * roundNumber;
		spawnDelay = (spawnDelay < 0l) ? 0l : spawnDelay;
		
		Entity target = DETERMINE_TARGET(managers);
		Point spawn = determineSpawnLocation(target);
		
		Zombie z = creator.create(spawn.x, spawn.y, this);
		setAttributes(z, speed, spawnDelay, target);
		return z;
		
	}
	
	private void setAttributes(Zombie z, int speed, long spawnDelay, Entity target) {		
		z.target = target;
		z.spawnTime = spawnDelay;
		z.speed = speed;
	}
	
	@Override
	public void draw(Graphics2D g, ViewRect rect) {
		pathFinder.getDrawer().draw(g, rect);
		super.draw(g, rect);
	}
	
	@Override
	protected void onRemove(Entity z) {
		if (z.getSpriteId().equals(BossZombie.SPRITE_ID)) {
			for (int i = 0; i < 10; i++) {
				Point var = new Point(z.getCenterX(), z.getCenterY());
				var = var.translate(Math.random() * 50 - 25, Math.random() * 50 - 25);
				int time = (int) (Math.random() * 400 + 500);
				PointParticle p = new PointParticle.Floating(var.intX(), var.intY(), time);
				particles.addParticle(p);
			}
			shop.addPoints(200);
		}
		zombiesKilled++;
		PointParticle p = new PointParticle.Floating((int) z.getCenterX(),(int)  z.getCenterY(), 800);
		particles.addParticle(p);
	}
	
	private static final int maxSpawnDistance = 600;
	private static final int maxBufferSize = 15;
	Point determineSpawnLocation(Entity target) {
		final int width = Sprite.get(Zombie.SPRITE_ID).getWidth();
		final int height = Sprite.get(Zombie.SPRITE_ID).getHeight();
		
		if (target == null) {
			System.err.println("Zombie's target is null.");
			return new Point(0,0);
		}
		
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
			return new Point(shortest.getCenter()).translate(-width/2, -height/2);
		} else {
			SpawnHole sh;
			double rand = Math.random();
			double index = size * rand;
			sh = buffer[(int) index];
			return new Point(sh.getCenterX() - width/2, sh.getCenterY() - height/2);
		}
	}
	
	static Entity DETERMINE_TARGET(ManagerService managers) {
		final int personWeight = 50;
		Collection<Building> buildings = managers.building.interactives;
		Collection<Person> persons = managers.person.list;
		
		int totalWeights = 0;
		for (Person p : persons) {
			if (!p.removeRequested() && !p.isDead())
				totalWeights += personWeight;
		}
		for (Building b : buildings)
			totalWeights += b.info.selectionWeight;
		
		int selected = (int) (Math.random() * totalWeights);
		int sum = 0;
		for (Person p : persons) {
			if (!p.removeRequested() && !p.isDead())
				sum += personWeight;
			if (sum > selected)
				return p;
		}
		for (Building b : buildings) {
			sum += b.info.selectionWeight;
			if (sum > selected)
				return b;
		}
		return null;
	}
}
