package triGame.game.ui;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import triGame.game.TriGame;
import triGame.game.ui.arsenal.Arsenal;
import triGame.game.ui.upgrades.Upgrades;

public class UserInterface {
	private JPanel pnlMain = new JPanel();
	private JPanelGetter getter;
	
	public Arsenal arsenal;
	public Upgrades upgrades;
	public Attacher attacher;
	
	public UserInterface(TriGame game) {
		pnlMain.setSize(game.getDisplay().getWidth(), 70);
		attacher = new Attacher(game, this);
		arsenal = new Arsenal(this, game.shop, attacher, game.gunManager);
		upgrades = new Upgrades(game.shop);
		game.getDisplay().add(pnlMain, BorderLayout.PAGE_END);
		switchTo(arsenal);
		game.getDisplay().pack();
	}
	
	public void switchTo(JPanelGetter getter) {
		pnlMain.removeAll();
		pnlMain.add(getter.getJPanel());
		//pnlMain.repaint();
		pnlMain.updateUI();
		this.getter = getter;
	}
	
	public boolean isSelected(JPanelGetter getter) {
		if (this.getter == getter)
			return true;
		return false;
	}
}
