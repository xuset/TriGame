package net.xuset.triGame.game.ui.arsenal;

import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.TsColor;
import net.xuset.tSquare.system.input.mouse.MouseAction;
import net.xuset.tSquare.system.input.mouse.TsMouseEvent;
import net.xuset.tSquare.ui.Alignment;
import net.xuset.tSquare.ui.Axis;
import net.xuset.tSquare.ui.UiForm;
import net.xuset.tSquare.ui.UiLabel;
import net.xuset.tSquare.util.Observer.Change;
import net.xuset.triGame.game.guns.GunType;
import net.xuset.triGame.game.shopping.ShopItem;
import net.xuset.triGame.game.shopping.ShopManager;
import net.xuset.triGame.game.shopping.UpgradeManager;
import net.xuset.triGame.game.ui.upgrades.UiUpgrades;

public class GunPanel extends UiForm{
	private final GunType gunType;
	private final ShopItem item;
	private final UpgradeManager upgrades;
	private final ShopManager shop;
	private final UiLabel lblPrice = new UiLabel();
	private final UiUpgrades uiUpgrades;
	private long clickTime = 0l;
	private boolean isSelected = false;
	
	public GunPanel(GunType gunType, String name, ShopItem item, UpgradeManager upgrades,
			ShopManager shop, UiUpgrades uiUpgrades) {
		this.gunType = gunType;
		this.item = item;
		this.upgrades = upgrades;
		this.shop = shop;
		this.uiUpgrades = uiUpgrades;
		
		shop.observer().watch(new ShopObserver());
		lblPrice.setText("$" + item.getCost());
		resetLblPrice(shop);
		
		getLayout().setOrientation(Axis.Y_AXIS);
		getLayout().setAlignment(Axis.X_AXIS, Alignment.CENTER);
		getLayout().add(new UiLabel(name));
		getLayout().add(lblPrice);
	}
	
	public UpgradeManager getGunUpgradeManager() { return upgrades; }
	
	public GunType getGunType() { return gunType; }
	
	public String getGunName() { return item.getName(); }
	
	public boolean hasPurchased() { return shop.hasPurchased(item); }
	
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	
	@Override
	protected void recieveMouseEvent(TsMouseEvent e, float x, float y) {
		super.recieveMouseEvent(e, x, y);
		
		if (e.action == MouseAction.PRESS) {
			if (shop.purchase(item))
				uiUpgrades.showUpgrades(getGunName(), getGunUpgradeManager());
			clickTime = System.currentTimeMillis();
		}
	}
	
	@Override
	public void draw(IGraphics g) {
		boolean bVis = isSelected || clickTime + 50 > System.currentTimeMillis();
		getBorder().setVisibility(bVis);
		super.draw(g);
	}
	
	private void resetLblPrice(ShopManager shop) {
		if (shop.getPointCount() < item.getCost())
			lblPrice.setForeground(TsColor.red);
		else
			lblPrice.setForeground(TsColor.black);
		
		if (shop.hasPurchased(item))
			lblPrice.setText("");
	}
	
	private class ShopObserver implements Change<ShopManager> {
		@Override
		public void observeChange(ShopManager t) {
			resetLblPrice(t);
		}
	}
}
