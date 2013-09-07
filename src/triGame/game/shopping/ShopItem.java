package triGame.game.shopping;

public class ShopItem {
	String name = "";
	int cost = 0;
	
	boolean oneTimePurchase = false;
	
	public String getName() { return name; }
	public int getCost() { return cost; }
	public boolean isOneTimePurchase() { return oneTimePurchase; }
	
	public ShopItem(String name, int cost) {
		this.name = name;
		this.cost = cost;
	}
	public ShopItem(String name, int cost, boolean isOneTimePurchase) {
		this.name = name;
		this.cost = cost;
		this.oneTimePurchase = isOneTimePurchase;
	}
}
