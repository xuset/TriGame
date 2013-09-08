package triGame.game.projectile;

import tSquare.game.entity.Entity;
import triGame.game.projectile.gun.GunSub;
import triGame.game.shopping.UpgradeManager;

public class ProjectileSub extends ProjectilePistol{
	private static final int initialSpeed = 900;

	protected ProjectileSub(int x, int y, double theta, ProjectileContainer container, long ownerId) {
		super(x, y, theta, container, ownerId);
		this.speed = initialSpeed;
	}
	
	public static ProjectileSub create(Entity start, ProjectileContainer container, UpgradeManager upgrade) {
		int x = (int) start.getCenterX();
		int y = (int) start.getCenterY();
		double theta = start.getAngle();
		ProjectileSub p = new ProjectileSub(x, y, theta, container, container.getUserId());
		p.damage = upgrade.getUpgrade(GunSub.DAMAGE_ITEM).value;
		container.getCreator().createOnNetwork(p);
		container.add(p);
		return p;
	}
	
	public String getIdentifier() {
		return ProjectilePistol.IDENTIFIER;
	}
	
}
