package triGame.game.entities.buildings.types;

import tSquare.game.entity.EntityKey;
import triGame.game.entities.buildings.Wall;
import triGame.game.shopping.ShopItem;

public class SteelBarrier extends Wall {

	public SteelBarrier(double x, double y, EntityKey key) {
		super(INFO.spriteId, x, y, INFO, key);
	}
	
	public static final WallInfo INFO = new WallInfo(
			"media/SteelBarrier.png",  //spriteId
			"steel",                   //Creator hash map key
			"Forged from steel, unlike Barriers.",
			new ShopItem("Steel", 20),
			200 //max health
		);

}
