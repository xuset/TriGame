package triGame.game.ui.arsenal;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import tSquare.imaging.ImageProcess;
import tSquare.util.Observer;
import triGame.game.entities.LocManCreator;
import triGame.game.shopping.ShopItem;
import triGame.game.shopping.ShopManager;
import triGame.game.shopping.UpgradeManager;
import triGame.game.ui.Attacher;

public class ArsenalItem extends JPanel{
	public static final double ITEM_SIZE = 50.0;
	private static final long serialVersionUID = 8955177654417376268L;
	private static final Border selectedBorder = BorderFactory.createLineBorder(Color.black, 1);
	private static final Border emptyBorder = BorderFactory.createEmptyBorder(1, 1, 1, 1);

	
	final ArsenalItemInfo info = new ArsenalItemInfo();
	final JLabel lblMain;
	JLabel lblPrice;
	private final ArsenalPanel arsenalPanel;
	
	public boolean visibile = true;
	
	public ArsenalItemInfo getInfo() { return info; }
	
	private void construct(ShopItem item, JLabel main, String description) {
		this.setSize((int) ITEM_SIZE, (int) ITEM_SIZE);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		if (item.getCost() > 0)
			lblPrice = new JLabel("$" + item.getCost(), JLabel.CENTER);
		else
			lblPrice = new JLabel("");
		this.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.add(main);
		this.add(lblPrice);
		info.shopItem = item;
		info.arsenalItem = this;
		info.description = description;
		
		main.setBorder(emptyBorder);
		main.addMouseListener(lblMainEvent);
	}
	
	public ArsenalItem(ShopItem item, BufferedImage image, int radius,
			LocManCreator<?> creator, String description, ArsenalPanel arsenalPanel) {
		
		this.arsenalPanel = arsenalPanel;
		lblMain = new  JLabel();
		lblMain.setIcon(new ImageIcon(scale(image)));
		info.isImagge = true;
		info.image = image;
		info.attachedItem = new Attacher.AttachedItem(image, radius, item, creator);
		construct(item, lblMain, description);
	}
	
	public ArsenalItem(ShopItem item, String text, String description, UpgradeManager upgradeManager,
			ArsenalPanel arsenalPanel) {
		
		this.arsenalPanel = arsenalPanel;
		info.upgradeManager = upgradeManager;
		info.text = text;
		lblMain = new JLabel(text, JLabel.CENTER);
		construct(item, lblMain, description);
	}
	
	Observer.Change<ShopManager> observeShopPoints = new Observer.Change<ShopManager>() {
		@Override
		public void observeChange(ShopManager t) {
			if (t.getPointCount() < info.shopItem.getCost())
				lblPrice.setForeground(Color.red);
			else
				lblPrice.setForeground(Color.black);
		}
	};

	private MouseListener lblMainEvent = new MouseListener() {
		@Override
		public void mouseClicked(MouseEvent e) {
			info.arsenalGroup.purchaseEvent.purchase(info);
			if (arsenalPanel != null)
				arsenalPanel.giveupFocus();
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			lblMain.setBorder(selectedBorder);
			arsenalPanel.displayDescription(info.description);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			lblMain.setBorder(emptyBorder);
			arsenalPanel.displayDescription("-");
		}
		public void mousePressed(MouseEvent e) { }
		public void mouseReleased(MouseEvent e) { }
	};
	
	private BufferedImage scale(BufferedImage img) {
		double scaleX = ITEM_SIZE / img.getWidth();
		double scaleY = ITEM_SIZE / img.getHeight();
		return ImageProcess.scale(img, scaleX, scaleY);
	}
}
