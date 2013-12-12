package triGame.game.ui;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;

import tSquare.game.DrawBoard;
import tSquare.system.Display;
import tSquare.system.PeripheralInput;
import triGame.game.ManagerService;
import triGame.game.shopping.ShopManager;
import triGame.game.ui.arsenal.Arsenal;
import triGame.game.ui.upgrades.Upgrades;

public class UserInterface {
	private final JPanel pnlMain = new JPanel();
	private final Component mainFocus;
	private JPanelGetter selectedJPanel;
	
	public final Arsenal arsenal;
	public final Upgrades upgrades;
	public final Attacher attacher;
	
	public UserInterface(Display display, DrawBoard drawBoard, ManagerService managers, ShopManager shop,
			PeripheralInput.Mouse mouse) {
		
		mainFocus = drawBoard;
		pnlMain.setSize(display.getWidth(), 70);
		attacher = new Attacher(managers, this, shop, mouse);
		arsenal = new Arsenal(this, shop, attacher);
		upgrades = new Upgrades(shop, this);
		display.add(pnlMain, BorderLayout.PAGE_END);
		switchTo(arsenal);
	}
	
	public void switchTo(JPanelGetter getter) {
		pnlMain.removeAll();
		pnlMain.add(getter.getJPanel());
		//pnlMain.repaint();
		pnlMain.updateUI();
		selectedJPanel = getter;
	}
	
	public boolean isSelected(JPanelGetter getter) {
		if (selectedJPanel == getter)
			return true;
		return false;
	}
	
	public void giveupFocus() {
		mainFocus.requestFocus();
	}
}
