package triGame.game.ui.upgrades;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

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

import triGame.game.shopping.ShopManager;
import triGame.game.shopping.UpgradeItem;
import triGame.game.shopping.UpgradeManager;

public class UpgradePanel extends JPanel{
	private static final long serialVersionUID = 8498226293916709649L;
	
	private JButton btnBuy = new JButton("purchase");
	private JLabel lblPrice = new JLabel("$0");
	private JProgressBar pbProgress = new JProgressBar();
	private JPanel pnlAttribute = new JPanel();
	private TitledBorder bdrAttribute = BorderFactory.createTitledBorder("");
	private JComboBox<UpgradeItem> cmbList = new JComboBox<UpgradeItem>();
	private JLabel lblImage = new JLabel();
	
	private UpgradeItem selectedAttribute = null;
	private UpgradeManager upgradeManager = null;
	private ShopManager shop;
	
	public UpgradePanel(ShopManager shop) {
		this.shop = shop;
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		pnlAttribute.setBorder(bdrAttribute);
		pnlAttribute.setLayout(new BoxLayout(pnlAttribute, BoxLayout.X_AXIS));
		btnBuy.addMouseListener(btnListener);
		cmbList.addItemListener(cmbListener);
		JPanel pnlCombo = new JPanel();
		add(lblImage);
		pnlCombo.add(cmbList);
		add(pnlCombo);
		add(Box.createHorizontalGlue());
		pnlAttribute.add(pbProgress);
		pnlAttribute.add(Box.createHorizontalStrut(15));
		pnlAttribute.add(lblPrice);
		pnlAttribute.add(btnBuy);
		add(pnlAttribute);
	}
	
	public void set(UpgradeManager manager,  String text) {
		lblImage.setText(text);
		lblImage.setIcon(null);
		set(manager);
	}
	
	public void set(UpgradeManager manager, BufferedImage image) {
		lblImage.setIcon(new ImageIcon(image));
		lblImage.setText("");
		set(manager);
	}
	
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
		lblPrice.setText("$" + upgrade.shopItem.getCost());
		int value = (int) (((double) upgrade.upgradeCount) / upgrade.maxUpgrades * 100);
		pbProgress.setValue(value);
		bdrAttribute.setTitle(upgrade.shopItem.getName());
		if (upgrade.upgradeCount >= upgrade.maxUpgrades)
			btnBuy.setEnabled(false);
		else
			btnBuy.setEnabled(true);
		selectedAttribute = upgrade;
		pnlAttribute.updateUI();
		
	}
	
	private void clearAttribute() {
		lblPrice.setText("$0");
		pbProgress.setValue(0);
		bdrAttribute.setTitle("Upgrade");
		btnBuy.setEnabled(false);
		selectedAttribute = null;
	}
	
	private ItemListener cmbListener = new ItemListener() {
		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED)
				setAttribute((UpgradeItem) e.getItem());
		}
	};
	
	private MouseListener btnListener = new MouseListener() {
		@Override
		public void mouseClicked(MouseEvent arg0) {
			if (selectedAttribute != null && shop.canPurchase(selectedAttribute.shopItem)) {
				upgradeManager.upgrade(selectedAttribute);
				setAttribute(selectedAttribute);
			}
				
		}
		public void mouseEntered(MouseEvent arg0) { }
		public void mouseExited(MouseEvent arg0) { }
		public void mousePressed(MouseEvent arg0) { }
		public void mouseReleased(MouseEvent arg0) { }
	};
}
