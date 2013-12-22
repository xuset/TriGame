package triGame.game.ui.upgrades;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

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

import tSquare.util.Observer;
import triGame.game.shopping.ShopManager;
import triGame.game.shopping.UpgradeItem;
import triGame.game.shopping.UpgradeManager;
import triGame.game.ui.FocusSurrender;

public class UpgradePanel extends JPanel{
	private static final long serialVersionUID = 8498226293916709649L;
	
	private final FocusSurrender focusSurrender;
	private JButton btnBuy = new JButton("purchase");
	private JLabel lblPrice = new JLabel("$0");
	private JProgressBar pbProgress = new JProgressBar();
	private JPanel pnlAttribute = new JPanel();
	private TitledBorder bdrAttribute = BorderFactory.createTitledBorder("");
	//private JComboBox<String> cmbList = new JComboBox<String>(); 	//for Java 7
	@SuppressWarnings("rawtypes")
	private JComboBox cmbList = new JComboBox(); 					//for java 6
	private JLabel lblImage = new JLabel();
	
	private UpgradeItem selectedAttribute = null;
	private UpgradeManager upgradeManager = null;
	private ShopManager shop;
	
	public UpgradePanel(ShopManager shop, FocusSurrender focusSurrender) {
		this.focusSurrender = focusSurrender;
		this.shop = shop;
		shop.observer().watch(pointChange);
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
		lblPrice.setText("$" + upgrade.shopItem.getCost());
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
		lblPrice.setText("$0");
		lblPrice.setForeground(Color.black);
		pbProgress.setValue(0);
		bdrAttribute.setTitle("Upgrade");
		btnBuy.setEnabled(false);
		selectedAttribute = null;
	}
	
	private Observer.Change<ShopManager> pointChange = new Observer.Change<ShopManager>() {
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
	
	private ItemListener cmbListener = new ItemListener() {
		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED)
				setAttribute((UpgradeItem) e.getItem());
			focusSurrender.surrenderFocus();
		}
	};
	
	private MouseListener btnListener = new MouseListener() {
		@Override
		public void mouseClicked(MouseEvent arg0) {
			if (selectedAttribute != null && shop.canPurchase(selectedAttribute.shopItem)) {
				upgradeManager.upgrade(selectedAttribute, shop);
				setAttribute(selectedAttribute);
			}
			focusSurrender.surrenderFocus();
		}
		public void mouseEntered(MouseEvent arg0) { }
		public void mouseExited(MouseEvent arg0) { }
		public void mousePressed(MouseEvent arg0) { }
		public void mouseReleased(MouseEvent arg0) { }
	};
}
