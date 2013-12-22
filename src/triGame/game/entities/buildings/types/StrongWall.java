package triGame.game.entities.buildings.types;

import tSquare.game.entity.EntityKey;
import triGame.game.entities.buildings.Wall;

public class StrongWall extends Wall {

	public StrongWall(double x, double y, EntityKey key) {
		super(INFO.spriteId, x, y, INFO, key);
		// TODO Auto-generated constructor stub
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
