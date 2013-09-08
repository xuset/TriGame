package triGame.game.projectile;

import tSquare.game.entity.Entity;
import triGame.game.projectile.gun.GunShotgun;
import triGame.game.shopping.UpgradeManager;

public class ProjectileShotgun extends Projectile {
	public static final String IDENTIFIER = "shotgun";
	public static final String SPRITE_ID = ProjectilePistol.SPRITE_ID;
	private static final int initialBursts = 3, initialSpeed = 700, initialDamage = -20;
	private static final double initialDeltaTheta = 10;
	
	private Entity[] bullets;
	private int x, y, speed, bursts, damage = initialDamage; //deltaTheta = degrees/radians between bullets; bursts = amount of bullets per shot
	private double theta, deltaTheta;
	
	public ProjectileShotgun(int x, int y, double theta, int bursts, double deltaTheta, int speed, ProjectileContainer container, long ownerId) {
		super(container, ownerId);
		this.x = x;
		this.y = y;
		this.theta = theta;
		this.bursts = bursts;
		this.deltaTheta = deltaTheta;
		this.speed = speed;
		bullets = new Entity[bursts];
		for (int i = 0; i < bursts; i++) {
			Entity e = new Entity(SPRITE_ID, x, y); 
			double relativeAngle = deltaTheta * (i + 1 - (bursts + 1) / 2.0);
			e.setAngle(relativeAngle + theta);
			bullets[i] = e;
		}
	}
	
	public static ProjectileShotgun create(Entity start, ProjectileContainer container, UpgradeManager upgrade) {
			int x = (int) start.getCenterX();
			int y = (int) start.getCenterY();
			double theta = start.getAngle();
			int bursts = initialBursts;
			double deltaTheta = initialDeltaTheta - upgrade.getUpgrade(GunShotgun.RANGE_ITEM).upgradeCount * 2;
			int speed = initialSpeed;
			ProjectileShotgun ps = new ProjectileShotgun(x, y, theta, bursts, deltaTheta, speed, container, container.getUserId());
			ps.damage = initialDamage - upgrade.getUpgrade(GunShotgun.DAMAGE_ITEM).upgradeCount * 10;
			container.getCreator().createOnNetwork(ps);
			container.add(ps);
			return ps;
	}

	public void performLogic() {
		for (int i = 0; i < bullets.length; i++) {
			if (bullets[i] != null) {
				Entity e = bullets[i];
				e.moveForward(speed * container.getDelta() / 1000);
				if (!CollisionHandling.doEdgeCollision(e, this, container)) {
					if (!CollisionHandling.doZombieCollision(e, damage, this, container)) {
						if (CollisionHandling.doBuildingCollision(e, damage, this, container)) {
							bullets[i] = null;
						}
					} else {
						bullets[i] = null;
					}
				} else {
					bullets[i] = null;
				}
			}
		}
	}

	public void draw() {
		for (int i = 0; i < bullets.length; i++) {
			if (bullets[i] != null)
				bullets[i].draw(container.gameBoard);
		}
	}
	
	public void remove() {
		boolean allNull = true;
		for (int i = 0; allNull && i < bullets.length; i++) {
			if (bullets[i] != null)
				allNull = false;
		}
		if (allNull == true) {
			super.remove();
		}
	}
	
	public static ProjectileShotgun createFromString(String parameters, ProjectileContainer container, long ownerId) {
		String[] parameter = parameters.split(":");
		int x = Integer.parseInt(parameter[0]);
		int y = Integer.parseInt(parameter[1]);
		double theta = Double.parseDouble(parameter[2]);
		int bursts = Integer.parseInt(parameter[3]);
		double deltaTheta = Double.parseDouble(parameter[4]);
		int speed = Integer.parseInt(parameter[5]);
		ProjectileShotgun ps = new ProjectileShotgun(x, y, theta, bursts, deltaTheta, speed, container, ownerId);
		container.add(ps);
		return ps;
	}

	public String createToString() {
		return (int) x + ":" + (int) y + ":" + theta + ":" + bursts + ":" + deltaTheta + ":" + speed;
	}
	
	public String getIdentifier() {
		return IDENTIFIER;
	}

}
