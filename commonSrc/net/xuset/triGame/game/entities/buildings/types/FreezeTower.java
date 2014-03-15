package net.xuset.triGame.game.entities.buildings.types;

import net.xuset.objectIO.netObject.NetVar;
import net.xuset.objectIO.netObject.ObjControllerI;
import net.xuset.tSquare.game.entity.EntityKey;
import net.xuset.tSquare.game.particles.Particle;
import net.xuset.tSquare.game.particles.ParticleController;
import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.TsColor;
import net.xuset.tSquare.math.point.PointR;
import net.xuset.triGame.game.ManagerService;
import net.xuset.triGame.game.entities.Person;
import net.xuset.triGame.game.entities.buildings.Building;
import net.xuset.triGame.game.entities.zombies.Zombie;
import net.xuset.triGame.game.shopping.ShopItem;
import net.xuset.triGame.game.shopping.UpgradeItem;


public class FreezeTower extends Building {
	private final ManagerService managers;
	private final UpgradeItem rangeUpgrade;
	private final UpgradeItem powerUpgrade;
	private NetVar.nDouble rangeValue;
	private NetVar.nInt powerValue;

	@Override public double getVisibilityRadius() { return rangeValue.get(); }
	
	private double getPower() { return 1.0 - powerValue.get() / 10.0; }

	public FreezeTower(double x, double y, ParticleController pc, ManagerService managers, EntityKey key) {
		super(INFO.spriteId, x, y, pc, INFO, key);
		this.managers = managers;
		
		rangeUpgrade = new UpgradeItem(new ShopItem("Range", 75),3, INFO.visibilityRadius, 0.5);
		powerUpgrade = new UpgradeItem(new ShopItem("Power", 50),3, 1, 1);
		upgrades.addUpgrade(rangeUpgrade);
		upgrades.addUpgrade(powerUpgrade);
		pc.addParticle(new RadiusParticle());
	}
	
	@Override
	protected void setNetObjects(ObjControllerI objClass) {
		super.setNetObjects(objClass);
		
		rangeValue = new NetVar.nDouble(INFO.visibilityRadius, "rangeValue", objClass);
		powerValue = new NetVar.nInt(1, "powerValue", objClass);
	}

	@Override
	public void update(int frameDelta) {
		if (owned()) {
			rangeValue.set(rangeUpgrade.getValue());
			powerValue.set((int) powerUpgrade.getValue());
			//TODO should powerValue's initialValue be set to 1? and others like it?
		}
		
		for (Zombie z : managers.zombie.list) {
			double distance = PointR.distance(z.getCenterX(), z.getCenterY(), getCenterX(), getCenterY());
			if (distance < rangeValue.get()) {
				z.freeze(getPower());
			}
		}
		
		Person p = managers.person.getPlayer();
		if (p != null && PointR.distance(p.getCenterX(), p.getCenterY(),
				getCenterX(), getCenterY()) < rangeValue.get()) {
			
			p.freeze(getPower());
		}
	}
	
	public static final BuildingInfo INFO = new BuildingInfo(
			"media/FreezeTower.png",    //spriteId
			"freezeTower",    //Creator hash map key
			1.5,        //visibilityRadius
			"Slow down the relentless waves.",
			new ShopItem("Freeze Tower", 100),
			true,    //has a healthBar
			true,    //has an UpgradeManager
			true,   //is interactive
			10,     //zombie target selection weight
			100     //max health
	);
	
	public class RadiusParticle extends Particle {
		private static final double initCircles = 4.0;
		private static final double radiusSpeed = 0.5;
		private final TsColor radiusColor = new TsColor(184, 24, 210);
		private float currentRadius = 0.01f;
		
		@Override
		public void draw(int frameDelta, IGraphics g) {
			double circles = initCircles + powerValue.get();
			currentRadius += frameDelta * radiusSpeed / 1000.0;
			float delta = (float) (rangeValue.get() / circles);
			if (currentRadius >= delta)
				currentRadius = 0.01f;

			float x = (float) (getCenterX());
			float y = (float) (getCenterY());
			g.setColor(radiusColor);
			for (int i = 0; i < circles; i++) {
				float r = currentRadius + i * delta;
				g.drawOval(x - r, y - r, 2 * r, 2 * r);
			}
		}

		@Override
		public boolean isExpired() {
			return removeRequested();
		}
		
	}
}
