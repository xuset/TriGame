package net.xuset.triGame.game.entities.buildings.types;

import net.xuset.tSquare.game.entity.EntityKey;
import net.xuset.tSquare.game.particles.ParticleController;
import net.xuset.triGame.game.entities.buildings.Building;
import net.xuset.triGame.game.shopping.ShopItem;


public class LightTower extends Building {

	public LightTower(double x, double y, ParticleController pc, EntityKey key) {
		super(INFO.spriteId, x, y, pc, INFO, key);
	}
	
	public static final BuildingInfo INFO = new BuildingInfo(
			"media/LightTower.png",	//spriteId
			"lightTower",			//Creator hash map key
			10,					//visibilityRadius
			"Just extends the playable area so you can easily expand your empire",
			new ShopItem("Light Tower", 80),
			true,   //has a healthBar
			false,  //has an UpgradeManager
			true,   //is interactive
			2,     //zombie target selection weight
			100     //max health
	);

}
