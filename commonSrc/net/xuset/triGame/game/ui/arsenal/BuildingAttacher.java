package net.xuset.triGame.game.ui.arsenal;


import net.xuset.tSquare.game.GameDrawable;
import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.IImage;
import net.xuset.tSquare.imaging.TsColor;
import net.xuset.tSquare.system.input.mouse.IMouseListener;
import net.xuset.tSquare.system.input.mouse.MouseAction;
import net.xuset.tSquare.system.input.mouse.TsMouseEvent;
import net.xuset.tSquare.util.Observer.Change;
import net.xuset.triGame.game.PointConverter;
import net.xuset.triGame.game.entities.LocManCreator;
import net.xuset.triGame.game.shopping.ShopItem;
import net.xuset.triGame.game.shopping.ShopManager;
import net.xuset.triGame.game.ui.UiCollisionDetector;

public class BuildingAttacher implements GameDrawable{
	private final PointConverter pointConverter;
	private final ShopManager shop;
	private final UiCollisionDetector collisionChecker;
	
	private LocManCreator<?> creator;
	private IImage image;
	private ShopItem shopItem;
	private float viewRadius;
	private boolean attached = false;
	
	private int realX = 0, realY = 0;
	private float gameX = 0, gameY = 0;
	private boolean shouldPurchase = false;
	
	
	public BuildingAttacher(IMouseListener mouse, PointConverter pointConverter,
			ShopManager shop, UiCollisionDetector collisionChecker) {
		
		this.shop = shop;
		this.pointConverter = pointConverter;
		this.collisionChecker = collisionChecker;
		
		mouse.watch(new MouseObserver());
	}
	
	public boolean isAttached() { return attached; }
	
	public void attach(ShopItem shopItem, LocManCreator<?> creator, IImage image,
			double viewRadius) {
		
		this.creator = creator;
		this.image = image;
		this.shopItem = shopItem;
		this.viewRadius = (float) viewRadius;
		attached = true;
	}
	
	public void clearAttached() {
		creator = null;
		image = null;
		attached = false;
	}
	
	public void placeAttached() {
		if (canPlace()) {
			shop.purchase(shopItem);
			creator.create(gameX, gameY);
		}
		shouldPurchase = false;
		clearAttached();
	}
	
	private boolean canPlace() {
		return shop.canPurchase(shopItem) &&
				!collisionChecker.isCollidingWith(realX, realY) &&
				creator.isValidLocation(gameX, gameY);
	}
	
	@Override
	public void draw(IGraphics g) {
		if (shouldPurchase && attached)
			placeAttached();
		
		if (!attached)
			return;

		g.drawImage(image, gameX, gameY);

		g.setColor(canPlace() ? TsColor.green : TsColor.red);
		g.drawRect(gameX, gameY, image.getWidth(g), image.getHeight(g));
		
		if (viewRadius > 0.0f) {
			g.drawOval(gameX + 0.5f - viewRadius,
					gameY + 0.5f - viewRadius,
					viewRadius * 2, viewRadius * 2);
		}
	}

	private class MouseObserver implements Change<TsMouseEvent> {
		@Override
		public void observeChange(TsMouseEvent t) {
			if (t.action == MouseAction.DRAG || t.action == MouseAction.MOVE) {
				gameX = (int) pointConverter.screenToGameX(t.x);
				gameY = (int) pointConverter.screenToGameY(t.y);
				realX = t.x;
				realY = t.y;
			}
			
			if (attached && t.action == MouseAction.RELEASE) {
				shouldPurchase = true;
			}
		}
	}
}
