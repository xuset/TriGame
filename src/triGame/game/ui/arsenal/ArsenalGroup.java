package triGame.game.ui.arsenal;

import java.util.ArrayList;

public class ArsenalGroup {
	public static interface PurchaseEvent { void purchase(ArsenalItemInfo item); }
	
	String name;
	
	public ArrayList<ArsenalItem> items = new ArrayList<ArsenalItem>();
	public PurchaseEvent purchaseEvent = null;
	
	public String name() { return name; }
	
	public ArsenalGroup(String name, PurchaseEvent purchaseEvent) {
		this.name = name;
		this.purchaseEvent = purchaseEvent;
	}
}
