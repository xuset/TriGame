package net.xuset.triGame.game.ui.arsenal;


import net.xuset.tSquare.game.GameDrawable;
import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.IImage;
import net.xuset.tSquare.imaging.TsColor;
import net.xuset.tSquare.system.input.mouse.IMouseListener;
import net.xuset.tSquare.system.input.mouse.MousePointer;
import net.xuset.triGame.game.PointConverter;
import net.xuset.triGame.game.entities.LocManCreator;
import net.xuset.triGame.game.shopping.ShopItem;
import net.xuset.triGame.game.shopping.ShopManager;
import net.xuset.triGame.game.ui.UiCollisionDetector;

public class BuildingAttacher implements GameDrawable{
	private final PointConverter pointConverter;
	private final ShopManager shop;
	private final UiCollisionDetector collisionChecker;
	private final IMouseListener mouseListener;
	
	private LocManCreator<?> creator;
	private IImage image;
	private ShopItem shopItem;
	private float viewRadius;
	private boolean attached = false;
	private MousePointer mousePointer = null;
	private boolean stayAttached = false;
	private boolean beenReleased = false;
	
	private int realX = 0, realY = 0;
	private float gameX = 0, gameY = 0;
	
	
	public BuildingAttacher(PointConverter pointConverter, ShopManager shop,
			UiCollisionDetector collisionChecker, IMouseListener mouseListener) {
		
		this.shop = shop;
		this.pointConverter = pointConverter;
		this.collisionChecker = collisionChecker;
		this.mouseListener = mouseListener;
	}
	
	public boolean isAttached() { return attached; }
	
	public void attach(ShopItem shopItem, LocManCreator<?> creator, IImage image,
			double viewRadius, MousePointer scaledMousePointer, boolean stayAttached) {
		
		mousePointer = mouseListener.getPointerById(scaledMousePointer.getId());
		this.creator = creator;
		this.image = image;
		this.shopItem = shopItem;
		this.viewRadius = (float) viewRadius;
		this.stayAttached = stayAttached;
		attached = true;
		beenReleased = false;
		updateAttachedInfo();
	}

	public void clearAttached() {
		mousePointer = null;
		creator = null;
		image = null;
		attached = false;
		beenReleased = false;
	}
	
	public void placeAttached() {
		if (canPlace()) {
			shop.purchase(shopItem);
			creator.create(gameX, gameY);
			beenReleased = false;
			if (!stayAttached)
				clearAttached();
		} else {
			clearAttached();
		}
	}
	
	private boolean canPlace() {
		return shop.canPurchase(shopItem) &&
				!collisionChecker.isCollidingWith(realX, realY) &&
				creator.isValidLocation(gameX, gameY);
	}
	
	@Override
	public void draw(IGraphics g) {
		if (mousePointer != null && !mousePointer.isPressed())
			beenReleased = true;
		
		if (attached) {
			if (stayAttached && beenReleased && mousePointer.isPressed()) {
				placeAttached();
			} else if (!stayAttached && !mousePointer.isPressed()){
				placeAttached();
			}
		}
		
		if (!attached)
			return;
		
		updateAttachedInfo();

		g.drawImage(image, gameX, gameY);
		boolean savedAntiAlias = g.isAntiAliasOn();
		g.setAntiAlias(true);

		g.setColor(canPlace() ? TsColor.green : TsColor.red);
		g.drawRect(gameX, gameY, image.getWidth(g), image.getHeight(g));
		
		if (viewRadius > 0.0f) {
			g.drawOval(gameX + 0.5f - viewRadius,
					gameY + 0.5f - viewRadius,
					viewRadius * 2, viewRadius * 2);
		}
		
		float crossR = viewRadius < 3 ? 3 : viewRadius;
		g.drawLine(gameX + 0.5f, gameY + 0.5f - crossR,
				gameX + 0.5f, gameY + 0.5f + crossR);
		g.drawLine(gameX + 0.5f - crossR, gameY + 0.5f,
				gameX + 0.5f + crossR, gameY + 0.5f);
		
		g.setAntiAlias(savedAntiAlias);
	}
	
	private void updateAttachedInfo() {
		float x = mousePointer.getX();
		float y = mousePointer.getY();
		realX = (int) x;
		realY = (int) y;
		gameX = (int) pointConverter.screenToGameX(x);
		gameY = (int) pointConverter.screenToGameY(y);
	}
}
