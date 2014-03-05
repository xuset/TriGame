package net.xuset.triGame.settings;

import net.xuset.tSquare.ui.Axis;
import net.xuset.tSquare.ui.UiComponent;
import net.xuset.tSquare.ui.UiForm;

public class UiSettingsForm {
	private final UiForm frmMain;
	
	public UiComponent getUiComponent() { return frmMain; }
	
	public UiSettingsForm(Settings settings) {
		frmMain = new UiForm();
		frmMain.getLayout().setOrientation(Axis.Y_AXIS);
		
		frmMain.getLayout().add(new UiSettingsTouch(settings));
		frmMain.getLayout().add(new UiSettingsSound(settings));
		frmMain.getLayout().add(new UiSettingsZoom(settings));
	}
	
	
}
