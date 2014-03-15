package net.xuset.triGame.game.entities.buildings.types;

import net.xuset.tSquare.game.entity.EntityKey;
import net.xuset.triGame.game.entities.buildings.Wall;
import net.xuset.triGame.game.shopping.ShopItem;


public class SteelBarrier extends Wall {

	public SteelBarrier(double x, double y, EntityKey key) {
		super(INFO.spriteId, x, y, INFO, key);
	}
	
	public static final WallInfo INFO = new WallInfo(
			"media/SteelBarrier.png",  //spriteId
			"steel",                   //Creator hash map key
			"Forged from steel, unlike Barriers.",
			new ShopItem("Steel Barrier", 20),
			200 //max health
		);

}
