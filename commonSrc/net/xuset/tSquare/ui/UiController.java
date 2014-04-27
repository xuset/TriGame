package net.xuset.tSquare.ui;

import java.util.ArrayList;

import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.ScaledGraphics;
import net.xuset.tSquare.math.rect.IRectangleR;
import net.xuset.tSquare.system.input.mouse.IMouseListener;
import net.xuset.tSquare.system.input.mouse.ScaledMouseListener;
import net.xuset.tSquare.system.input.mouse.TsMouseEvent;
import net.xuset.tSquare.ui.layout.UiMainForm;
import net.xuset.tSquare.util.Observer.Change;

public class UiController {
	private final ArrayList<TsMouseEvent> mouseQueue = new ArrayList<TsMouseEvent>();
	private final UiForm mainForm;
	private final UiPopupController popupController = new UiPopupController();
	private ScaledMouseListener mouseListener;
	private double lastWidth = 0.0, lastHeight = 0.0;
	private float scale = 1.0f;
	
	public float getScale() { return scale; }
	
	public UiPopupController getPopupController() { return popupController; }
	
	public UiForm getForm() { return mainForm; }
	
	public UiController() {
		mainForm = new UiMainForm();
	}
	
	public UiController(IMouseListener mouseListener) {
		this.mouseListener = new ScaledMouseListener(mouseListener, scale);
		mainForm = new UiMainForm();
		this.mouseListener.watch(new MouseObserver());
	}
	
	public void setScale(float scale) {
		this.scale = scale;
		mouseListener.setScale(scale);
	}
	
	public void draw(IGraphics g) {
		dispatchRecievedInputEvents();
		g = new ScaledGraphics(g, scale, false);
		IRectangleR view = g.getView();
		if (view.getWidth() != lastWidth || view.getHeight() != lastHeight) {
			lastWidth = view.getWidth();
			lastHeight = view.getHeight();
			mainForm.setSize((float) lastWidth, (float) lastHeight);
		}
		mainForm.draw(g);
		popupController.draw(g);
	}
	
	public void dispatchRecievedInputEvents() {
		synchronized(mouseQueue) {
			for (TsMouseEvent t : mouseQueue) {
				if (popupController.contains(t.x, t.y)) {
					popupController.recieveMouseEvent(t, t.x, t.y);
				} else {
					UiForm f = getForm();
					if (f.contains(t.x, t.y)) {
						float rx = t.x - f.getX();
						float ry = t.y - f.getY();
						f.recieveMouseEvent(t, rx, ry);
					}
				}
			}
			mouseQueue.clear();
		}
	}
	
	public void unwatchMouseEvents() {
		mouseListener.unwatchSource();
	}
	
	private class MouseObserver implements Change<TsMouseEvent> {

		@Override
		public void observeChange(TsMouseEvent t) {
			synchronized(mouseQueue) {
				mouseQueue.add(t);
			}
		}
		
	}
}
