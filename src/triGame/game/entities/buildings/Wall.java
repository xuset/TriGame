package triGame.game.entities.buildings;

import tSquare.game.entity.EntityKey;
import triGame.game.shopping.ShopItem;

public abstract class Wall extends Building {

	public Wall(String spriteId, double x, double y, BuildingInfo info,
			EntityKey key) {
		super(spriteId, x, y, info, key);
	}
	
	public static class WallInfo extends BuildingInfo {
		
		public WallInfo(String spriteId, String identifier,
				String description, ShopItem item) {
			
			super(spriteId, identifier, 0, description, item, false, false, false);
		}
	}

}
