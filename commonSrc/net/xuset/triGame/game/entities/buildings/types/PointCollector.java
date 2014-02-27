package net.xuset.triGame.game.entities.buildings.types;

import net.xuset.tSquare.game.entity.EntityKey;
import net.xuset.tSquare.game.particles.ParticleController;
import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.triGame.game.GameMode;
import net.xuset.triGame.game.ManagerService;
import net.xuset.triGame.game.entities.PointParticle;
import net.xuset.triGame.game.entities.PointWell;
import net.xuset.triGame.game.entities.buildings.Building;
import net.xuset.triGame.game.shopping.ShopItem;
import net.xuset.triGame.game.shopping.ShopManager;


public class PointCollector extends Building {
	
	private final PointParticle.Floating particle;
	private final ShopManager shop;
	private final int pointAddFrequency;
	private final GameMode gameMode;
	private final PointWell pointWell;
	

	public PointCollector(double x, double y, ManagerService managers, ParticleController pc,
			ShopManager shop, GameMode gameMode, EntityKey key) {
		
		super(INFO.spriteId, x, y, pc, INFO, key);
		this.gameMode = gameMode;
		this.shop = shop;
		pointAddFrequency = 333;
		particle = new PointParticle.Floating(getCenterX(), getCenterY(), pointAddFrequency);
		pointWell = managers.pointWell.getByPoint(getX(), getY());
	}
	
	private int lastFrameDelta;
	@Override
	public void update(int frameDelta) {
		lastFrameDelta = frameDelta;
		super.update(frameDelta);
	}
	
	@Override
	public void draw(IGraphics g) {
		super.draw(g);
		
		if (gameMode.isRoundGoing() && !pointWell.isEmpty()) {
			particle.draw(lastFrameDelta, g);
			if (owned() && particle.isExpired() && pointWell.takePoints(1)) {
				particle.reset();
				shop.addPoints(1);
			}
		}
	}
	
	public static final BuildingInfo INFO = new BuildingInfo(
			"media/PointCollector.png",	//spriteId
			"pointCollector",			//Creator hash map key
			300,						//visibilityRadius
			"Want to collect more points? Than place this over a Point Well",
			new ShopItem("Point collector", 300),
			true,   //has a healthBar
			true,   //has an UpgradeManager
			true,   //is interactive
			1,     //zombie target selection weight
			100     //max health
	);

}
