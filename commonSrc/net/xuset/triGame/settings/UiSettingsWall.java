package net.xuset.triGame.settings;

import net.xuset.tSquare.ui.UiForm;
import net.xuset.tSquare.ui.UiLabel;
import net.xuset.tSquare.ui.UiSlider;

public class UiSettingsWall extends UiForm {
	private static final double minPercent = 0.0;
	private static final double maxPercent = 10.0;
	
	private final Settings settings;
	private final UiLabel lblPercent = new UiLabel("100%");
	
	public UiSettingsWall(Settings settings) {
		this.settings = settings;
		UiSlider slider = new UiSlider();
		setSlider(slider, settings.wallGenCoefficient);
		slider.setSliderListener(new SliderListener());
		getLayout().add(new UiLabel("Random walls"));
		getLayout().add(slider);
		getLayout().add(lblPercent);
	}
	
	private void setSlider(UiSlider slider, double percent) {
		double range = maxPercent - minPercent;
		double offset = percent / range;
		offset = Math.min(1.0, Math.max(0, offset)); //limit to value between 0.0 and 1.0
		slider.setPosition(offset);
	}
	
	private class SliderListener implements UiSlider.SliderChange {

		@Override
		public void onChange(double newPosition) {
			double range = maxPercent - minPercent;
			double offset = range * newPosition;
			
			settings.wallGenCoefficient = offset;
			lblPercent.setText((int) (offset * 100) + "%");
		}
		
	}
}
