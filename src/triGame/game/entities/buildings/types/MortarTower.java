package triGame.game.entities.buildings.types;

import tSquare.game.entity.Entity;
import tSquare.game.entity.EntityKey;
import tSquare.game.particles.ParticleController;
import tSquare.math.Point;
import triGame.game.ManagerService;
import triGame.game.shopping.ShopItem;

public class MortarTower extends Tower {
	
	private final long shootDelay = 1500;
	private final int speed = 250;
	private final int damage = -100;
	
	private long lastShootTime = 0;
	
	@Override
	public int getVisibilityRadius() { return INFO.visibilityRadius; }

	public MortarTower(double x, double y, ParticleController pc,
			ManagerService managers, EntityKey key) {
		
		super(x, y, pc, managers, INFO, key);
	}
	
	@Override
	protected void shootAtTarget(Entity target) {
		if (lastShootTime + shootDelay > System.currentTimeMillis())
			return;
		
		double dist = Point.distance(getCenterX(), getCenterY(), target.getX(), target.getY());
		if (dist < INFO.visibilityRadius) {
			
			double angle = Point.degrees(this.getCenterX(), this.getCenterY(),target.getCenterX(), target.getCenterY());
			int x = (int) getCenterX();
			int y = (int) getCenterY();
			
			managers.projectile.mortorCreate(x, y, angle, speed, damage);
			lastShootTime = System.currentTimeMillis();
		}
	}

	public static final BuildingInfo INFO = new BuildingInfo(
			"media/MortarTower.png",    //spriteId
			"mortarTower",    //Creator hash map key
			200,        //visibilityRadius
			"'BOOM,' says the mortar tower.",
			new ShopItem("Mortar tower", 500),
			true,    //has a healthBar
			false,    //has an UpgradeManager
			true,   //is interactive
			15,     //zombie target selection weight
			200     //max health
	);
}
