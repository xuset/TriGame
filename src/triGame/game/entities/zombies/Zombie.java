package triGame.game.entities.zombies;

import tSquare.game.entity.Entity;
import tSquare.game.entity.EntityKey;
import tSquare.game.entity.Manager;
import tSquare.math.Point;
import tSquare.paths.Node;
import tSquare.paths.Path;
import triGame.game.ManagerService;
import triGame.game.Params;
import triGame.game.shopping.ShopManager;

//TODO prevent zombies humping persons upon collision

public class Zombie extends Entity {
	public static final String SPRITE_ID = "zombie";
	public static final int MAX_ZOMBIES = 100;
	private static final int hitBackDistance = 10;
	private static final int ticksPerFindPath = 5;
	
	private double damage = -100; //damage per second

	Entity target;
	private int lastFindPathTick = 0;
	private int tickCount = 0;

	long spawnTime = 0l;
	int speed = 50;
	
	private final ManagerService managers;
	private final boolean isServer;
	private final ZombiePathFinder pathFinder;
	private final ShopManager shop;
	
	private int realSpeed = speed;
	private Path path;
	private Point lastTargetBlock = new Point(0, 0);
	private int lastObjectGridModCount = 0;
	
	public int getSpeed() { return realSpeed; }
	
	private boolean isSpawning() { return spawnTime > System.currentTimeMillis(); }
	
	public Zombie(String spriteId, double x, double y, ManagerService managers,
			boolean isServer, ZombiePathFinder pathFinder, ShopManager shop, EntityKey key) {
		
		super(spriteId, x, y, key);
		this.managers = managers;
		this.isServer = isServer;
		this.pathFinder = pathFinder;
		this.shop = shop;
	}
	
	void setMaxHealth(int max) {
		int actual = (int) (max - getHealth());
		modifyHealth(actual);
	}

	@Override
	public void performLogic(int frameDelta) {
		if (!isServer)
			return;
		
		if (getHealth() <= 0)
			remove();
		
		if (System.currentTimeMillis() < spawnTime)
			return;
		
		if (target == null || target.getHealth() <= 0 || target.removeRequested())
			target = ZombieManager.DETERMINE_TARGET(managers);
		if (target == null)
			return;
		
		if (shouldFindNewPath())
			findPath();
		move(frameDelta);
		tickCount++;
	}
	
	private boolean shouldFindNewPath() {
		Point targetPosition = new Point(target.getCenterX(), target.getCenterY());
		Params.roundToGrid(targetPosition);
		boolean playerMoved = !lastTargetBlock.isEqualTo(targetPosition);
		boolean modCountChanged = managers.building.objectGrid.getModCount() != lastObjectGridModCount;
		boolean refresh = lastFindPathTick + ticksPerFindPath < tickCount;
		if (refresh && (playerMoved || modCountChanged)) {
			lastFindPathTick = tickCount;
			return true;
		}
		return false;
	}
	
	private void findPath() {
		if (!pathFinder.findPath(path, this))
			throw new RuntimeException("I dont know why this happened but it shouldnt have.");
			//TODO remove this after enough time has passed. 
		
		path = pathFinder.buildPath();
		lastTargetBlock.x = Params.roundToGrid(target.getCenterX());
		lastTargetBlock.y = Params.roundToGrid(target.getCenterY());
		lastObjectGridModCount = managers.building.objectGrid.getModCount();
	}
	
	public void move(int frameDelta) {
		double distance = realSpeed * frameDelta / 1000.0;		
		
		if (path != null && path.peekNextStep() != null) {
			Node.Point step = path.peekNextStep();
			setAngle(Point.degrees(this.getCenterX(), this.getCenterY(), step.x, step.y));
			moveForward(distance);
			if (Math.abs(this.getCenterX() - step.x) < 3 && Math.abs(this.getCenterY() - step.y) < 3) {
				path.pollNextStep();
			}
		} else {
			turn(target.getCenterX(), target.getCenterY());
			moveForward(distance);
		}
		if (!inflictDamage(managers.person, frameDelta)) {
			if (!managers.building.objectGrid.isBlockOpen(getCenterX(), getCenterY())){
				inflictDamage(managers.building, frameDelta);
			}
		}
		realSpeed = speed;
	}
	
	public void freeze(double speedChange) {
		if (isServer)
			realSpeed *= speedChange;
	}
	
	private boolean inflictDamage(Manager<?> manager, int frameDelta) {
		Entity e = collidedWithFirst(manager.list);
		if (e != null && e.getHealth() > 0) {
			e.modifyHealth(damage * frameDelta / 1000.0);
			moveForward(-speed * frameDelta / 1000.0);
			return true;
		}
		return false;
	}
	
	public void hitByProjectile(int damage) {
		modifyHealth(damage);
		hitBack(hitBackDistance);
		if (getHealth() <= 0) {
			managers.healthPack.maybeDropHealth(getCenterX(), getCenterY());
			shop.addPoints(4);
		}
	}
	
	private void hitBack(int distance) {
		if (isSpawning())
			return;
		double ratio = (1.0 * realSpeed) / (1.0 * speed);
		distance = (int) (-1 * distance * ratio);
		moveForward(distance);
		if (!managers.building.objectGrid.isRectangleOpen(hitbox))
			moveForward(-1 * distance);
	}
}
