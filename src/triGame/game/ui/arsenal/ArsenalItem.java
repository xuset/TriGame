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

import tSquare.imaging.ImageProccess;
import tSquare.util.Observer;
import triGame.game.shopping.ShopItem;
import triGame.game.shopping.ShopManager;
import triGame.game.shopping.UpgradeManager;

public class ArsenalItem extends JPanel{
	public static final int ITEM_SIZE = 50;
	private static final long serialVersionUID = 8955177654417376268L;
	private static final Border selectedBorder = BorderFactory.createLineBorder(Color.black, 1);
	private static final Border emptyBorder = BorderFactory.createEmptyBorder(1, 1, 1, 1);
	
	JLabel lblPrice;
	JLabel lblMain;
	ArsenalPanel arsenalPanel = null;
	
	ArsenalItemInfo info = new ArsenalItemInfo();
	
	public boolean visibile = true;
	
	private void construct(ShopItem item, JLabel main) {
		this.setSize(ITEM_SIZE, ITEM_SIZE);
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
		main.setBorder(emptyBorder);
		main.addMouseListener(lblMainEvent);
	}
	
	public ArsenalItem(ShopItem item, BufferedImage image, int radius) {
		this(item, image);
		info.hasRadius = true;
		info.radius = radius;
	}
	
	public ArsenalItem(ShopItem item, BufferedImage image) {
		lblMain = new  JLabel();
		lblMain.setIcon(new ImageIcon(scale(image)));
		info.isImagge = true;
		info.image = image;
		construct(item, lblMain);
	}
	
	public ArsenalItem(ShopItem item, String text, UpgradeManager upgradeManager) {
		info.upgradeManager = upgradeManager;
		info.text = text;
		lblMain = new JLabel(text, JLabel.CENTER);
		construct(item, lblMain);
	}
	
	Observer.Change<ShopManager> observeShopPoints = new Observer.Change<ShopManager>() {
		@Override
		public void observeChange(ShopManager t) {
			if (t.points < info.shopItem.getCost())
				lblPrice.setForeground(Color.red);
			else
				lblPrice.setForeground(Color.black);
		}
	};

	private MouseListener lblMainEvent = new MouseListener() {
		@Override
		public void mouseClicked(MouseEvent e) {
			info.arsenalGroup.purchaseEvent.purchase(info);
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
		return ImageProccess.scale(img, scaleX, scaleY);
	}
}
