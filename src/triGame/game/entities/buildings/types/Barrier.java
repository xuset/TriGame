package triGame.game.entities.buildings.types;

import tSquare.game.entity.EntityKey;
import triGame.game.entities.buildings.Wall;
import triGame.game.shopping.ShopItem;


public class Barrier extends Wall {
	
	public Barrier(double x, double y, EntityKey key) {
		super(INFO.spriteId, x, y, INFO, key);
	}
	
	public static final WallInfo INFO = new WallInfo(
			"barrier",	//spriteId
			"barrier",  //Creator hash map key
			"Just a general wall to block and redirect the hoards of evil triangles",
			new ShopItem("Barrier", 10)
	);

}
