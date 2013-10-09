package triGame.game.entities.zombies;

import tSquare.game.entity.Entity;
import tSquare.game.entity.Manager;
import tSquare.imaging.Sprite;
import tSquare.math.IdGenerator;
import tSquare.math.Point;
import tSquare.paths.Node;
import tSquare.paths.Path;
import triGame.game.entities.building.BuildingManager;

//TODO prevent zombies humping persons upon collision

public class Zombie extends Entity {
	public static final String SPRITE_ID = "zombie";
	public static final int MAX_ZOMBIES = 100;
	
	private static final int spawnWaitTime = 3000; //milliseconds. time before zombie can start moving
	private static final int deltaHitTime = 10; //milliseconds. time between damage hits
	
	public double damage = -1;

	protected Entity target;
	
	private ZombieManager manager;
	private BuildingManager buildingManager;
	private Path path;
	private Point lastTargetBlock = new Point(0, 0);
	private boolean pathToTargetExists = false;
	private int lastObjectGridModCount = 0;
	private int speed = 50;
	private long spawnTime = 0l;
	private long lastHitTime = 0l;
	
	public Zombie(int x, int y, Entity target, ZombieManager manager, long id) {
		super(SPRITE_ID, x, y, manager, id);
		this.target = target;
		this.buildingManager = manager.getBuildingManager();
		this.manager = manager;
	}
	
	public static Zombie create(ZombieManager manager) {
		Entity target = manager.determineTargetEntity();
		if (target == null)
			return null;
		Point loc = manager.determineSpawnLocation(target);
		if (loc == null)
			return null;
		int width = Sprite.get(SPRITE_ID).getWidth();
		int height = Sprite.get(SPRITE_ID).getHeight();
		double x = loc.x - width / 2;
		double y = loc.y - height / 2;
		Zombie z = new Zombie((int) x, (int) y, target, manager, IdGenerator.getInstance().getId());
		z.createOnNetwork(true);
		z.spawnTime = System.currentTimeMillis() + spawnWaitTime;
		manager.add(z);
		return z;
	}

	@Override
	public void performLogic() {
		if (manager.isServer()) {
			if (target == null || target.isRemoved())
				target = manager.determineTargetEntity();
			if (target != null) {
				if (System.currentTimeMillis() > spawnTime) {
					boolean playerDidntMoved = Point.isEqualTo(lastTargetBlock.x, lastTargetBlock.y, buildingManager.objectGrid.roundToGridX(target.getCenterX()), buildingManager.objectGrid.roundToGridY(target.getCenterY()));
					if (buildingManager.objectGrid.modCount + manager.getWallManager().objectGrid.modCount != lastObjectGridModCount || (pathToTargetExists && playerDidntMoved == false))
						findPath();
					move();
				}
			}
		}
	}
	
	private void findPath() {
		ZombiePathFinder pathFinder = manager.pathFinder;
		pathFinder.setZombie(this);
		if (pathFinder.findPath(path)) {
			pathToTargetExists = true;
			lastTargetBlock.set(buildingManager.objectGrid.roundToGridX(target.getCenterX()), buildingManager.objectGrid.roundToGridY(target.getCenterY()));
			path = pathFinder.buildPath();
		} else {
			pathToTargetExists = false;
			path = null;
		}
		lastObjectGridModCount = manager.getWallManager().objectGrid.modCount + manager.getBuildingManager().objectGrid.modCount;
	}
	
	public void move() {
		double distance = (speed * manager.getDelta()) / 1000.0;
		if (path != null && path.peekNextStep() != null) {
			Node node = path.peekNextStep();
			setAngle(Point.degrees(this.getCenterX(), this.getCenterY(), node.getX(), node.getY()));
			moveForward(distance);
			if (Math.abs(this.getCenterX() - node.getX()) < 1 && Math.abs(this.getCenterY() - node.getY()) < 1) {
				path.pollNextStep();
			}
		} else {
			turn(target.getCenterX(), target.getCenterY());
			moveForward(distance);
		}
		if (!inflictDamage(manager.getPersonManager())) {
			if(!inflictDamage(manager.getBuildingManager())) {
				inflictDamage(manager.getWallManager());
			}
		}
	}
	
	private boolean inflictDamage(Manager<?> manager) {
		Entity e = collidedWithFirst(manager.getList());
		if (e != null) {
			if (lastHitTime + deltaHitTime < System.currentTimeMillis()) {
				e.modifyHealth(damage);
				lastHitTime = System.currentTimeMillis();
			}
			moveForward(-speed * manager.getDelta() / 1000.0);
			return true;
		}
		return false;
	}
	
	public void hitBack(int distance) {
		distance = distance * -1;
		moveForward(distance);
		if (collidedWithFirst(manager.getBuildingManager().getList()) != null ||
		collidedWithFirst(manager.getWallManager().getList()) != null) {
			moveForward(-distance);
		}
	}

	@Override
	public double modifyHealth(double delta) {
		super.modifyHealth(delta);
		if (getHealth() <= 0) {
			remove();
		}
		return getHealth();
	}

}
