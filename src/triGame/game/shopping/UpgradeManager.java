package triGame.game.shopping;

import java.util.ArrayList;

import tSquare.util.Observer;


public class UpgradeManager {
	private String name;
	private ShopManager shopManager;
	private Observer<UpgradeItem> observer = new Observer<UpgradeItem>();
	
	public ArrayList<UpgradeItem> items = new ArrayList<UpgradeItem>();
	
	public String getName() { return name; }
	public Observer<UpgradeItem> observer() { return observer; }
	
	public UpgradeManager(ShopManager shopManager, String name) {
		this.shopManager = shopManager;
		this.name = name;
	}
	
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
	
	public boolean upgrade(UpgradeItem item) {
		if (item.upgradeCount < item.maxUpgrades && shopManager.canPurchase(item.shopItem)) {
			shopManager.purchase(item.shopItem);
			item.upgradeCount++;
			item.value += item.upgradeIncriment;
			observer.notifiyWatchers(item);
			return true;
		}
		return false;
	}
	
	public boolean upgrade(int itemsIndex) {
		return upgrade(items.get(itemsIndex));
	}
}
