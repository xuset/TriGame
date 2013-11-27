package triGame.game.entities.buildings;

import java.awt.Graphics2D;

import tSquare.game.GameBoard.ViewRect;
import tSquare.game.entity.Entity;
import tSquare.game.entity.EntityKey;
import triGame.game.entities.HealthBar;
import triGame.game.shopping.ShopItem;
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
	
	public Building(String spriteId, double x, double y, BuildingInfo info, EntityKey key) {
		super(spriteId, x, y, key);
		this.info = info;
		healthBar = (info.hasHealthBar) ? new HealthBar(this) : null;
		upgrades = (info.isUpgradable) ? new UpgradeManager() : null;
		

		visibilityRadius = info.visibilityRadius;
	}
	
	@Override
	public void performLogic(int frameDelta) {
		if (getHealth() <= 0)
			remove();
	}
	
	@Override
	public void draw(Graphics2D g, ViewRect rect) {
		super.draw(g, rect);
		if (healthBar != null)
			healthBar.draw(g, rect);
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
		public final int selectionWeight = 10;
		
		public boolean isShopable() { return item != null; }
		
		public BuildingInfo(String spriteId, String identifier, int visibilityRadius,
				String description, ShopItem item,
				boolean hasHealthBar, boolean isUpgradable, boolean isInteractive) {
			this.spriteId = spriteId;
			this.identifier = identifier;
			this.visibilityRadius = visibilityRadius;
			this.description = description;
			this.item = item;
			this.hasHealthBar = hasHealthBar;
			this.isUpgradable = isUpgradable;
			this.isInteractive = isInteractive;
			
		}
	}
}
