package triGame.game.ui.arsenal;

import javax.swing.Box;
import javax.swing.JPanel;

import triGame.game.ui.JPanelGetter;

public class ArsenalGroup implements JPanelGetter{
	public static interface PurchaseEvent { void purchase(ArsenalItemInfo item); }
	
	public final String name;
	public final JPanel panel = new JPanel();
	public PurchaseEvent purchaseEvent = null;
	
	public ArsenalGroup(String name, PurchaseEvent purchaseEvent) {
		this.name = name;
		this.purchaseEvent = purchaseEvent;
		panel.add(Box.createHorizontalGlue());
	}
	
	public void addArsenalItem(ArsenalItem a) {
		panel.add(a);
		panel.add(Box.createHorizontalGlue());
	}
	
	protected void onSwitchTo(ArsenalPanel arseanlPanel) {
		
	}

	@Override
	public JPanel getJPanel() {
		return panel;
	}
	
	
}
