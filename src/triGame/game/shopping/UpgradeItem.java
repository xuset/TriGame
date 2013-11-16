package triGame.game.shopping;

public class UpgradeItem {
	public final ShopItem shopItem;
	public final int initialValue;
	public final int upgradeIncriment;
	public final int maxUpgrades;
	
	private int upgradeCount;
	
	public int getUpgradeCount() { return upgradeCount; }
	public int getValue() { return initialValue + upgradeCount * upgradeIncriment; }
	
	
	public UpgradeItem(ShopItem shopItem, int maxUpgrades, int initialValue, int upgradeIncriment) {
		this.shopItem = shopItem;
		this.initialValue = initialValue;
		this.upgradeIncriment = upgradeIncriment;
		this.maxUpgrades = maxUpgrades;
	}
	
	void upgrade() {
		if (upgradeCount < maxUpgrades)
			upgradeCount++;
	}
	
	public String toString() {
		return shopItem.name;
	}
	
	
}
