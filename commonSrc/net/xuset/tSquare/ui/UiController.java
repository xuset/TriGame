package net.xuset.tSquare.ui;

import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.math.rect.IRectangleR;
import net.xuset.tSquare.system.input.mouse.IMouseListener;
import net.xuset.tSquare.system.input.mouse.TsMouseEvent;
import net.xuset.tSquare.ui.layout.UiMainForm;
import net.xuset.tSquare.util.Observer.Change;

public class UiController {
	private final UiForm mainForm;
	private double lastWidth = 0.0, lastHeight = 0.0;
	
	public UiForm getForm() { return mainForm; }
	
	public UiController() {
		mainForm = new UiMainForm();
	}
	
	public UiController(IMouseListener mouseListener) {
		mainForm = new UiMainForm();
		mouseListener.watch(new MouseObserver());
	}
	
	public void draw(IGraphics g) {
		IRectangleR view = g.getView();
		if (view.getWidth() != lastWidth || view.getHeight() != lastHeight) {
			lastWidth = view.getWidth();
			lastHeight = view.getHeight();
			mainForm.setSize((float) lastWidth, (float) lastHeight);
		}
		mainForm.draw(g);
	}
	
	private class MouseObserver implements Change<TsMouseEvent> {

		@Override
		public void observeChange(TsMouseEvent t) {
			UiForm f = getForm();
			if (f.contains(t.x, t.y)) {
				float rx = t.x - f.getX();
				float ry = t.y - f.getY();
				f.recieveMouseEvent(t, rx, ry);
			}
		}
		
	}
}
