package net.xuset.triGame.game.ui.upgrades;

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
		
		btnBuy.addMouseListener(new OnBuyMouseEvent());
		
		UiLayout l = getLayout();
		l.setAlignment(Axis.Y_AXIS, Alignment.CENTER);
		l.setAlignment(Axis.X_AXIS, Alignment.CENTER);
		l.add(lblName);
		l.add(lblPrice);
		l.add(progressBar);
		l.add(btnBuy);
	}
	
	void setUpgrade(UpgradeItem uItem, UpgradeManager uManager) {
		this.uItem = uItem;
		this.uManager = uManager;
		lblName.setText(uItem.shopItem.getName());
		double progress = uItem.getUpgradeCount() / (0.0 + uItem.maxUpgrades);
		progressBar.setProgress(progress);
		lblPrice.setText("$" + uItem.shopItem.getCost());
	}
	
	private class OnBuyMouseEvent implements Change<TsMouseEvent> {
		@Override
		public void observeChange(TsMouseEvent t) {
			if (t.action == MouseAction.PRESS) {
				uManager.upgrade(uItem, shop);
				setUpgrade(uItem, uManager);
			}
		}
	}
}
