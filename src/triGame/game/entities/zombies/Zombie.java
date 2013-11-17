package triGame.game.entities.zombies;

import tSquare.game.entity.Entity;
import tSquare.game.entity.EntityKey;
import tSquare.game.entity.Manager;
import tSquare.math.Point;
import tSquare.paths.Node;
import tSquare.paths.Path;
import triGame.game.ManagerService;
import triGame.game.Params;

//TODO prevent zombies humping persons upon collision

public class Zombie extends Entity {
	public static final String SPRITE_ID = "zombie";
	public static final int MAX_ZOMBIES = 100;
	private static final int spawnWaitTime = 3000; //milliseconds. time before zombie can start moving
	private static final int deltaHitTime = 10; //milliseconds. time between damage hits
	
	public double damage = -1;

	Entity target;
	private long spawnTime = 0l;
	
	private final ManagerService managers;
	private final boolean isServer;
	private final ZombiePathFinder pathFinder;
	
	private Path path;
	private Point lastTargetBlock = new Point(0, 0);
	private boolean pathToTargetExists = false;
	private int lastObjectGridModCount = 0;
	private int speed = 50;
	private long lastHitTime = 0l;
	
	public Zombie(double x, double y, ManagerService managers,
			boolean isServer, ZombiePathFinder pathFinder, EntityKey key) {
		
		super(SPRITE_ID, x, y, key);
		this.managers = managers;
		this.isServer = isServer;
		this.pathFinder = pathFinder;
		spawnTime = System.currentTimeMillis() + spawnWaitTime;
	}

	@Override
	public void performLogic(int frameDelta) {
		if (getHealth() <= 0)
			remove();
		if (isServer) {
			if (target == null || target.removeRequested())
				target = ZombieManager.DETERMINE_TARGET(managers);
			if (target != null) {
				if (System.currentTimeMillis() > spawnTime) {
					Point targetPosition = new Point(target.getCenterX(), target.getCenterY());
					Params.roundToGrid(targetPosition);
					boolean playerDidntMoved = lastTargetBlock.isEqualTo(targetPosition);
					if (managers.building.objectGrid.getModCount() !=
							lastObjectGridModCount || (pathToTargetExists && playerDidntMoved == false)) {
						findPath();
					}
					move(frameDelta);
				}
			}
		}
	}
	
	private void findPath() {
		if (pathFinder.findPath(path, this)) {
			pathToTargetExists = true;
			lastTargetBlock.x = Params.roundToGrid(target.getCenterX());
			lastTargetBlock.y = Params.roundToGrid(target.getCenterY());
			path = pathFinder.buildPath();
		} else {
			pathToTargetExists = false;
			path = null;
		}
		lastObjectGridModCount = managers.building.objectGrid.getModCount();
	}
	
	public void move(int frameDelta) {
		double distance = (speed * frameDelta) / 1000.0;
		if (path != null && path.peekNextStep() != null) {
			Node.Point step = path.peekNextStep();
			setAngle(Point.degrees(this.getCenterX(), this.getCenterY(), step.x, step.y));
			moveForward(distance);
			if (Math.abs(this.getCenterX() - step.x) < 1 && Math.abs(this.getCenterY() - step.y) < 1) {
				path.pollNextStep();
			}
		} else {
			turn(target.getCenterX(), target.getCenterY());
			moveForward(distance);
		}
		if (!inflictDamage(managers.person, frameDelta)) {
			inflictDamage(managers.building, frameDelta);
		}
	}
	
	private boolean inflictDamage(Manager<?> manager, int frameDelta) {
		Entity e = collidedWithFirst(manager.list);
		if (e != null) {
			if (lastHitTime + deltaHitTime < System.currentTimeMillis()) {
				e.modifyHealth(damage);
				lastHitTime = System.currentTimeMillis();
			}
			moveForward(-speed * frameDelta / 1000.0);
			return true;
		}
		return false;
	}
	
	public void hitBack(int distance) {
		distance = distance * -1;
		moveForward(distance);
		if (collidedWithFirst(managers.building.list) != null) {
			moveForward(-distance);
		}
	}
}
