package net.xuset.triGame.game.ui;

import net.xuset.tSquare.imaging.TsColor;
import net.xuset.tSquare.ui.UiForm;

public class BaseForm extends UiForm {
	public BaseForm() {
		setOpaque(true);
		setBackground(new TsColor(230, 230, 230));
		getBorder().setVisibility(true);
	}
}
