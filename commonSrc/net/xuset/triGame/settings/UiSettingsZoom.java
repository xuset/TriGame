package net.xuset.triGame.settings;

import net.xuset.tSquare.ui.Alignment;
import net.xuset.tSquare.ui.Axis;
import net.xuset.tSquare.ui.UiForm;
import net.xuset.tSquare.ui.UiLabel;
import net.xuset.tSquare.ui.UiSlider;
import net.xuset.tSquare.ui.UiSlider.SliderChange;

class UiSettingsZoom extends UiForm {
	private static final double minZoom = 0.5, maxZoom = 2.0;
	
	private final Settings settings;
	private final UiLabel lblZoom = new UiLabel();
	private final UiSlider slider = new UiSlider();
	
	UiSettingsZoom(Settings settings) {
		this.settings = settings;

		getLayout().setAlignment(Axis.Y_AXIS, Alignment.CENTER);
		
		getLayout().add(new UiLabel("Zoom   "));
		getLayout().add(slider);
		getLayout().add(lblZoom);
		
		setInitialPosition();
		slider.setSliderListener(new SliderListener());
		slider.getBorder().setVisibility(true);
		reset();
	}
	
	private void setInitialPosition() {
		double zoom = settings.blockSize / (0.0 + settings.defaultBlockSize);
		double position = (zoom - minZoom) / (maxZoom - minZoom);
		if (position > 1.0)
			position = 1.0;
		slider.setPosition(position);
	}
	
	private void reset() {
		double sliderZoom = minZoom + (maxZoom - minZoom) * slider.getPosition();
		settings.blockSize = (int) (settings.defaultBlockSize * sliderZoom);
		lblZoom.setText(((int) (sliderZoom * 100)) + "%");
	}
	
	private class SliderListener implements SliderChange {
		@Override
		public void onChange(double newPosition) {
			reset();
		}
	}
}
