package triGame.game.entities.buildings.types;

import java.awt.Color;
import java.awt.Graphics2D;

import tSquare.game.GameBoard.ViewRect;
import tSquare.game.entity.EntityKey;
import tSquare.game.particles.ParticleController;
import tSquare.math.Point;
import triGame.game.ManagerService;
import triGame.game.entities.buildings.Building;
import triGame.game.shopping.ShopItem;
import triGame.game.shopping.UpgradeItem;

public class HealthTower extends Building {
	private static final double elapsedDrawTime = 2000;
	private static final double regenerateScale = 1.0 / 10.0;
	private static final double spinSpeed = 15;

	private final ManagerService managers;
	private final UpgradeItem rateUpgrade;
	private final UpgradeItem rangeUpgrade;
	private final UpgradeItem powerUpgrade;
	
	private long lastRegeneration = 0;
	private long drawTimeStarted = 0;
	
	@Override
	public int getVisibilityRadius() { return rangeUpgrade.getValue(); }
	
	public HealthTower(double x, double y, ParticleController pc,
			ManagerService managers, EntityKey key) {
		
		super(INFO.spriteId, x, y, pc, INFO, key);
		this.managers = managers;
		rateUpgrade = new UpgradeItem(new ShopItem("Regeneration rate", 200), 3, 4000, -2000);
		rangeUpgrade = new UpgradeItem(new ShopItem("Range", 200), 3, INFO.visibilityRadius, 50);
		powerUpgrade = new UpgradeItem(new ShopItem("Healing power", 200), 3, 1, 1);
	}

	@Override
	public void performLogic(int frameDelta) {
		super.performLogic(frameDelta);
		
		if (!owned())
			return;
		
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
	}
	
	@Override
	public void draw(Graphics2D g, ViewRect rect) {
		super.draw(g, rect);

		final long nextRegeneration = lastRegeneration + rateUpgrade.getValue();
		if (System.currentTimeMillis() > nextRegeneration - elapsedDrawTime) {
			if(drawTimeStarted == 0)
				drawTimeStarted = System.currentTimeMillis();
			
			final double ratio = (System.currentTimeMillis() - drawTimeStarted) / elapsedDrawTime;
			final int radius = (int) (rangeUpgrade.getValue() * ratio);
			final int x = (int) (getCenterX() - rect.getX() - radius);
			final int y = (int) (getCenterY() - rect.getY() - radius);
			
			g.setColor(Color.cyan);			
			g.drawOval(x, y, radius * 2, radius * 2);
			
			setAngle(getAngle() + spinSpeed);
		} else {
			drawTimeStarted = 0;
		}
	}
	
	private void increaseBuildingHealth(Building b) {
		double deltaHealth = b.info.maxHealth - b.getHealth();
		double gains = deltaHealth * powerUpgrade.getValue() * regenerateScale;
		b.modifyHealth(gains);
	}
	
	
	public static final BuildingInfo INFO = new BuildingInfo(
			"media/HealthTower.png",    //spriteId
			"healthTower",    //Creator hash map key
			200,        //visibilityRadius
			"Need help defending yourself from waves of undead triangles?",
			new ShopItem("Regenerator", 700),
			true,   //has a healthBar
			false,   //has an UpgradeManager
			true,   //is interactive
			10,     //zombie target selection weight
			100     //max health
	);
}
