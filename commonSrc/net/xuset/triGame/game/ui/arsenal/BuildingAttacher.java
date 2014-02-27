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

public class BuildingAttacher implements GameDrawable{
	private final PointConverter pointConverter;
	private final ShopManager shop;
	
	private LocManCreator<?> creator;
	private IImage image;
	private ShopItem shopItem;
	private boolean attached = false;
	
	private double mouseX = 0, mouseY = 0;
	private boolean shouldPurchase = false;
	
	
	public BuildingAttacher(IMouseListener ml, PointConverter pointConverter, ShopManager shop) {
		this.shop = shop;
		this.pointConverter = pointConverter;
		ml.watch(new MouseObserver());
	}
	
	public void attach(ShopItem shopItem, LocManCreator<?> c, IImage image,
			double mouseX, double mouseY) {
		
		this.mouseX = mouseX;
		this.mouseY = mouseY;
		creator = c;
		this.image = image;
		this.shopItem = shopItem;
		attached = true;
	}
	
	public void clearAttached() {
		creator = null;
		image = null;
		attached = false;
	}
	
	public void placeAttached() {
		double x = pointConverter.screenToGameX(mouseX);
		double y = pointConverter.screenToGameY(mouseY);
		if (canPlace()) {
			shop.purchase(shopItem);
			creator.create(x, y);
		}
		shouldPurchase = false;
		clearAttached();
	}
	
	private boolean canPlace() {
		double x = pointConverter.screenToGameX(mouseX);
		double y = pointConverter.screenToGameY(mouseY);
		return creator.isValidLocation(x, y) &&
				shop.canPurchase(shopItem);
	}
	
	@Override
	public void draw(IGraphics g) {
		if (shouldPurchase && attached)
			placeAttached();
		
		if (!attached)
			return;

		g.drawImage(image, (int) mouseX, (int) mouseY);

		if (canPlace())
			g.setColor(TsColor.green);
		else
			g.setColor(TsColor.red);
		g.drawRect((int) mouseX, (int) mouseY, image.getWidth(), image.getHeight());
	}
	
	private class MouseObserver implements Change<TsMouseEvent> {

		@Override
		public void observeChange(TsMouseEvent t) {
			if (t.action == MouseAction.DRAG || t.action == MouseAction.MOVE) {
				mouseX = pointConverter.gameToScreenX((int) pointConverter.screenToGameX(t.x));
				mouseY = pointConverter.gameToScreenY((int) pointConverter.screenToGameY(t.y));
			}
			
			if (attached && t.action == MouseAction.RELEASE) {
				shouldPurchase = true;
			}
		}
		
	}
}
