package triGame.game.playerInterface;


import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import triGame.game.shopping.ShopItem;
import triGame.game.shopping.ShopManager;

public class GunPanel extends JPanel{
	private static final long serialVersionUID = -6314923759100183640L;
	
	private TwoColumnContainerPanel container = new TwoColumnContainerPanel();
	private JButton buyButton = new JButton("Buy");
	private UpgradePanel upgradePanel;
	private ShopManager shopManager;
	private ItemInfoPanel infoPanel;
	private GunItem selectedItem;
	
	public GunPanel(ShopManager shop, UpgradePanel panel, ItemInfoPanel infoPanel) {
		this.upgradePanel = panel;
		this.shopManager = shop;
		this.infoPanel = infoPanel;
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setAlignmentY(Component.TOP_ALIGNMENT);
		this.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.setBorder(BorderFactory.createTitledBorder("Guns"));
		buyButton.setEnabled(false);
		buyButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		buyButton.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent arg0) {
				if (buyButton.isEnabled()) {
					shopManager.purchase(selectedItem.getShopItem());
					buyButton.setEnabled(false);
					upgradePanel.setUpgradeManager(selectedItem.getUpgradeManager(), null);
				}
			}
			public void mouseEntered(MouseEvent arg0) { }
			public void mouseExited(MouseEvent arg0) { }
			public void mousePressed(MouseEvent arg0) { }
			public void mouseReleased(MouseEvent arg0) { }
		});
		this.add(container);
		this.add(Box.createVerticalStrut(25));
		this.add(buyButton);
	}
	
	public void itemClicked(GunItem gun) {
		this.unselectAllItems();
		this.selectedItem = gun;
		ShopItem shopItem = gun.getShopItem();
		infoPanel.setItemName(gun.getText());
		gun.setBorder(BorderFactory.createLineBorder(Color.blue, 1));
		if (gun.getShopItem() == null || shopManager.hasPurchased(shopItem)) {
			infoPanel.setCost("Purchased");
			upgradePanel.setUpgradeManager(gun.getUpgradeManager(), null);
			buyButton.setEnabled(false);
		} else {
			infoPanel.setCost(shopItem.getCost());
			upgradePanel.clearUpgradeManager();
			if (shopManager.canPurchase(gun.getShopItem()))
				buyButton.setEnabled(true);
		}
	}
	
	public void unselect() {
		selectedItem = null;
		buyButton.setEnabled(false);
	}
	
	public boolean isItemSelected() {
		if (selectedItem == null)
			return false;
		return true;
	}
	
	private Component[] comps;
	public void unselectAllItems() {
		unselect();
		comps = container.getLeftColumnComponents();
		for (int i = 0; i < comps.length; i++) {
			if (comps[i] instanceof GunItem)
				((GunItem) comps[i]).unselect();
		}
		comps = container.getRightColumnComponents();
		for (int i = 0; i < comps.length; i++) {
			if (comps[i] instanceof GunItem)
				((GunItem) comps[i]).unselect();
		}
	}
	
	public void addGuns(Component comp) {
		container.addArsenal(comp);
	}
}
