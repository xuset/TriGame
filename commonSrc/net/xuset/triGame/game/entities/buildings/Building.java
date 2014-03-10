package net.xuset.triGame.game.entities.buildings;

import net.xuset.tSquare.game.entity.Entity;
import net.xuset.tSquare.game.entity.EntityKey;
import net.xuset.tSquare.game.particles.ParticleController;
import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.TsColor;
import net.xuset.triGame.game.entities.HealthBar;
import net.xuset.triGame.game.entities.buildings.types.HeadQuarters;
import net.xuset.triGame.game.shopping.ShopItem;
import net.xuset.triGame.game.shopping.UpgradeItem;
import net.xuset.triGame.game.shopping.UpgradeManager;


public abstract class Building extends Entity{
	public final BuildingInfo info;
	public final UpgradeManager upgrades;
	protected final HealthBar healthBar;
	
	protected double visibilityRadius = 0;
	
	public double getVisibilityRadius() { return visibilityRadius; }
	public boolean isUpgradable() { return upgrades != null; }
	public boolean isShopable() { return info.isShopable(); }
	public boolean isInteractive() { return info.isInteractive; }
	
	public Building(String spriteId, double x, double y,
			ParticleController pc, BuildingInfo info, EntityKey key) {
		super(spriteId, x, y, key);
		this.info = info;
		
		healthBar = (info.hasHealthBar) ? new HealthBar(this) : null;
		setupHealth(info.maxHealth);
		
		upgrades = (info.isUpgradable) ? new UpgradeManager() : null;
		
		if (healthBar != null && pc != null && (owned() || info == HeadQuarters.INFO))
			pc.addParticle(healthBar);

		visibilityRadius = info.visibilityRadius;
	}
	
	private void setupHealth(int maxHealth) {
		if (owned()) {
			double toAdd = maxHealth - getHealth();
			modifyHealth(toAdd);
		}
		if (healthBar != null) {
			healthBar.maxHealth = maxHealth;
		}
		
	}
	
	@Override
	public double modifyHealth(double delta) {
		double health = super.modifyHealth(delta);
		if (health <= 0)
			remove();
		return health;
	}
	
	@Override
	public void draw(IGraphics g) {
		super.draw(g);
		drawUpgradeVisual(getCenterX(), getCenterY(), g);
	}
	
	private static final float upgrVisDistance = 18/50.0f;
	private static final float upgrVisRadius = 4/50.0f;
	private void drawUpgradeVisual(double drawX, double drawY, IGraphics g) {
		if (upgrades == null)
			return;
		
		final int totalItems = upgrades.items.size();
		final double angleDelta = 2.0 * Math.PI / totalItems;
		int count = 0;
		for (UpgradeItem item : upgrades.items) {
			if (item.getUpgradeCount() == item.maxUpgrades) {
				double rads = angleDelta * count - getAngle() + Math.PI;
				float itemX = (float) (drawX + Math.cos(rads) * upgrVisDistance);
				float itemY = (float) (drawY + Math.sin(rads) * upgrVisDistance);
				drawUpgrVis(itemX, itemY, upgrVisRadius, g);
			}
			count++;
		}
	}
	
	private void drawUpgrVis(float x, float y, float r, IGraphics g) {
		final float border = 2/50.0f;
		g.setColor(TsColor.darkGray);
		g.fillRect(x - r, y - r, r * 2, r * 2);
		g.setColor(TsColor.white);
		g.fillRect(x - r + border, y - r + border, 2 * (r - border), 2 * (r - border));

	}
	
	public static class BuildingInfo{
		public final String spriteId;
		public final String identifier;
		public final String description;
		public final ShopItem item;
		public final double visibilityRadius;
		public final boolean hasHealthBar;
		public final boolean isUpgradable;
		public final boolean isInteractive;
		public final int selectionWeight;
		public final int maxHealth;
		
		
		public boolean isShopable() { return item != null; }
		
		public BuildingInfo(String spriteId, String identifier, double visibilityRadius,
				String description, ShopItem item,
				boolean hasHealthBar, boolean isUpgradable, boolean isInteractive,
				int selectionWeight, int maxHealth) {
			
			this.spriteId = spriteId;
			this.identifier = identifier;
			this.visibilityRadius = visibilityRadius;
			this.description = description;
			this.item = item;
			this.hasHealthBar = hasHealthBar;
			this.isUpgradable = isUpgradable;
			this.isInteractive = isInteractive;
			this.selectionWeight =  selectionWeight;
			this.maxHealth = maxHealth;
			
		}
	}
}
