package triGame.game.ui.arsenal;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import triGame.game.shopping.ShopManager;
import triGame.game.ui.UserInterface;

public class ArsenalPanel{
	
	private ArsenalGroup displayedGroup;
	private final JButton btnSwitch = new JButton();
	private final JLabel lblDescription = new JLabel("-", JLabel.CENTER);
	private final JPanel panel;
	private final ShopManager shop;
	private final UserInterface ui;

	final JPanel pnlSplit;
	
	public final ArrayList<ArsenalGroup> groups = new ArrayList<ArsenalGroup>();
	
	public ArsenalPanel(ShopManager shop, UserInterface ui) {
		this.ui = ui;
		this.shop = shop;
		
		pnlSplit = new JPanel();
		pnlSplit.setLayout(new BoxLayout(pnlSplit, BoxLayout.Y_AXIS));
		
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		//panel.setAlignmentY(Component.CENTER_ALIGNMENT);
		//panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		btnSwitch.addMouseListener(lblSwitchEvent);
		btnSwitch.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		
		JPanel pnlDescription = new JPanel();
		pnlDescription.add(lblDescription);
		
		pnlSplit.add(pnlDescription);
		pnlSplit.add(panel);
	}
	
	private MouseListener lblSwitchEvent = new MouseListener() {
		@Override
		public void mouseClicked(MouseEvent arg0) {
			switchGroup(getNextGroup());
			ui.giveupFocus();
		}
		public void mouseEntered(MouseEvent arg0) { }
		public void mouseExited(MouseEvent arg0) { }
		public void mousePressed(MouseEvent arg0) { }
		public void mouseReleased(MouseEvent arg0) { }
	};
	
	private ArsenalGroup getNextGroup() {
		Iterator<ArsenalGroup> it = groups.iterator();
		while (it.hasNext()) {
			ArsenalGroup next = it.next();
			if (next == displayedGroup) {
				if (it.hasNext())
					return it.next();
				else
					return groups.get(0);
			}
		}
		return null;
	}
	
	public void addToArsenal(ArsenalGroup group, ArsenalItem aItem) {
		if (groups.contains(group)) {
			addArsenalItem(group, aItem);
		} else {
			groups.add(group);
			addArsenalItem(group, aItem);
		}
		if (displayingGroup(group))
			refreshDisplay();
	}
	
	private void addArsenalItem(ArsenalGroup group, ArsenalItem item) {
		group.items.add(item);
		item.info.arsenalGroup = group;
		item.observeShopPoints.observeChange(shop);
		shop.observer().watch(item.observeShopPoints);
	}
	
	public void refreshDisplay() {
		lblDescription.setText("-");
		panel.removeAll();
		panel.add(btnSwitch);
		for (ArsenalItem aItem : displayedGroup.items) {
			aItem.arsenalPanel = this;
			if (aItem.visibile) {
				panel.add(Box.createHorizontalStrut(20));
				panel.add(aItem);
			}
		}
	}
	
	public void switchGroup(ArsenalGroup g) {
		displayedGroup = g;
		btnSwitch.setText(getNextGroup().name);
		refreshDisplay();
	}
	
	public boolean displayingGroup(ArsenalGroup g) {
		if (displayedGroup == g)
			return true;
		return false;
	}
	
	void displayDescription(String text) {
		lblDescription.setText(text);
	}
	
	void giveupFocus() { ui.giveupFocus(); }
}
