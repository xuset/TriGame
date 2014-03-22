
package net.xuset.triGame.game.entities.buildings.types;

import net.xuset.tSquare.game.entity.Entity;
import net.xuset.tSquare.game.entity.EntityKey;
import net.xuset.tSquare.game.particles.ParticleController;
import net.xuset.tSquare.math.point.PointR;
import net.xuset.triGame.game.entities.buildings.Building;
import net.xuset.triGame.game.entities.projectiles.ProjectileManager;
import net.xuset.triGame.game.entities.zombies.Zombie;
import net.xuset.triGame.game.entities.zombies.ZombieTargeter;
import net.xuset.triGame.game.shopping.ShopItem;
import net.xuset.triGame.game.shopping.UpgradeItem;


public class Tower extends Building {
	public static final String SOUND_ID = "media/Laser_Shot.wav";
	
	private static final int initialSpeed = 14;
	private static final int initialShootDelay = 500;
	private static final int initialDamage = -35;
	
	protected final ProjectileManager projectile;
	protected final ZombieTargeter zombieTargeter;
	
	protected UpgradeItem rangeUpgrade = null;
	protected UpgradeItem fireRateUpgrade = null;
	protected UpgradeItem damageUpgrade = null;
	
	protected long lastShot = 0;
	
	@Override
	public double getVisibilityRadius() { return rangeUpgrade.getValue(); }
	
	protected int getFireRate() { return (int) fireRateUpgrade.getValue(); }
	
	protected boolean readyToFire() { return System.currentTimeMillis() >= lastShot + getFireRate(); }
	
	protected String getFireShotSoundId() { return SOUND_ID; }
	
	public Tower(double x, double y, ParticleController pc, ZombieTargeter zombieTargeter,
			ProjectileManager projectile, EntityKey key) {
		this(x, y, pc, zombieTargeter, projectile, INFO, key);
		rangeUpgrade = new UpgradeItem(new ShopItem("Range", 100),3, INFO.visibilityRadius, 1);
		if (owned()) {
			fireRateUpgrade = new UpgradeItem(new ShopItem("Fire rate", 100), 3, initialShootDelay, -50);
			damageUpgrade = new UpgradeItem(new ShopItem("Damage", 100), 3, initialDamage, -10);
			upgrades.addUpgrade(rangeUpgrade);
			upgrades.addUpgrade(fireRateUpgrade);
			upgrades.addUpgrade(damageUpgrade);
		}
	}
	
	protected Tower(double x, double y, ParticleController pc, ZombieTargeter zombieTargeter,
			ProjectileManager projectile, BuildingInfo info, EntityKey key) {
		
		super(info.spriteId, x, y, pc, info, key);
		this.projectile = projectile;
		this.zombieTargeter = zombieTargeter;
	}

	@Override
	public void update(int frameDelta) {
		if (!owned() || !zombieTargeter.canTargetZombies())
			return;
		
		if (readyToFire()) {
			Zombie target = zombieTargeter.targetZombie(getCenterX(), getCenterY());
			shootAtTarget(target);
			lastShot = System.currentTimeMillis();
		}
		super.update(frameDelta);
	}
	
	protected void shootAtTarget(Entity target) {
		if (target == null)
			return;
		
		final double targetX = target.getCenterX(), targetY = target.getCenterY();
		
		double dist = PointR.distance(getCenterX(), getCenterY(), targetX, targetY);
		if (dist < getVisibilityRadius()) {
			turn(targetX, targetY);
			shoot();
		}
	}
	
	private void shoot() {
		projectile.create(getCenterX(), getCenterY(), getAngle(), initialSpeed,
				(int) damageUpgrade.getValue(), true, getFireShotSoundId());
	}
	
	public static final BuildingInfo INFO = new BuildingInfo(
			"tower",    //spriteId
			"tower",    //Creator hash map key
			10,        //visibilityRadius
			"Need help defending yourself from waves of undead triangles?",
			new ShopItem("Long-range Zombie Destroyer", 500),
			true,   //has a healthBar
			true,   //has an UpgradeManager
			true,   //is interactive
			15,     //zombie target selection weight
			100     //max health
	);
}
