package triGame.game.entities.buildings.types;

import tSquare.game.entity.EntityKey;
import tSquare.game.particles.ParticleController;
import triGame.game.entities.buildings.Building;
import triGame.game.shopping.ShopItem;

public class LightTower extends Building {

	public LightTower(double x, double y, ParticleController pc, EntityKey key) {
		super(INFO.spriteId, x, y, pc, INFO, key);
	}
	
	public static final BuildingInfo INFO = new BuildingInfo(
			"media/LightTower.png",	//spriteId
			"lightTower",			//Creator hash map key
			250,					//visibilityRadius
			"Just extends the playable area so you can easily expand your empire",
			new ShopItem("Light Tower", 80),
			true,   //has a healthBar
			false,    //has an UpgradeManager
			true
	);

}
