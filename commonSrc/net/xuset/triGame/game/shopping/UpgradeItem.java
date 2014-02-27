package net.xuset.triGame.game.shopping;

public class UpgradeItem {
	public final ShopItem shopItem;
	public final double initialValue;
	public final double upgradeIncriment;
	public final int maxUpgrades;
	
	private int upgradeCount;
	
	public int getUpgradeCount() { return upgradeCount; }
	public double getValue() { return initialValue + upgradeCount * upgradeIncriment; }
	
	
	public UpgradeItem(ShopItem shopItem, int maxUpgrades, double initialValue, double upgradeIncriment) {
		this.shopItem = shopItem;
		this.initialValue = initialValue;
		this.upgradeIncriment = upgradeIncriment;
		this.maxUpgrades = maxUpgrades;
	}
	
	void upgrade() {
		if (upgradeCount < maxUpgrades)
			upgradeCount++;
	}

	@Override
	public String toString() {
		return shopItem.name;
	}
	
	
}
