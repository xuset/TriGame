package net.xuset.triGame.settings;

import net.xuset.tSquare.ui.UiCheckBox;
import net.xuset.tSquare.ui.UiCheckBox.UiCheckChange;
import net.xuset.tSquare.ui.Alignment;
import net.xuset.tSquare.ui.Axis;
import net.xuset.tSquare.ui.UiForm;
import net.xuset.tSquare.ui.UiLabel;

class UiSettingsSound extends UiForm {
	private final Settings settings;
	private final UiCheckBox checkBox = new UiCheckBox();
	
	public UiSettingsSound(Settings settings) {
		this.settings = settings;
		checkBox.setChecked(settings.enableSound);
		checkBox.setCheckChange(new CheckListener());
		checkBox.getBorder().setVisibility(true);
		
		getLayout().setAlignment(Axis.Y_AXIS, Alignment.CENTER);
		getLayout().add(checkBox);
		getLayout().add(new UiLabel("   enable sound"));
	}
	
	private class CheckListener implements UiCheckChange {

		@Override
		public void onChange(boolean newVal) {
			settings.enableSound = newVal;
		}
		
	}

}
