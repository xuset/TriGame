package net.xuset.triGame.game.entities.buildings.types;

import net.xuset.tSquare.game.entity.EntityKey;
import net.xuset.tSquare.game.particles.ParticleController;
import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.triGame.game.entities.buildings.Building;


public class HeadQuarters extends Building {
	
	
	public HeadQuarters(double x, double y, ParticleController pc, EntityKey key) {
		super(INFO.spriteId, x, y, pc, INFO, key);
		healthBar.relativeY = 0.1;
	}

	public static final BuildingInfo INFO = new BuildingInfo(
			"hq",   //spriteId
			"hq",   //Creator hash map key
			10,    //visibilityRadius
			"The head quarters op tri",
			null,   //the ShopItem
			true,   //has a healthBar
			false,  //has an UpgradeManager
			true,   //is interactive
			20,     //zombie target selection weight
			1500     //max health
	);
	
	@Override
	public void draw(IGraphics g) {
		super.draw(g);
	}

}
