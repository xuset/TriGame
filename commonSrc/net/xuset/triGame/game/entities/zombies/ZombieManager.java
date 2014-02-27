package net.xuset.triGame.game.entities.zombies;

import net.xuset.tSquare.game.entity.Creator;
import net.xuset.tSquare.game.entity.Entity;
import net.xuset.tSquare.game.entity.EntityKey;
import net.xuset.tSquare.game.entity.Manager;
import net.xuset.tSquare.game.entity.ManagerController;
import net.xuset.tSquare.game.entity.Creator.CreateFunc;
import net.xuset.tSquare.game.particles.ParticleController;
import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.math.point.IPointR;
import net.xuset.tSquare.math.point.IPointW;
import net.xuset.tSquare.math.point.Point;
import net.xuset.triGame.game.GameMode;
import net.xuset.triGame.game.ManagerService;
import net.xuset.triGame.game.entities.PointParticle;
import net.xuset.triGame.game.entities.buildings.Building;
import net.xuset.triGame.game.entities.buildings.BuildingManager;
import net.xuset.triGame.game.shopping.ShopManager;



public class ZombieManager extends Manager<Zombie> {
	public static final String HASH_MAP_KEY = "zombie";
	
	private int zombiesKilled = 0;

	private final Creator<Zombie> zombieCreator;
	private final Creator<BossZombie> bossCreator;
	private final ManagerService managers;
	private final GameMode gameMode;
	private final ZombiePathFinder pathFinder;
	private final boolean isServer;
	private final ParticleController particles;
	private final ShopManager shop;
	
	public int getZombiesKilled() { return zombiesKilled; }
	public int getZombiesAlive() { return list.size(); }
	
	public ZombieManager(ManagerController controller, ManagerService managers,
			GameMode gameMode, BuildingManager buildingManager, boolean isServer, 
			ShopManager shop, ParticleController particles) {
		
		super(controller, HASH_MAP_KEY);
		this.gameMode = gameMode;
		this.isServer = isServer;
		this.particles = particles;
		this.managers = managers;
		this.shop = shop;
		pathFinder = new ZombiePathFinder(managers, buildingManager);
		zombieCreator = new Creator<Zombie>("Zombie", controller.creator, new ZombieCreateFunc());
		bossCreator = new Creator<BossZombie>("BossZombie", controller.creator, new BossCreateFunc());
	}
	
	public BossZombie createBoss() {
		final int roundNumber = gameMode.getRoundNumber();
		final double speed = 3.0/5.0;
		final int health = (roundNumber * roundNumber) * 15 * managers.person.list.size();
		final Building hq = managers.building.getBuildingGetter().getHQ();
		
		return createBoss(health, speed, hq, 40);
	}
	
	public BossZombie createBoss(int health, double speed, Entity target, int buildingG) {
		final long spawnDelay = 0;
		final IPointR spawn = gameMode.getZombieHandler().setSpawnPoint(target);
		
		return createBoss(spawn.getX(), spawn.getY(), target, spawnDelay, speed, buildingG, health);
	}
	
	public Zombie createZombie() {
		Entity target = gameMode.getZombieHandler().findTarget(null);
		if (target == null)
			return null;
		double speed = gameMode.getZombieHandler().determineSpeed();
		long spawnDelay = gameMode.getZombieHandler().determineSpawnDelay();
		int health = gameMode.getZombieHandler().determineHealth();
		int buildingG = gameMode.getZombieHandler().determinePathBuildingG();
		IPointR spawn = gameMode.getZombieHandler().setSpawnPoint(target);
		
		return createZombie(spawn.getX(), spawn.getY(), target, spawnDelay, speed, buildingG, health);
	}
	
	public Zombie createZombie(double x, double y, Entity target, long spawnDelay,
			double speed, int buildingG, int maxHealth) {
		
		Zombie z = new Zombie(Zombie.SPRITE_ID, x, y, target, spawnDelay, speed,
				buildingG, maxHealth, managers, gameMode.getZombieHandler(),
				isServer, pathFinder, shop, null);
		add(z);
		zombieCreator.createOnNetwork(z, this);
		return z;
	}
	
	public BossZombie createBoss(double x, double y, Entity target, long spawnDelay,
			double speed, int buildingG, int maxHealth) {
		
		BossZombie z = new BossZombie(x, y, target, spawnDelay, speed,
				buildingG, maxHealth, managers, gameMode.getZombieHandler(),
				isServer, pathFinder, shop, null);
		add(z);
		bossCreator.createOnNetwork(z, this);
		return z;
	}
	
	@Override
	public void draw(IGraphics g) {
		pathFinder.getDrawer().draw(g);
		super.draw(g);
	}
	
	@Override
	protected void onRemove(Entity z) {
		if (z.getSpriteId().equals(BossZombie.SPRITE_ID)) {
			for (int i = 0; i < 10; i++) {
				IPointW var = new Point(z.getCenterX(), z.getCenterY());
				var.translate(Math.random() * 50 - 25, Math.random() * 50 - 25);
				int time = (int) (Math.random() * 400 + 500);
				PointParticle p = new PointParticle.Floating(var.getX(), var.getY(), time);
				particles.addParticle(p);
			}
		}
		zombiesKilled++;
		PointParticle p = new PointParticle.Floating(z.getCenterX(), z.getCenterY(), 800);
		particles.addParticle(p);
	}
	
	private class ZombieCreateFunc implements CreateFunc<Zombie> {
		@Override public Zombie create(EntityKey key) {
			return new Zombie(managers, shop, key);
		}
	}
	
	private class BossCreateFunc implements CreateFunc<BossZombie> {
		@Override public BossZombie create(EntityKey key) {
			return new BossZombie(managers, shop, key);
		}
	}
}
