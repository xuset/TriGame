package triGame.game.entities.buildings.types;


import java.awt.Graphics2D;

import tSquare.game.GameBoard.ViewRect;
import tSquare.game.entity.EntityKey;
import tSquare.game.particles.ParticleController;
import tSquare.util.PlaceHolder;
import triGame.game.ManagerService;
import triGame.game.RoundHandler;
import triGame.game.entities.PointParticle;
import triGame.game.entities.PointWell;
import triGame.game.entities.buildings.Building;
import triGame.game.shopping.ShopItem;
import triGame.game.shopping.ShopManager;

public class PointCollector extends Building {
	
	private final PointParticle.Floating particle;
	private final ShopManager shop;
	private final int pointAddFrequency;
	private final PlaceHolder<RoundHandler> phRoundHandler;
	private final PointWell pointWell;
	

	public PointCollector(double x, double y, ManagerService managers, ParticleController pc,
			ShopManager shop, PlaceHolder<RoundHandler> phRoundHandler, EntityKey key) {
		
		super(INFO.spriteId, x, y, pc, INFO, key);
		this.phRoundHandler = phRoundHandler;
		this.shop = shop;
		pointAddFrequency = 333;
		particle = new PointParticle.Floating((int) getCenterX(), (int) getCenterY(), pointAddFrequency);
		particle.height = 50;
		pointWell = managers.pointWell.getByPoint(getX(), getY());
	}
	
	private int lastFrameDelta;
	@Override
	public void performLogic(int frameDelta) {
		lastFrameDelta = frameDelta;
		super.performLogic(frameDelta);
	}
	
	@Override
	public void draw(Graphics2D g, ViewRect rect) {
		super.draw(g, rect);
		
		if (phRoundHandler.get().isRoundOnGoing() && !pointWell.isEmpty()) {
			particle.draw(lastFrameDelta, g, rect);
			if (owned() && particle.isExpired()) {
				particle.reset();
				shop.addPoints(1);
				pointWell.takePoints(1);
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
			10,     //zombie target selection weight
			100     //max health
	);

}
