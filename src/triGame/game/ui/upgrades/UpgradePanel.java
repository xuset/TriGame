package triGame.game.ui.upgrades;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.TitledBorder;

import tSquare.imaging.Sprite;
import tSquare.util.Observer;
import triGame.game.entities.PointParticle;
import triGame.game.shopping.ShopManager;
import triGame.game.shopping.UpgradeItem;
import triGame.game.shopping.UpgradeManager;
import triGame.game.ui.UserInterface;

public class UpgradePanel extends JPanel{
	private static final long serialVersionUID = 8498226293916709649L;
	
	private final UserInterface ui;
	private final JButton btnBuy = new JButton("purchase");
	private final JLabel lblPrice = new JLabel("0");
	private final JProgressBar pbProgress = new JProgressBar();
	private final JPanel pnlAttribute = new JPanel();
	private final TitledBorder bdrAttribute = BorderFactory.createTitledBorder("");
	@SuppressWarnings("rawtypes")
	private final JComboBox cmbList = new JComboBox(); // for java 6
	private final JLabel lblImage = new JLabel();
	
	private UpgradeItem selectedAttribute = null;
	private UpgradeManager upgradeManager = null;
	private ShopManager shop;
	
	public UpgradePanel(ShopManager shop, UserInterface ui) {
		this.shop = shop;
		this.ui = ui;
		
		shop.observer().watch(new PointObserver());
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		btnBuy.addActionListener(new PurchaseListener());
		cmbList.addItemListener(new ComboListener());
		
		lblPrice.setIcon(new ImageIcon(Sprite.get(PointParticle.SPRITE_ID).image));
		lblPrice.setOpaque(true);
		lblPrice.setBackground(Color.LIGHT_GRAY);
		lblPrice.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new BackListener());
		
		JPanel pnlCombo = new JPanel();
		pnlCombo.add(cmbList);
		
		pnlAttribute.setBorder(bdrAttribute);
		pnlAttribute.setLayout(new BoxLayout(pnlAttribute, BoxLayout.X_AXIS));
		pnlAttribute.add(pbProgress);
		pnlAttribute.add(Box.createHorizontalStrut(15));
		pnlAttribute.add(lblPrice);
		pnlAttribute.add(btnBuy);
		
		add(btnBack);
		add(lblImage);
		add(pnlCombo);
		add(Box.createHorizontalGlue());
		add(pnlAttribute);
	}
	
	public void set(UpgradeManager manager,  String text) {
		lblImage.setText(text);
		lblImage.setIcon(null);
		set(manager);
	}
	
	public void set(UpgradeManager manager, Image image) {
		lblImage.setIcon(new ImageIcon(image));
		lblImage.setText("");
		set(manager);
	}
	
	@SuppressWarnings("unchecked")
	private void set(UpgradeManager manager) {
		upgradeManager = manager;
		cmbList.removeAllItems();
		for (UpgradeItem uItem : manager.items)
			cmbList.addItem(uItem);
		if (manager.items.isEmpty())
			clearAttribute();
		else
			setAttribute(manager.items.get(0));
	}
	
	private void setAttribute(UpgradeItem upgrade) {
		lblPrice.setText("" + upgrade.shopItem.getCost());
		if (shop.getPointCount() < upgrade.shopItem.getCost())
			lblPrice.setForeground(Color.red);
		else
			lblPrice.setForeground(Color.black);
		int value = (int) (((double) upgrade.getUpgradeCount()) / upgrade.maxUpgrades * 100);
		pbProgress.setValue(value);
		bdrAttribute.setTitle(upgrade.shopItem.getName());
		if (upgrade.getUpgradeCount() >= upgrade.maxUpgrades)
			btnBuy.setEnabled(false);
		else
			btnBuy.setEnabled(true);
		selectedAttribute = upgrade;
		pnlAttribute.updateUI();
		
	}
	
	private void clearAttribute() {
		lblPrice.setText("0");
		lblPrice.setForeground(Color.black);
		pbProgress.setValue(0);
		bdrAttribute.setTitle("Upgrade");
		btnBuy.setEnabled(false);
		selectedAttribute = null;
	}
	
	private class PointObserver implements Observer.Change<ShopManager> {
		@Override
		public void observeChange(ShopManager t) {
			if (selectedAttribute != null) {
				if (t.getPointCount() < selectedAttribute.shopItem.getCost())
					lblPrice.setForeground(Color.red);
				else
					lblPrice.setForeground(Color.black);
				
			}
		}
	};
	
	private class ComboListener implements ItemListener {
		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED)
				setAttribute((UpgradeItem) e.getItem());
			ui.focus.surrenderFocus();
		}
	};
	
	private class PurchaseListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			ui.focus.surrenderFocus();
			if (selectedAttribute != null && shop.canPurchase(selectedAttribute.shopItem)) {
				upgradeManager.upgrade(selectedAttribute, shop);
				setAttribute(selectedAttribute);
			}			
		}
	};
	
	private class BackListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			ui.switchTo(ui.arsenal);
			ui.focus.surrenderFocus();
		}
	}
}
