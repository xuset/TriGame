package triGame.game.entities.building;

import tSquare.math.IdGenerator;
import triGame.game.shopping.ShopItem;

public class HeadQuarters extends Building {
	public static final String IDENTIFIER = "hq";
	public static final String SPRITE_ID = "hq";
	public static final ShopItem NEW_HQ = new ShopItem("HQ", 1000);
	public static final int INITIAL_RADIUS = 500;
	
	private static final int spriteLength = 100;
	
	public HeadQuarters(int x, int y, BuildingManager manager, long ownerId, long entityId) {
		super(SPRITE_ID, x, y, manager, ownerId, entityId, INITIAL_RADIUS);
		health.set(1500.0);
		healthBar.relativeY = 5;
		healthBar.maxHealth = health.get().intValue();
	}
	
	public static HeadQuarters create(int x, int y, BuildingManager manager) {
		if (manager.objectGrid.isRectangleOpen(x, y, spriteLength, spriteLength)) {
			HeadQuarters hq = new HeadQuarters(x, y, manager, manager.getUserId(), IdGenerator.getInstance().getId());
			hq.createOnNetwork(true);
			manager.add(hq);
			return hq;
		}
		return null;
	}

	@Override
	public void performLogic() {
	}

	@Override
	public String getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	public void remove() {
		super.remove();
		manager.setGameOver();
	}

}
