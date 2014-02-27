package net.xuset.triGame.game.entities.buildings;

import net.xuset.tSquare.game.entity.Entity;
import net.xuset.tSquare.game.entity.EntityKey;
import net.xuset.tSquare.game.particles.ParticleController;
import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.TsColor;
import net.xuset.tSquare.math.rect.IRectangleR;
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
		IRectangleR rect = g.getView();
		drawUpgradeVisual( (getCenterX() - rect.getX()),
				(getCenterY() - rect.getY()), g);
	}
	
	private static final int upgrVisDistance = 18;
	private static final int upgrVisRadius = 4;
	private void drawUpgradeVisual(double drawX, double drawY, IGraphics g) {
		if (upgrades == null)
			return;
		
		final int totalItems = upgrades.items.size();
		final double angleDelta = 360.0 / totalItems;
		int count = 0;
		for (UpgradeItem item : upgrades.items) {
			if (item.getUpgradeCount() == item.maxUpgrades) {
				final double rads = Math.toRadians(angleDelta * count - getAngle() + 180);
				final int itemX = (int) (drawX + Math.cos(rads) * upgrVisDistance);
				final int itemY = (int) (drawY + Math.sin(rads) * upgrVisDistance);
				drawUpgrVis(itemX, itemY, upgrVisRadius, g);
			}
			count++;
		}
	}
	
	private void drawUpgrVis(int x, int y, int r, IGraphics g) {
		final int border = 2;
		g.setColor(TsColor.darkGray);
		g.drawRect(x - r, y - r, r * 2, r * 2);
		g.setColor(TsColor.white);
		g.drawRect(x - r + border, y - r + border, 2 * (r - border), 2 * (r - border));

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
