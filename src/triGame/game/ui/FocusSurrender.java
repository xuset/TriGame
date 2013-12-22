package triGame.game.ui;

import java.awt.Component;

public class FocusSurrender {
	private final Component focusGainer;
	
	public FocusSurrender(Component focusGainer) {
		this.focusGainer = focusGainer;
	}
	
	public void surrenderFocus() {
		focusGainer.requestFocus();
	}
}
