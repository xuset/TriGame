package triGame.game.entities.buildings.types;

import tSquare.game.entity.EntityKey;
import tSquare.game.particles.ParticleController;
import triGame.game.entities.buildings.Building;

public class HeadQuarters extends Building {
	
	
	public HeadQuarters(double x, double y, ParticleController pc, EntityKey key) {
		super(INFO.spriteId, x, y, pc, INFO, key);
		health.set(1500.0);
		healthBar.relativeY = 5;
		healthBar.maxHealth = health.get().intValue();
	}

	public static final BuildingInfo INFO = new BuildingInfo(
			"hq",   //spriteId
			"hq",   //Creator hash map key
			500,    //visibilityRadius
			"The head quarters op tri",
			null,   //the ShopItem
			true,   //has a healthBar
			false,  //has an UpgradeManager
			true
	);

}
