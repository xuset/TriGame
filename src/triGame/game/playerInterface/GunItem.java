package triGame.game.playerInterface;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import triGame.game.shopping.ShopItem;
import triGame.game.shopping.UpgradeManager;

public class GunItem extends JPanel implements MouseListener{
	private static final long serialVersionUID = 8832753390374636772L;
	
	public ShopItem getShopItem() { return shopItem; }
	public UpgradeManager getUpgradeManager() { return upgradeManager; }
	public String getText() { return label.getText(); }
	
	private ShopItem shopItem;
	private UpgradeManager upgradeManager;
	private GunPanel gunPanel;
	private JLabel label;
	
	public GunItem(ShopItem shopItem, UpgradeManager upgradeManager, String name, GunPanel gunPanel) {
		this.shopItem = shopItem;
		this.gunPanel = gunPanel;
		this.upgradeManager = upgradeManager;
		this.setSize(50, 50);
		this.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		label = new JLabel(name, JLabel.CENTER);
		label.setAlignmentY(Component.CENTER_ALIGNMENT);
		this.add(label);
		this.addMouseListener(this);
	}
	
	public void unselect() {
		this.setBorder(BorderFactory.createLineBorder(Color.black, 1));
	}
	
	public void mouseClicked(MouseEvent e) {
		gunPanel.itemClicked(this);
	}
	
	public void mouseExited(MouseEvent e) {
		if (gunPanel.isItemSelected() == false) {
			this.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		}
	}
	public void mouseEntered(MouseEvent e) {
		if (gunPanel.isItemSelected() == false) {
			this.setBorder(BorderFactory.createLineBorder(Color.blue, 1));
		}
	}
	
	public void mousePressed(MouseEvent e) { }
	public void mouseReleased(MouseEvent e) { }
}
