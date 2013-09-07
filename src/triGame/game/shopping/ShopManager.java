package triGame.game.shopping;
import java.util.ArrayList;


public class ShopManager {
	private ArrayList<ShopItem> reciept = new ArrayList<ShopItem>();
	
	public int points;
	
	public ShopManager(int points) {
		this.points = points;
	}
	
	public boolean hasPurchased(ShopItem item) {
		if (reciept.contains(item))
			return true;
		return false;
	}

	public boolean purchase(ShopItem item) {
		if (canPurchase(item)) {
			points -= item.getCost();
			if (reciept.contains(item) == false)
				reciept.add(item);
			return true;
		}
		return false;
	}
	
	public boolean canPurchase(ShopItem item) {
		if (points >= item.getCost() && ((item.oneTimePurchase == true && reciept.contains(item) == false) || item.oneTimePurchase == false))
			return true;
		return false;
	}
}
