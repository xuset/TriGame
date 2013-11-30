package triGame.game.entities.buildings.types;

import java.awt.Color;
import java.awt.Graphics2D;

import tSquare.game.GameBoard.ViewRect;
import tSquare.game.entity.EntityKey;
import tSquare.game.particles.Particle;
import tSquare.game.particles.ParticleController;
import tSquare.math.Point;
import triGame.game.ManagerService;
import triGame.game.entities.Person;
import triGame.game.entities.buildings.Building;
import triGame.game.entities.zombies.Zombie;
import triGame.game.shopping.ShopItem;
import triGame.game.shopping.UpgradeItem;

public class FreezeTower extends Building {
	private final ManagerService managers;
	private final UpgradeItem rangeUpgrade;

	@Override public int getVisibilityRadius() { return rangeUpgrade.getValue(); }

	public FreezeTower(double x, double y, ParticleController pc, ManagerService managers, EntityKey key) {
		super(INFO.spriteId, x, y, pc, INFO, key);
		this.managers = managers;
		rangeUpgrade = new UpgradeItem(new ShopItem("Range", 100),3, INFO.visibilityRadius, 25);
		upgrades.addUpgrade(rangeUpgrade);
		upgrades.addUpgrade(new UpgradeItem(new ShopItem("Freeze rate", 100),3, 2500, -500));
		pc.addParticle(new RadiusParticle());
	}
	
	@Override
	public void performLogic(int frameDelta) {
		
		
		for (Zombie z : managers.zombie.list) {
			double distance = Point.distance(z.getCenterX(), z.getCenterY(), getCenterX(), getCenterY());
			if (distance < rangeUpgrade.getValue()) {
				z.freeze(0.3);
			}
		}
		
		for (Person p : managers.person.list) {
			double distance = Point.distance(p.getCenterX(), p.getCenterY(), getCenterX(), getCenterY());
			if (distance < rangeUpgrade.getValue()) {
				p.freeze(0.3);
			}
		}
	}
	
	public static final BuildingInfo INFO = new BuildingInfo(
			"media/FreezeTower.png",    //spriteId
			"freezeTower",    //Creator hash map key
			75,        //visibilityRadius
			"Slow down the hoards.",
			new ShopItem("Freeze tower", 200),
			true,    //has a healthBar
			true,    //has an UpgradeManager
			true
	);
	
	public class RadiusParticle extends Particle {
		private static final double circles = 4.0;
		private static final int radiusSpeed = 20;
		private final Color radiusColor = new Color(184, 24, 210);
		private double currentRadius = 1.0;
		
		@Override
		public void draw(int frameDelta, Graphics2D g, ViewRect rect) {
			currentRadius += frameDelta * radiusSpeed / 1000.0;
			double delta = rangeUpgrade.getValue() / circles;
			if (currentRadius >= delta)
				currentRadius = 1.0;
		
			int x = (int) (getCenterX() - rect.getX());
			int y = (int) (getCenterY() - rect.getY());
			g.setColor(radiusColor);
			for (int i = 0; i < circles; i++) {
				int r = (int) (currentRadius + i * delta);
				g.drawOval(x - r, y - r, 2 * r, 2 * r);
			}
		}

		@Override
		public boolean isExpired() {
			return removeRequested();
		}
		
	}
}
