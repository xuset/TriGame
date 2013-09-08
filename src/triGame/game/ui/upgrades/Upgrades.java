package triGame.game.ui.upgrades;

import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import triGame.game.shopping.ShopManager;
import triGame.game.shopping.UpgradeManager;
import triGame.game.ui.JPanelGetter;

public class Upgrades implements JPanelGetter{
	public UpgradePanel panel;
	
	public JPanel getJPanel() { return panel; }
	
	public Upgrades(ShopManager shop) {
		panel = new UpgradePanel(shop);
	}
	
	public void set(UpgradeManager manager, BufferedImage image) {
		panel.set(manager, image);
	}
	
	public void set(UpgradeManager manager, String text) {
		panel.set(manager, text);
	}
}
