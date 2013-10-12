package triGame.game.entities.building;


import tSquare.math.IdGenerator;
import triGame.game.TriGame;
import triGame.game.shopping.ShopItem;

public class PointCollector extends Building {
	public static final ShopItem NEW_POINT_COLLECTOR = new ShopItem("Point collector", 300);
	public static final String IDENTIFIER = "pointCollector";
	public static final String SPRITE_ID = "media/PointCollector.png";
	public static final int VISIBILITY_RADIUS = 300;
	
	private long lastPointAdd;
	private int pointAddFrequency;

	public PointCollector(int x, int y, BuildingManager manager, long ownerId, long entityId) {
		super(SPRITE_ID, x, y, manager, ownerId, entityId, VISIBILITY_RADIUS);
		pointAddFrequency = (int) (Math.random() * 800 + 1000);
		lastPointAdd = System.currentTimeMillis();
	}
	
	public static PointCollector create(int x, int y, BuildingManager manager) {
		PointCollector p = new PointCollector(x, y, manager, manager.getUserId(), IdGenerator.getNext());
		p.createOnNetwork(true);
		manager.add(p);
		return p;
	}
	
	public void performLogic() {
		TriGame g = manager.getGameInstance();
		if (g.roundHandler.isRoundOnGoing() && lastPointAdd + pointAddFrequency < System.currentTimeMillis()) {
			g.shop.points += 1;
			lastPointAdd = System.currentTimeMillis();
		}
	}

	@Override
	public String getIdentifier() {
		return IDENTIFIER;
	}

}
