package triGame.game.entities.building;

import triGame.game.shopping.ShopItem;

public class HeadQuarters extends Building {
	public static final String IDENTIFIER = "hq";
	public static final String SPRITE_ID = "hq";
	public static final ShopItem NEW_HQ = new ShopItem("HQ", 1000);
	
	private static final int spriteLength = 100;
	private static final int visibilityRadius = 500;
	
	public HeadQuarters(int x, int y, BuildingManager manager, long ownerId, long entityId) {
		super(SPRITE_ID, x, y, manager, ownerId, entityId, visibilityRadius);
		health = 1500;
		healthBar.relativeY = 5;
		healthBar.maxHealth = (int) health;
	}
	
	public static HeadQuarters create(int x, int y, BuildingManager manager) {
		if (manager.objectGrid.isRectangleOpen(x, y, spriteLength, spriteLength)) {
			HeadQuarters hq = new HeadQuarters(x, y, manager, manager.getUserId(), manager.getUniqueId());
			hq.createOnNetwork(true);
			manager.add(hq);
			return hq;
		}
		return null;
	}
	
	public static HeadQuarters createFromString(String parameters, BuildingManager manager, long ownerId, long entityId) {
		String[] split = parameters.split(":", 2);
		int x = Integer.parseInt(split[0]);
		int y = Integer.parseInt(split[1]);
		HeadQuarters hq = new HeadQuarters(x, y, manager, ownerId, entityId);
		return hq;
	}
	
	public void performLogic() {
		varContainer.update();
	}

	public String getIdentifier() {
		return IDENTIFIER;
	}
	
	public String createToString() {
		return createToStringHeader() + ((int) x) + ":" + ((int) y);
	}
	
	public void remove() {
		super.remove();
		manager.setGameOver();
	}

}
