package triGame.game.entities.building;

import tSquare.math.IdGenerator;
import triGame.game.shopping.ShopItem;

public class SmallTower extends Tower {
	public static final String IDENTIFIER = "samll tower";
	public static final String SPRITE_ID = "small tower";
	public static final ShopItem NEW_TOWER = new ShopItem("Small Tower", 250);
	public static final int INITIAL_RANGE = 300;
	
	private static final int initialShootDelay = 600;
	private static final int initialDamage = -20;
	
	public SmallTower(int x, int y, BuildingManager manager, long ownerId, long entityId) {
		super(SPRITE_ID, x, y, manager, ownerId, entityId, INITIAL_RANGE);
	}
	
	public static SmallTower create(int x, int y, BuildingManager manager) {
		SmallTower t = new SmallTower(x, y, manager, manager.getUserId(), IdGenerator.getNext());
		t.createOnNetwork(true);
		manager.add(t);
		t.addUpgrades();
		t.rangeUpgrade.value = INITIAL_RANGE;
		t.damageUpgrade.value = initialDamage;
		t.fireRateUpgrade.value = initialShootDelay;
		return t;
	}

	@Override
	public String getIdentifier() {
		return IDENTIFIER;
	}
}
