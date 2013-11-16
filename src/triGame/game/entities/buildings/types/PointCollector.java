package triGame.game.entities.buildings.types;


import java.awt.Graphics2D;

import tSquare.game.GameBoard.ViewRect;
import tSquare.game.entity.EntityKey;
import tSquare.util.PlaceHolder;
import triGame.game.RoundHandler;
import triGame.game.entities.PointParticle;
import triGame.game.entities.buildings.Building;
import triGame.game.shopping.ShopItem;
import triGame.game.shopping.ShopManager;

public class PointCollector extends Building {
	
	private final PointParticle.Floating particle;
	private final ShopManager shop;
	private final int pointAddFrequency;
	private final PlaceHolder<RoundHandler> phRoundHandler;
	

	public PointCollector(double x, double y, ShopManager shop,
			PlaceHolder<RoundHandler> phRoundHandler, EntityKey key) {
		
		super(INFO.spriteId, x, y, INFO, key);
		this.phRoundHandler = phRoundHandler;
		this.shop = shop;
		pointAddFrequency = (int) (Math.random() * 1000 + 2000);
		particle = new PointParticle.Floating((int) getCenterX(), (int) getCenterY(), pointAddFrequency);
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
		if (phRoundHandler.get().isRoundOnGoing()) {
			particle.draw(lastFrameDelta, g, rect);
			if (owned())
				shop.points += 1;
			if (particle.isExpired())
				particle.reset();
		}
	}
	
	public static final BuildingInfo INFO = new BuildingInfo(
			"media/PointCollector.png",	//spriteId
			"pointCollector",			//Creator hash map key
			300,						//visibilityRadius
			"Want to collect more points? Than place this over a Point Well",
			new ShopItem("Point collector", 300),
			true,   //has a healthBar
			true,    //has an UpgradeManager
			true
	);

}
