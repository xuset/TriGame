package triGame.game.entities.zombies;

import objectIO.netObject.NetVar;
import objectIO.netObject.ObjControllerI;
import tSquare.game.entity.Entity;
import tSquare.game.entity.EntityKey;
import tSquare.game.entity.Manager;
import tSquare.math.Point;
import tSquare.paths.Node;
import tSquare.paths.Path;
import triGame.game.ManagerService;
import triGame.game.Params;
import triGame.game.shopping.ShopManager;

public class Zombie extends Entity {
	public static final String SPRITE_ID = "zombie";
	public static final int MAX_ZOMBIES = 100;
	private static final int hitBackDistance = 10;
	private static final int ticksPerFindPath = 5;
	
	protected final int additionalBuildingG;//higher value, less likely to break through building.

	private final long spawnTime;
	private final int speed;
	private final ManagerService managers;
	private final ZombieHandler zombieHandler;
	private final boolean isServer;
	private final ZombiePathFinder pathFinder;
	private final ShopManager shop;
	
	private int lastFindPathTick = 0;
	private int tickCount = 0;

	private Entity target;
	private double realSpeed;
	private double damage = -100; //damage per second
	private Path path;
	private Point lastTargetBlock = new Point(0, 0);
	private int lastObjectGridModCount = 0;
	
	private NetVar.nBool isSpawning;
	
	private boolean isSpawning() { return isSpawning.get(); }
	Entity getTarget() { return target; }
	public boolean isAlive() { return getHealth() > 0; }
	
	Zombie(ManagerService managers, ShopManager shop, EntityKey key) { //for clients
		super(key);
		this.shop = shop;
		this.managers = managers;
		target = null;
		zombieHandler = null;
		pathFinder = null;
		speed = 50;
		isServer = false;
		spawnTime = 0;
		additionalBuildingG = 0;
		realSpeed = speed;
	}
	
	Zombie(String spriteId, double x, double y, Entity target, long spawnDelay, int speed, int buildingG, double maxHealth, 
			ManagerService managers, ZombieHandler zombieHandler, boolean isServer, ZombiePathFinder pathFinder,
			ShopManager shop, EntityKey key) { //for server
		
		super(spriteId, x, y, key);
		this.target = target;
		this.speed = speed;
		this.additionalBuildingG = buildingG;
		this.managers = managers;
		this.zombieHandler = zombieHandler;
		this.isServer = isServer;
		this.pathFinder = pathFinder;
		this.shop = shop;
		spawnTime = System.currentTimeMillis() + spawnDelay;
		health.set(maxHealth);
	}
	
	
	public void freeze(double speedChange) {
		realSpeed = speed * speedChange;
	}
	
	public void hitByProjectile(int damage) {
		hitBack(hitBackDistance);
		if (isAlive()) {
			modifyHealth(damage);
			if (!isAlive()) {
				managers.dropPack.maybeDropPack(getCenterX(), getCenterY());
				shop.addPoints(4);
			}
		}
	}

	@Override
	public void performLogic(int frameDelta) {
		realSpeed = speed;
		if (!isServer)
			return;
		
		if (getHealth() <= 0)
			remove();
		
		if (System.currentTimeMillis() < spawnTime)
			return;
		else
			isSpawning.set(false);
		
		if (target == null || target.getHealth() <= 0 || target.removeRequested())
			target = zombieHandler.findTarget(this);
		if (target == null) {
			remove();
			return;
		}
		
		if (shouldFindNewPath())
			findPath();
		move(frameDelta);
		tickCount++;
	}
	
	@Override
	protected void setNetObjects(ObjControllerI objClass) {
		isSpawning = new NetVar.nBool(true, "isSpawning", objClass);
	}
	
	private boolean shouldFindNewPath() {
		Point targetPosition = new Point(target.getCenterX(), target.getCenterY());
		Params.roundToGrid(targetPosition);
		boolean playerMoved = !lastTargetBlock.isEqualTo(targetPosition);
		boolean modCountChanged = managers.building.objectGrid.getModCount() != lastObjectGridModCount;
		boolean refresh = lastFindPathTick + ticksPerFindPath < tickCount;
		boolean firstTime = tickCount == 0;
		if (firstTime || (refresh && (playerMoved || modCountChanged))) {
			lastFindPathTick = tickCount;
			return true;
		}
		return false;
	}
	
	private void findPath() {
		if (pathFinder.findPath(path, this))
			path = pathFinder.buildPath();
		else
			path = null;
		lastTargetBlock.x = Params.roundToGrid(target.getCenterX());
		lastTargetBlock.y = Params.roundToGrid(target.getCenterY());
		lastObjectGridModCount = managers.building.objectGrid.getModCount();
	}
	
	private void move(int frameDelta) {
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
