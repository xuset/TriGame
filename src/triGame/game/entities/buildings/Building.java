package triGame.game.entities.buildings;

import java.awt.Color;
import java.awt.Graphics2D;

import tSquare.game.GameBoard.ViewRect;
import tSquare.game.entity.Entity;
import tSquare.game.entity.EntityKey;
import tSquare.game.particles.ParticleController;
import triGame.game.entities.HealthBar;
import triGame.game.shopping.ShopItem;
import triGame.game.shopping.UpgradeItem;
import triGame.game.shopping.UpgradeManager;

public abstract class Building extends Entity{
	public final BuildingInfo info;
	public final UpgradeManager upgrades;
	protected final HealthBar healthBar;
	
	protected int visibilityRadius = 0;
	
	public int getVisibilityRadius() { return visibilityRadius; }
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
		
		if (healthBar != null && pc != null)
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
	public void draw(Graphics2D g, ViewRect rect) {
		super.draw(g, rect);
		drawUpgradeVisual( (getCenterX() - rect.getX()),
				(getCenterY() - rect.getY()), g);
	}
	
	private static final int upgrVisDistance = 18;
	private static final int upgrVisRadius = 4;
	private void drawUpgradeVisual(double drawX, double drawY, Graphics2D g) {
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
	
	private void drawUpgrVis(int x, int y, int r, Graphics2D g) {
		final int border = 2;
		g.setColor(Color.DARK_GRAY);
		g.fillRect(x - r, y - r, r * 2, r * 2);
		g.setColor(Color.white);
		g.fillRect(x - r + border, y - r + border, 2 * (r - border), 2 * (r - border));

	}
	
	public static class BuildingInfo{
		public final String spriteId;
		public final String identifier;
		public final String description;
		public final ShopItem item;
		public final int visibilityRadius;
		public final boolean hasHealthBar;
		public final boolean isUpgradable;
		public final boolean isInteractive;
		public final int selectionWeight;
		public final int maxHealth;
		
		
		public boolean isShopable() { return item != null; }
		
		public BuildingInfo(String spriteId, String identifier, int visibilityRadius,
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
			this.selectionWeight =  10;
			this.maxHealth = maxHealth;
			
		}
	}
}
