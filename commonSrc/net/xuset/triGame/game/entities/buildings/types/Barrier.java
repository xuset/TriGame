package net.xuset.triGame.game.entities.buildings.types;

import net.xuset.tSquare.game.entity.EntityKey;
import net.xuset.triGame.game.entities.buildings.Wall;
import net.xuset.triGame.game.shopping.ShopItem;



public class Barrier extends Wall {
	
	public Barrier(double x, double y, EntityKey key) {
		super(INFO.spriteId, x, y, INFO, key);
	}
	
	public static final WallInfo INFO = new WallInfo(
			"barrier",	//spriteId
			"barrier",  //Creator hash map key
			"Just a general wall to block and redirect the hoards of evil triangles",
			new ShopItem("Barrier", 10),
			100 //max health
	);

}
