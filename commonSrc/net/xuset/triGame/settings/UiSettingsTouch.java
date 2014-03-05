package net.xuset.triGame.settings;

import net.xuset.tSquare.ui.UiCheckBox;
import net.xuset.tSquare.ui.UiCheckBox.UiCheckChange;
import net.xuset.tSquare.ui.Alignment;
import net.xuset.tSquare.ui.Axis;
import net.xuset.tSquare.ui.UiForm;
import net.xuset.tSquare.ui.UiLabel;

class UiSettingsTouch extends UiForm{
	private final Settings settings;
	private final UiCheckBox checkBox = new UiCheckBox();
	
	public UiSettingsTouch(Settings settings) {
		this.settings = settings;
		checkBox.setChecked(settings.drawUiTouch);
		checkBox.setCheckChange(new CheckListener());
		checkBox.getBorder().setVisibility(true);
		
		getLayout().setAlignment(Axis.Y_AXIS, Alignment.CENTER);
		getLayout().add(checkBox);
		getLayout().add(new UiLabel("  enable touch controls"));
	}
	
	private class CheckListener implements UiCheckChange {

		@Override
		public void onChange(boolean newVal) {
			settings.drawUiTouch = newVal;
		}
	}
	
}
