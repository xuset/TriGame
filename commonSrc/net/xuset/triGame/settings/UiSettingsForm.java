package net.xuset.triGame.settings;

import net.xuset.tSquare.ui.Axis;
import net.xuset.tSquare.ui.UiComponent;
import net.xuset.tSquare.ui.UiForm;
import net.xuset.tSquare.ui.UiSlider.SliderChange;

public class UiSettingsForm {
	private final UiForm frmMain;
	
	public UiComponent getUiComponent() { return frmMain; }
	
	public UiSettingsForm(Settings settings) {
		this(settings, true);
	}
	
	public UiSettingsForm(Settings settings, boolean showAll) {
		frmMain = new UiForm();
		frmMain.getLayout().setOrientation(Axis.Y_AXIS);
		
		frmMain.getLayout().add(new UiSettingsTouch(settings));
		frmMain.getLayout().add(new UiSettingsSound(settings));
		frmMain.getLayout().add(createUiZoom(settings));
		if (showAll) {
			frmMain.getLayout().add(createGameZoom(settings));
			frmMain.getLayout().add(new UiSettingsWall(settings));
		}
	}
	
	private UiSettingsZoom createUiZoom(final Settings settings) {
		return new UiSettingsZoom("Ui zoom",
				new SliderChange() {

					@Override
					public void onChange(double newPosition) {
						settings.uiZoom = (float) (newPosition * settings.initUiZoom);
					}
			
		}, settings.uiZoom / settings.initUiZoom);
	}
	
	private UiSettingsZoom createGameZoom(final Settings settings) {
		return new UiSettingsZoom("Game zoom",
				new SliderChange() {

					@Override
					public void onChange(double newPosition) {
						settings.blockSize =
								(int) (settings.defaultBlockSize * newPosition);
					}
			
		}, settings.blockSize / (0.0 + settings.defaultBlockSize));
	}
	
	
}
