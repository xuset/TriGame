package net.xuset.triGame.game.entities.buildings.types;

import net.xuset.tSquare.game.entity.EntityKey;
import net.xuset.triGame.game.entities.buildings.Wall;


public class StrongWall extends Wall {

	public StrongWall(double x, double y, EntityKey key) {
		super(INFO.spriteId, x, y, INFO, key);
	}
	
	@Override
	public double modifyHealth(double amount) {
		return getHealth();
	}
	
	public static final WallInfo INFO = new WallInfo(
			"strongWall",	//spriteId
			"strongWall",  //Creator hash map key
			"", //no description
			null, //not shopable
			100
		);

}
