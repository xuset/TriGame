package triGame.game.shopping;

public class UpgradeItem {
	public ShopItem shopItem;
	public int upgradeCount, maxUpgrades, value, upgradeIncriment;
	
	public UpgradeItem(ShopItem shopItem, int maxUpgrades, int value, int upgradeIncriment) {
		this.shopItem = shopItem;
		this.value = value;
		this.upgradeIncriment = upgradeIncriment;
		this.maxUpgrades = maxUpgrades;
	}
	
	public UpgradeItem(String name, int cost, int maxUpgrades, int value, int upgradeIncriment) {
		this(new ShopItem(name, cost), maxUpgrades, value, upgradeIncriment);
	}
	
	public String toString() {
		return shopItem.name;
	}
	
	
}
