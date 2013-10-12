package triGame.game.entities.building;

import tSquare.math.IdGenerator;
import triGame.game.shopping.ShopItem;

public class LightTower extends Building {
	public static final String SPRITE_ID = "media/LightTower.png";
	public static final String IDENTIFIER = "lightTower";
	public static final int VISIBILITY_RADIUS = 250;
	public static final ShopItem NEW_LIGHT_TOWER = new ShopItem("Light Tower", 80);

	public LightTower(int x, int y, BuildingManager manager, long ownerId, long entityId) {
		super(SPRITE_ID, x, y, manager, ownerId, entityId, VISIBILITY_RADIUS);
	}
	
	public static LightTower create(int x, int y, BuildingManager manager) {
		LightTower l = new LightTower(x, y, manager, manager.getUserId(), IdGenerator.getInstance().getId());
		l.createOnNetwork(true);
		manager.add(l);
		return l;
	}

	@Override
	public String getIdentifier() {
		return IDENTIFIER;
	}

}
