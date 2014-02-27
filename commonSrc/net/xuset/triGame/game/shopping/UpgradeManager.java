package net.xuset.triGame.game.shopping;

import java.util.ArrayList;

import net.xuset.tSquare.util.Observer;




public class UpgradeManager {
	private Observer<UpgradeItem> observer = new Observer<UpgradeItem>();
	
	public ArrayList<UpgradeItem> items = new ArrayList<UpgradeItem>();
	
	public Observer<UpgradeItem> observer() { return observer; }
	
	public UpgradeItem addUpgrade(UpgradeItem item) {
		items.add(item);
		return item;
	}
	
	public UpgradeItem getUpgrade(ShopItem item) {
		for (UpgradeItem ui : items) {
			if (ui.shopItem == item) {
				return ui;
			}
		}
		return null;
	}
	
	public boolean upgrade(UpgradeItem item, ShopManager shop) {
		if (item.getUpgradeCount() < item.maxUpgrades && shop.canPurchase(item.shopItem)) {
			shop.purchase(item.shopItem);
			item.upgrade();
			observer.notifyWatchers(item);
			return true;
		}
		return false;
	}
}
