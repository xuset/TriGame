package net.xuset.triGame.settings;

import net.xuset.tSquare.ui.Alignment;
import net.xuset.tSquare.ui.Axis;
import net.xuset.tSquare.ui.UiForm;
import net.xuset.tSquare.ui.UiLabel;
import net.xuset.tSquare.ui.UiSlider;
import net.xuset.tSquare.ui.UiSlider.SliderChange;

class UiSettingsZoom extends UiForm {
	private static final double minZoom = 0.5, maxZoom = 2.0;
	
	private final UiLabel lblZoom = new UiLabel();
	private final UiSlider slider = new UiSlider();
	private final SliderChange listener;
	
	UiSettingsZoom(String name, SliderChange sliderListener, double init) {

		this.listener = sliderListener;
		
		getLayout().setAlignment(Axis.Y_AXIS, Alignment.CENTER);
		
		getLayout().add(new UiLabel(name + "   "));
		getLayout().add(slider);
		getLayout().add(lblZoom);
		
		setInitialPosition(init);
		slider.setSliderListener(new SliderListener());
		slider.getBorder().setVisibility(true);
		reset();
	}
	
	private void setInitialPosition(double init) {
		double position = (init - minZoom) / (maxZoom - minZoom);
		if (position > 1.0)
			position = 1.0;
		if (position < 0.0)
			position = 0.0;
		slider.setProgress(position);
	}
	
	private void reset() {
		double sliderZoom = minZoom + (maxZoom - minZoom) * slider.getProgress();
		lblZoom.setText(((int) (sliderZoom * 100)) + "%");
		listener.onChange(sliderZoom);
	}
	
	private class SliderListener implements SliderChange {
		@Override
		public void onChange(double newPosition) {
			reset();
		}
	}
}
