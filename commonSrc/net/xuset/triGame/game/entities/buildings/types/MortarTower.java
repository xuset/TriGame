package net.xuset.triGame.game.entities.buildings.types;

import net.xuset.tSquare.game.entity.Entity;
import net.xuset.tSquare.game.entity.EntityKey;
import net.xuset.tSquare.game.particles.ParticleController;
import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.TsColor;
import net.xuset.tSquare.math.point.PointR;
import net.xuset.triGame.game.entities.projectiles.ProjectileManager;
import net.xuset.triGame.game.entities.zombies.ZombieTargeter;
import net.xuset.triGame.game.shopping.ShopItem;
import net.xuset.triGame.game.shopping.UpgradeItem;


public class MortarTower extends Tower {
	private static final double initSplashRadius = 1.0;
	
	protected final UpgradeItem splashUpgrade;
	
	public MortarTower(double x, double y, ParticleController pc,
			ZombieTargeter targeter, ProjectileManager projectile, EntityKey key) {
		
		super(x, y, pc, targeter, projectile, INFO, key);
		fireRateUpgrade = new UpgradeItem(new ShopItem("Fire rate", 100), 3, 1500, -150);
		rangeUpgrade = new UpgradeItem(new ShopItem("Range", 100), 3, INFO.visibilityRadius, 1.5);
		damageUpgrade = new UpgradeItem(new ShopItem("Damage", 100), 3, -70, -15);
		splashUpgrade = new UpgradeItem(new ShopItem("Splash radius", 100), 3, 1.0, 0.20);
		upgrades.addUpgrade(fireRateUpgrade);
		upgrades.addUpgrade(rangeUpgrade);
		upgrades.addUpgrade(damageUpgrade);
		upgrades.addUpgrade(splashUpgrade);
	}
	
	@Override
	protected void shootAtTarget(Entity target) {		
		if (target == null)
			return;
		
		final double targetX = target.getCenterX(), targetY = target.getCenterY();
		final double myX = getCenterX(), myY = getCenterY();
		
		final double dist = PointR.distance(myX, myY, targetX, targetY);
		if (dist < getVisibilityRadius()) {
			final double angle = PointR.angle(myX, myY, targetX, targetY);
			final double speed = 5.0;
			final int damage = (int) damageUpgrade.getValue();
			//final double splashRadius = splashUpgrade.getValue();
			
			projectile.mortarCreate(myX, myY, angle, speed, damage, initSplashRadius,
					getVisibilityRadius(), true);
		}
	}

	private double squareRadius = 0;
	private static final int totalSquares = 2;
	private static final double maxSquareRadius = 1.0/5.0;
	@Override
	public void draw(IGraphics g) {
		super.draw(g);
		
		final float drawX = (float) (getCenterX());
		final float drawY = (float) (getCenterY());
		
		final double squareDelta = maxSquareRadius / totalSquares;
		for (int i = 0; i < totalSquares; i++) {
			float radius = (float) (squareRadius + i * squareDelta);
			g.setColor(TsColor.orange);
			g.drawRect(drawX - radius, drawY - radius, radius * 2, radius * 2);
			//TODO only need rectangle outline not fill ^
		}
		
		squareRadius += 1.0 / 150.0;
		if (squareRadius > squareDelta)
			squareRadius = 0;
	}

	public static final BuildingInfo INFO = new BuildingInfo(
			"media/MortarTower.png",  //spriteId
			"mortarTower",            //Creator hash map key
			4,                      //visibilityRadius
			"'BOOM,' says the mortar tower.",
			new ShopItem("Mortar Tower", 500),
			true,   //has a healthBar
			true,   //has an UpgradeManager
			true,   //is interactive
			15,     //zombie target selection weight
			200     //max health
	);
}
