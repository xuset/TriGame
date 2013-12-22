package triGame.game.ui.upgrades;

import java.awt.Image;

import javax.swing.JPanel;

import triGame.game.shopping.ShopManager;
import triGame.game.shopping.UpgradeManager;
import triGame.game.ui.FocusSurrender;
import triGame.game.ui.JPanelGetter;

public class Upgrades implements JPanelGetter{
	public UpgradePanel panel;
	
	public JPanel getJPanel() { return panel; }
	
	public Upgrades(ShopManager shop, FocusSurrender focus) {
		panel = new UpgradePanel(shop, focus);
	}
	
	public void set(UpgradeManager manager, Image image) {
		panel.set(manager, image);
	}
	
	public void set(UpgradeManager manager, String text) {
		panel.set(manager, text);
	}
}
