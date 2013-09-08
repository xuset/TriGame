package triGame.game.shopping;

public class UpgradeItem {
	public ShopItem shopItem;
	public int upgradeCount = 0;
	public int maxUpgrades = 3;
	public UpgradeItem(String name, int cost) {
		this.shopItem = new ShopItem(name, cost);
	}
	public UpgradeItem(ShopItem shopItem) {
		this.shopItem = shopItem;
	}
	
	public String toString() {
		return shopItem.name;
	}
	
	
}
