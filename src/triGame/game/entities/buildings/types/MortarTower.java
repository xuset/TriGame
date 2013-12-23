package triGame.game.entities.buildings.types;

import java.awt.Color;
import java.awt.Graphics2D;

import tSquare.game.GameBoard.ViewRect;
import tSquare.game.entity.Entity;
import tSquare.game.entity.EntityKey;
import tSquare.game.particles.ParticleController;
import tSquare.math.Point;
import triGame.game.entities.projectiles.ProjectileManager;
import triGame.game.entities.zombies.ZombieTargeter;
import triGame.game.shopping.ShopItem;
import triGame.game.shopping.UpgradeItem;

public class MortarTower extends Tower {
	public MortarTower(double x, double y, ParticleController pc,
			ZombieTargeter targeter, ProjectileManager projectile, EntityKey key) {
		
		super(x, y, pc, targeter, projectile, INFO, key);
		fireRateUpgrade = new UpgradeItem(new ShopItem("Fire rate", 100), 3, 1500, -150);
		rangeUpgrade = new UpgradeItem(new ShopItem("Range", 100), 3, INFO.visibilityRadius, 75);
		damageUpgrade = new UpgradeItem(new ShopItem("Damage", 100), 3, -232, -15);
		accuracyUpgrade = new UpgradeItem(new ShopItem("Accuracy", 100), 3, 250, 80);
		upgrades.addUpgrade(fireRateUpgrade);
		upgrades.addUpgrade(rangeUpgrade);
		upgrades.addUpgrade(damageUpgrade);
		upgrades.addUpgrade(accuracyUpgrade);
	}
	
	@Override
	protected void shootAtTarget(Entity target) {		
		if (target == null)
			return;
		
		final double targetX = target.getCenterX(), targetY = target.getCenterY();
		final double myX = getCenterX(), myY = getCenterY();
		
		final double dist = Point.distance(myX, myY, targetX, targetY);
		if (dist < getVisibilityRadius()) {
			final double angle = Point.degrees(myX, myY, targetX, targetY);
			final int speed = accuracyUpgrade.getValue();
			final int damage = damageUpgrade.getValue();
			
			projectile.mortarCreate((int) myX, (int) myY, angle, speed, damage);
		}
	}

	private double squareRadius = 0;
	private static final int totalSquares = 2;
	private static final double maxSquareRadius = 10;
	@Override
	public void draw(Graphics2D g, ViewRect rect) {
		super.draw(g, rect);
		
		final int drawX = (int) (getCenterX() - rect.getX());
		final int drawY = (int) (getCenterY() - rect.getY());
		
		final double squareDelta = maxSquareRadius / totalSquares;
		for (int i = 0; i < totalSquares; i++) {
			int radius = (int) (squareRadius + i * squareDelta);
			g.setColor(Color.orange);
			g.drawRect(drawX - radius, drawY - radius, radius * 2  - 1, radius * 2 - 1);
		}
		
		squareRadius += 1.0 / 3.0;
		if (squareRadius > squareDelta)
			squareRadius = 0;
	}

	public static final BuildingInfo INFO = new BuildingInfo(
			"media/MortarTower.png",  //spriteId
			"mortarTower",            //Creator hash map key
			200,                      //visibilityRadius
			"'BOOM,' says the mortar tower.",
			new ShopItem("Mortar tower", 500),
			true,   //has a healthBar
			true,   //has an UpgradeManager
			true,   //is interactive
			15,     //zombie target selection weight
			200     //max health
	);
}
