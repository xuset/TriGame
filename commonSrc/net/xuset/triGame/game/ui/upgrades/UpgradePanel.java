package net.xuset.triGame.game.ui.upgrades;

import net.xuset.tSquare.imaging.TsColor;
import net.xuset.tSquare.system.input.mouse.MouseAction;
import net.xuset.tSquare.system.input.mouse.TsMouseEvent;
import net.xuset.tSquare.ui.Alignment;
import net.xuset.tSquare.ui.Axis;
import net.xuset.tSquare.ui.UiButton;
import net.xuset.tSquare.ui.UiForm;
import net.xuset.tSquare.ui.UiLabel;
import net.xuset.tSquare.ui.UiProgressBar;
import net.xuset.tSquare.ui.layout.UiLayout;
import net.xuset.tSquare.util.Observer.Change;
import net.xuset.triGame.game.shopping.ShopManager;
import net.xuset.triGame.game.shopping.UpgradeItem;
import net.xuset.triGame.game.shopping.UpgradeManager;

public class UpgradePanel extends UiForm {
	private final ShopManager shop;
	private final UiLabel lblName = new UiLabel("");
	private final UiLabel lblPrice = new UiLabel("");
	private final UiProgressBar progressBar = new UiProgressBar();
	private final UiButton btnBuy = new UiButton("Buy");
	
	private UpgradeItem uItem = null;
	private UpgradeManager uManager = null;
	
	public UpgradePanel(ShopManager shop) {
		this.shop = shop;
		shop.observer().watch(new OnShopChange());
		
		btnBuy.addMouseListener(new OnBuyMouseEvent());
		btnBuy.setEnabled(false);
		
		UiLayout l = getLayout();
		l.setAlignment(Axis.Y_AXIS, Alignment.CENTER);
		l.setAlignment(Axis.X_AXIS, Alignment.BACK);
		l.add(lblName);
		l.add(progressBar);
		l.add(btnBuy);
		l.add(lblPrice);
	}
	
	void setUpgrade(UpgradeItem uItem, UpgradeManager uManager) {
		this.uItem = uItem;
		this.uManager = uManager;
		lblName.setText(uItem.shopItem.getName());
		lblPrice.setText("$" + uItem.shopItem.getCost());
		resetStyle();
	}
	
	private void resetStyle() {
		double progress = uItem.getUpgradeCount() / (0.0 + uItem.maxUpgrades);
		progressBar.setProgress(progress);
		lblPrice.setForeground(shop.canPurchase(uItem.shopItem) ?
				TsColor.black : TsColor.red);
		btnBuy.setEnabled(canPurchase());
	}
	
	private boolean canPurchase() {
		return shop.canPurchase(uItem.shopItem) &&
				uItem.getUpgradeCount() < uItem.maxUpgrades;
	}
	
	private class OnBuyMouseEvent implements Change<TsMouseEvent> {
		private static final long maxTimeHeld = 1000;
		private long timePressed = 0;
		
		@Override
		public void observeChange(TsMouseEvent t) {
			if (t.action == MouseAction.PRESS)
				timePressed = System.currentTimeMillis();
			
			if (t.action == MouseAction.RELEASE &&
					timePressed + maxTimeHeld > System.currentTimeMillis() &&
					canPurchase()) {
				
				uManager.upgrade(uItem, shop);
				resetStyle();
			}
		}
	}
	
	private class OnShopChange implements Change<ShopManager> {

		@Override
		public void observeChange(ShopManager t) {
			if (uItem != null && uManager != null)
				resetStyle();
		}
		
	}
}
