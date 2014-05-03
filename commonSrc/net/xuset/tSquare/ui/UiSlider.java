package net.xuset.tSquare.ui;

import net.xuset.tSquare.system.input.mouse.TsMouseEvent;

public class UiSlider extends UiProgressBar {
	public static interface SliderChange { void onChange(double newPosition); }
	
	private SliderChange listener = null;

	public UiSlider() {
		super(100, 20);
	}
	
	public void setSliderListener(SliderChange listener) { this.listener = listener; }
	public SliderChange getSliderListener() { return listener; }

	@Override
	protected void recieveMouseEvent(TsMouseEvent e, float x, float y) {
		super.recieveMouseEvent(e, x, y);
		
		if (e.pointer.isPressed()) {
			int seg = 30;
			double newProg = x / getWidth() + 0.5 / seg;
			newProg = Math.floor(newProg * seg) / seg;
			setProgress(newProg);
			if (listener != null)
				listener.onChange(getProgress());
		}
	}
}
