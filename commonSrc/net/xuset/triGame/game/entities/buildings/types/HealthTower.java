package net.xuset.triGame.game.entities.buildings.types;

import net.xuset.objectIO.connections.Connection;
import net.xuset.objectIO.netObject.NetVar;
import net.xuset.objectIO.netObject.ObjControllerI;
import net.xuset.tSquare.game.entity.EntityKey;
import net.xuset.tSquare.game.particles.Particle;
import net.xuset.tSquare.game.particles.ParticleController;
import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.TsColor;
import net.xuset.tSquare.math.point.PointR;
import net.xuset.triGame.game.ManagerService;
import net.xuset.triGame.game.entities.buildings.Building;
import net.xuset.triGame.game.shopping.ShopItem;
import net.xuset.triGame.game.shopping.UpgradeItem;


public class HealthTower extends Building {
	private static final float elapsedDrawTime = 1000;
	private static final double regenerateScale = 1.0 / 15.0;
	private static final double spinSpeed = 15;

	private final ManagerService managers;
	private final UpgradeItem rateUpgrade;
	private final UpgradeItem rangeUpgrade;
	private final UpgradeItem powerUpgrade;
	private NetVar.nDouble rangeValue;
	private NetVar.nBool drawRegeneration;
	
	private long lastRegeneration = 0;
	private long drawTimeStarted = 0;
	
	@Override
	public double getVisibilityRadius() { return rangeUpgrade.getValue(); }
	
	public HealthTower(double x, double y, ParticleController pc,
			ManagerService managers, EntityKey key) {
		
		super(INFO.spriteId, x, y, pc, INFO, key);
		this.managers = managers;
		
		rateUpgrade = new UpgradeItem(new ShopItem("Regeneration rate", 100), 3, 12000, -2000);
		rangeUpgrade = new UpgradeItem(new ShopItem("Range", 100), 3, INFO.visibilityRadius, 1);
		powerUpgrade = new UpgradeItem(new ShopItem("Healing power", 100), 3, 1, 1);
		upgrades.addUpgrade(rateUpgrade);
		upgrades.addUpgrade(rangeUpgrade);
		upgrades.addUpgrade(powerUpgrade);
		
		pc.addParticle(new HealthParticle());
	}
	
	@Override
	protected void setNetObjects(ObjControllerI objClass) {
		rangeValue = new NetVar.nDouble(INFO.visibilityRadius, "range", objClass);
		drawRegeneration = new NetVar.nBool(false, "drawRegeneration", objClass);
		drawRegeneration.setEvent(true, new OnRegenerateChange());
	}

	@Override
	public void update(int frameDelta) {
		super.update(frameDelta);
		
		if (!owned())
			return;
		
		rangeValue.set(rangeUpgrade.getValue());
		
		final long nextRegeneration = lastRegeneration + ((int) rateUpgrade.getValue());
		final double centerX = getCenterX();
		final double centerY = getCenterY();
		final double range = rangeUpgrade.getValue();
		if (System.currentTimeMillis() > nextRegeneration) {
			for (Building b : managers.building.interactives) {
				double dist = PointR.distance(centerX, centerY, b.getCenterX(), b.getCenterY());
				if (dist <= range)
					increaseBuildingHealth(b);
			}
			lastRegeneration = System.currentTimeMillis();
		}
		
		if (System.currentTimeMillis() > nextRegeneration - (int) elapsedDrawTime) {
			if (!drawRegeneration.get()) {
				drawRegeneration.set(true);
			}
		} else
			drawRegeneration.set(false);
	}
	
	@Override
	public void draw(IGraphics g) {
		super.draw(g);

		if (drawRegeneration.get())
			setAngle(getAngle() + spinSpeed);
		else
			setAngle(Math.PI / 2);
	}
	
	private void increaseBuildingHealth(Building b) {
		double deltaHealth = b.info.maxHealth - b.getHealth();
		double gains = deltaHealth * powerUpgrade.getValue() * regenerateScale;
		b.modifyHealth(gains);
	}
	
	private final class OnRegenerateChange implements NetVar.OnChange<Boolean> {
		@Override
		public void onChange(NetVar<Boolean> var, Connection c) {
			drawTimeStarted = var.get() ? System.currentTimeMillis() : 0;
		}
	}
	
	private class HealthParticle extends Particle {

		@Override
		public void draw(int delta, IGraphics g) {
			if (!drawRegeneration.get())
				return;
				
			final float ratio = (System.currentTimeMillis() - drawTimeStarted) / elapsedDrawTime;
			final float radius = (float) (rangeValue.get() * ratio);
			final float x = (float) (getCenterX() - radius);
			final float y = (float) (getCenterY() - radius);
			
			g.setColor(TsColor.green);
			g.drawOval(x, y, radius * 2, radius * 2);
		}

		@Override
		public boolean isExpired() {
			return HealthTower.this.removeRequested();
		}
		
	}
	
	public static final BuildingInfo INFO = new BuildingInfo(
			"media/HealthTower.png",    //spriteId
			"healthTower",    //Creator hash map key
			2.51,        //visibilityRadius
			"Regenerate any tower's health within it's range",
			new ShopItem("Regenerator", 400),
			true,   //has a healthBar
			true,   //has an UpgradeManager
			true,   //is interactive
			10,     //zombie target selection weight
			100     //max health
	);
}
