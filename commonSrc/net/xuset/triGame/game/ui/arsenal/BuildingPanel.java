package net.xuset.triGame.game.ui.arsenal;

import net.xuset.tSquare.imaging.IImage;
import net.xuset.tSquare.imaging.TsColor;
import net.xuset.tSquare.system.input.mouse.MouseAction;
import net.xuset.tSquare.system.input.mouse.TsMouseEvent;
import net.xuset.tSquare.ui.Alignment;
import net.xuset.tSquare.ui.Axis;
import net.xuset.tSquare.ui.UiForm;
import net.xuset.tSquare.ui.UiLabel;
import net.xuset.tSquare.util.Observer.Change;
import net.xuset.triGame.game.entities.LocManCreator;
import net.xuset.triGame.game.shopping.ShopItem;
import net.xuset.triGame.game.shopping.ShopManager;

public class BuildingPanel extends UiForm {
	private final ShopItem shopItem;
	private final LocManCreator<?> creator;
	private final IImage img;
	private final BuildingAttacher attacher;
	private final UiLabel lblPrice;
	private final double viewRadius;
	private final ItemMetaSetter mouseCallback;
	private final String description;
	private boolean canPurchase = true;
	
	public BuildingPanel(ShopItem shopItem, LocManCreator<?> creator, IImage img,
			BuildingAttacher attacher, ShopManager shop, double viewRadius,
			ItemMetaSetter mouseCallback, String description) {
		
		this.shopItem = shopItem;
		this.creator = creator;
		this.img = img;
		this.viewRadius = viewRadius;
		this.attacher = attacher;
		this.mouseCallback = mouseCallback;
		this.description = description;
		
		shop.observer().watch(new ShopObserver());
		
		UiLabel lblImage = new UiLabel();
		lblImage.setImage(img);
		lblPrice = new UiLabel("$" + shopItem.getCost());
		
		getLayout().setOrientation(Axis.Y_AXIS);
		getLayout().setAlignment(Axis.X_AXIS, Alignment.CENTER);
		getLayout().add(lblImage);
		getLayout().add(lblPrice);
		
		canPurchase = shop.canPurchase(shopItem);
		resetLblPriceColor();
	}

	@Override
	protected void recieveMouseEvent(TsMouseEvent e, float x, float y) {
		super.recieveMouseEvent(e, x, y);
		
		if (e.action == MouseAction.MOVE || e.action == MouseAction.PRESS) {
			mouseCallback.setDisplayInfo(shopItem.getName(), description, getBorder());
		}
		
		if (canPurchase && e.action == MouseAction.PRESS)
			attacher.attach(shopItem, creator, img, viewRadius, e.pointer);
	}
	
	private void resetLblPriceColor() {
		lblPrice.setForeground(canPurchase ? TsColor.black : TsColor.red);
	}
	
	private class ShopObserver implements Change<ShopManager> {
		@Override
		public void observeChange(ShopManager t) {
			canPurchase = t.canPurchase(shopItem);
			resetLblPriceColor();
		}
	}
}
