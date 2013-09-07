package triGame.game.shopping;

import java.util.ArrayList;


public class UpgradeManager {
	private String name;
	private ShopManager shopManager;
	
	public ArrayList<UpgradeItem> items = new ArrayList<UpgradeItem>();
	
	public String getName() { return name; }
	
	public UpgradeManager(ShopManager shopManager, String name) {
		this.shopManager = shopManager;
		this.name = name;
	}
	
	public UpgradeItem addUpgrade(String name, int cost) {
		UpgradeItem item = new UpgradeItem(new ShopItem(name, cost));
		items.add(item);
		return item;
	}
	
	public UpgradeItem addUpgrade(UpgradeItem item) {
		items.add(item);
		return item;
	}
	
	public UpgradeItem addUpgrade(ShopItem shopItem) {
		UpgradeItem item = new UpgradeItem(shopItem);
		items.add(item);
		return item;
	}
	
	public void addUpgrades(ArrayList<ShopItem> shopItems) {
		for (int i = 0; i < shopItems.size(); i++) {
			items.add(new UpgradeItem(shopItems.get(i)));
		}
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
			return true;
		}
		return false;
	}
	
	public boolean upgrade(int itemsIndex) {
		return upgrade(items.get(itemsIndex));
	}
}
