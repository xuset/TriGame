package triGame.game.entities.zombies;

import java.awt.Graphics2D;

import tSquare.game.GameBoard.ViewRect;
import tSquare.game.entity.Entity;
import tSquare.game.entity.EntityKey;
import tSquare.game.entity.LocationCreator;
import tSquare.game.entity.Manager;
import tSquare.game.entity.ManagerController;
import tSquare.game.particles.ParticleController;
import tSquare.math.Point;
import triGame.game.GameMode;
import triGame.game.ManagerService;
import triGame.game.entities.PointParticle;
import triGame.game.entities.buildings.Building;
import triGame.game.entities.buildings.BuildingManager;
import triGame.game.shopping.ShopManager;


public class ZombieManager extends Manager<Zombie> {
	public static final String HASH_MAP_KEY = "zombie";
	
	private int zombiesKilled = 0;

	final LocationCreator<Zombie> creator;
	final LocationCreator<Zombie> bossCreator;
	private final ManagerService managers;
	private final GameMode gameMode;
	private final ZombiePathFinder pathFinder;
	private final boolean isServer;
	private final ParticleController particles;
	private final ShopManager shop;
	
	public int getZombiesKilled() { return zombiesKilled; }
	public int getZombiesAlive() { return list.size(); }
	
	public ZombieManager(ManagerController controller, ManagerService managers,
			GameMode gameMode, BuildingManager buildingManager,
			boolean isServer, ShopManager shop, ParticleController particles) {
		
		super(controller, HASH_MAP_KEY);
		this.gameMode = gameMode;
		this.isServer = isServer;
		this.particles = particles;
		this.managers = managers;
		this.shop = shop;
		pathFinder = new ZombiePathFinder(managers, buildingManager);
		
		creator = new LocationCreator<Zombie>(HASH_MAP_KEY, controller.creator, 
				new LocationCreator.IFace<Zombie>() {
					@Override
					public Zombie create(double x, double y, EntityKey key) {
						ZombieManager m = ZombieManager.this;
						return new Zombie(Zombie.SPRITE_ID, x, y, m.managers,
								m.gameMode.getZombieHandler(), m.isServer, m.pathFinder, m.shop, key);
					}
				});
		
		bossCreator = new LocationCreator<Zombie>("BossCreator", controller.creator,
				new LocationCreator.IFace<Zombie>() {
					@Override
					public Zombie create(double x, double y, EntityKey key) {
						ZombieManager m = ZombieManager.this;
						return new BossZombie(x, y, m.managers, m.gameMode.getZombieHandler(),
								m.isServer, m.pathFinder, m.shop, key);
					}
				});
	}
	
	public Zombie createBoss() {
		final int roundNumber = gameMode.getRoundNumber();
		final int speed = 30;
		final int health = (roundNumber * roundNumber) * 15 * managers.person.list.size();
		final Building hq = managers.building.getHQ();
		
		return createBoss(health, speed, hq, 40);
	}
	
	public Zombie createBoss(int health, int speed, Entity target, int buildingG) {
		final long spawnDelay = 0;
		final Point spawn = gameMode.getZombieHandler().setSpawnPoint(target);
		
		BossZombie boss = (BossZombie) bossCreator.create(spawn.x, spawn.y, this);
		setAttributes(boss, speed, spawnDelay, target, health, buildingG);
		
		return boss;
	}
	
	public Zombie create() {
		Entity target = gameMode.getZombieHandler().findTarget(null);
		if (target == null)
			return null;
		int speed = gameMode.getZombieHandler().determineSpeed();
		long spawnDelay = gameMode.getZombieHandler().determineSpawnDelay();
		int health = gameMode.getZombieHandler().determineHealth();
		int buildingG = gameMode.getZombieHandler().determinePathBuildingG();
		Point spawn = gameMode.getZombieHandler().setSpawnPoint(target);
		
		Zombie z = creator.create(spawn.x, spawn.y, this);
		setAttributes(z, speed, spawnDelay, target, health, buildingG);
		return z;
		
	}
	
	private void setAttributes(Zombie z, int speed, long spawnDelay,
			Entity target, int health, int buildingG) {	
		
		z.target = target;
		z.spawnTime = spawnDelay;
		z.speed = speed;
		z.additionalBuildingG = buildingG;
		z.setMaxHealth(health);
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
		}
		zombiesKilled++;
		PointParticle p = new PointParticle.Floating((int) z.getCenterX(),(int)  z.getCenterY(), 800);
		particles.addParticle(p);
	}
}
