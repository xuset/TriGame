package triGame.game.shopping;
import java.util.ArrayList;

import tSquare.util.Observer;


public class ShopManager {
	private ArrayList<ShopItem> reciept = new ArrayList<ShopItem>();
	private Observer<ShopManager> observer = new Observer<ShopManager>(this);
	
	public int points;
	
	public Observer<ShopManager> observer() { return observer; }
	
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
			observer.notifyWatchers();
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
