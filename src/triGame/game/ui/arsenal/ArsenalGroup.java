package triGame.game.ui.arsenal;

import java.util.ArrayList;

public class ArsenalGroup {
	public static interface PurchaseEvent { void purchase(ArsenalItemInfo item); }
	
	public final String name;
	
	public final ArrayList<ArsenalItem> items = new ArrayList<ArsenalItem>();
	public PurchaseEvent purchaseEvent = null;
	
	public ArsenalGroup(String name, PurchaseEvent purchaseEvent) {
		this.name = name;
		this.purchaseEvent = purchaseEvent;
	}
}
