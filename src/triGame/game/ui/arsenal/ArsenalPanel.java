package triGame.game.ui.arsenal;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import triGame.game.shopping.ShopManager;

public class ArsenalPanel{
	
	private ArsenalGroup displayedGroup;
	private JButton btnSwitch = new JButton();
	private ShopManager shop;

	JPanel panel;
	
	public ArrayList<ArsenalGroup> groups = new ArrayList<ArsenalGroup>();
	
	public ArsenalPanel(ShopManager shop) {
		this.shop = shop;
		panel = new JPanel();
		//panel.setBorder(BorderFactory.createLineBorder(Color.black, 2, true));
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.setAlignmentY(Component.CENTER_ALIGNMENT);
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		btnSwitch.addMouseListener(lblSwitchEvent);
		btnSwitch.setAlignmentY(Component.BOTTOM_ALIGNMENT);
	}
	
	private MouseListener lblSwitchEvent = new MouseListener() {
		@Override
		public void mouseClicked(MouseEvent arg0) {
			Iterator<ArsenalGroup> it = groups.iterator();
			while (it.hasNext()) {
				ArsenalGroup next = it.next();
				if (next == displayedGroup) {
					if (it.hasNext())
						switchGroup(it.next());
					else
						switchGroup(groups.get(0));
					break;
				}
			}
		}
		public void mouseEntered(MouseEvent arg0) { }
		public void mouseExited(MouseEvent arg0) { }
		public void mousePressed(MouseEvent arg0) { }
		public void mouseReleased(MouseEvent arg0) { }
	};
	
	public void addToArsenal(ArsenalGroup group, ArsenalItem aItem) {
		if (groups.contains(group)) {
			group.items.add(aItem);
			aItem.info.arsenalGroup = group;
			aItem.shop = shop;
		} else {
			groups.add(group);
			group.items.add(aItem);
			aItem.info.arsenalGroup = group;
			aItem.shop = shop;
		}
		if (displayingGroup(group))
			refreshDisplay();
	}
	
	public void refreshDisplay() {
		panel.removeAll();
		panel.add(btnSwitch);
		for (ArsenalItem aItem : displayedGroup.items) {
			if (aItem.visibile) {
				panel.add(Box.createHorizontalStrut(20));
				panel.add(aItem);
			}
		}
	}
	
	public void switchGroup(ArsenalGroup g) {
		displayedGroup = g;
		btnSwitch.setText(g.name);
		refreshDisplay();
	}
	
	public boolean displayingGroup(ArsenalGroup g) {
		if (displayedGroup == g)
			return true;
		return false;
	}
}
