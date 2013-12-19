package triGame.game.entities.buildings.types;

import java.awt.Color;
import java.awt.Graphics2D;

import objectIO.connections.Connection;
import objectIO.netObject.NetVar;
import tSquare.game.GameBoard.ViewRect;
import tSquare.game.entity.EntityKey;
import tSquare.game.particles.Particle;
import tSquare.game.particles.ParticleController;
import tSquare.math.Point;
import triGame.game.ManagerService;
import triGame.game.entities.buildings.Building;
import triGame.game.shopping.ShopItem;
import triGame.game.shopping.UpgradeItem;

public class HealthTower extends Building {
	private static final double elapsedDrawTime = 1000;
	private static final double regenerateScale = 1.0 / 15.0;
	private static final double spinSpeed = 15;

	private final ManagerService managers;
	private final UpgradeItem rateUpgrade;
	private final UpgradeItem rangeUpgrade;
	private final UpgradeItem powerUpgrade;
	private final NetVar.nInt rangeValue;
	private final NetVar.nBool drawRegeneration;
	
	private long lastRegeneration = 0;
	private long drawTimeStarted = 0;
	
	@Override
	public int getVisibilityRadius() { return rangeUpgrade.getValue(); }
	
	public HealthTower(double x, double y, ParticleController pc,
			ManagerService managers, EntityKey key) {
		
		super(INFO.spriteId, x, y, pc, INFO, key);
		this.managers = managers;
		
		rateUpgrade = new UpgradeItem(new ShopItem("Regeneration rate", 200), 3, 12000, -2000);
		rangeUpgrade = new UpgradeItem(new ShopItem("Range", 200), 3, INFO.visibilityRadius, 25);
		powerUpgrade = new UpgradeItem(new ShopItem("Healing power", 200), 3, 1, 1);
		upgrades.addUpgrade(rateUpgrade);
		upgrades.addUpgrade(rangeUpgrade);
		upgrades.addUpgrade(powerUpgrade);
		
		rangeValue = new NetVar.nInt(rangeUpgrade.getValue(), "range", objClass);
		drawRegeneration = new NetVar.nBool(false, "drawRegeneration", objClass);
		drawRegeneration.event = new OnRegenerateChange();
		
		pc.addParticle(new HealthParticle());
	}

	@Override
	public void performLogic(int frameDelta) {
		super.performLogic(frameDelta);
		
		if (!owned())
			return;
		
		rangeValue.set(rangeUpgrade.getValue());
		
		final long nextRegeneration = lastRegeneration + rateUpgrade.getValue();
		final double centerX = getCenterX();
		final double centerY = getCenterY();
		final int range = rangeUpgrade.getValue();
		if (System.currentTimeMillis() > nextRegeneration) {
			for (Building b : managers.building.interactives) {
				double dist = Point.distance(centerX, centerY, b.getCenterX(), b.getCenterY());
				if (dist <= range)
					increaseBuildingHealth(b);
			}
			lastRegeneration = System.currentTimeMillis();
		}
		
		if (System.currentTimeMillis() > nextRegeneration - elapsedDrawTime) {
			if (!drawRegeneration.get()) {
				drawRegeneration.set(true);
				drawRegeneration.event.onChange(drawRegeneration, null);
			}
		} else
			drawRegeneration.set(false);
	}
	
	@Override
	public void draw(Graphics2D g, ViewRect rect) {
		super.draw(g, rect);

		if (drawRegeneration.get())
			setAngle(getAngle() + spinSpeed);
		else
			setAngle(0.0);
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
		public void draw(int delta, Graphics2D g, ViewRect rect) {
			if (!drawRegeneration.get())
				return;
				
			final double ratio = (System.currentTimeMillis() - drawTimeStarted) / elapsedDrawTime;
			final int radius = (int) (rangeValue.get() * ratio);
			final int x = (int) (getCenterX() - rect.getX() - radius);
			final int y = (int) (getCenterY() - rect.getY() - radius);
			
			g.setColor(Color.green);
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
			76,        //visibilityRadius
			"Regenerate a tower's health within it's range",
			new ShopItem("Regenerator", 700),
			true,   //has a healthBar
			true,   //has an UpgradeManager
			true,   //is interactive
			10,     //zombie target selection weight
			100     //max health
	);
}
