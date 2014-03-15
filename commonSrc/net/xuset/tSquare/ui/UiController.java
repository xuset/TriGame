package net.xuset.tSquare.ui;

import java.util.ArrayList;

import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.ScaledGraphics;
import net.xuset.tSquare.math.rect.IRectangleR;
import net.xuset.tSquare.system.input.mouse.IMouseListener;
import net.xuset.tSquare.system.input.mouse.TsMouseEvent;
import net.xuset.tSquare.ui.layout.UiMainForm;
import net.xuset.tSquare.util.Observer;
import net.xuset.tSquare.util.Observer.Change;

public class UiController {
	private final ArrayList<TsMouseEvent> mouseQueue = new ArrayList<TsMouseEvent>();
	private final ArrayList<Observer.Change<TsMouseEvent>> observers;
	private final UiForm mainForm;
	private final UiPopupController popupController = new UiPopupController();
	private double lastWidth = 0.0, lastHeight = 0.0;
	private float scale = 1.0f;
	
	public void setScale(float scale) { this.scale = scale; }
	public float getScale() { return scale; }
	
	public UiPopupController getPopupController() { return popupController; }
	
	public UiForm getForm() { return mainForm; }
	
	public UiController() {
		observers = new ArrayList<Observer.Change<TsMouseEvent>>();
		mainForm = new UiMainForm();
	}
	
	public UiController(IMouseListener mouseListener) {
		this();
		mouseListener.watch(new MouseObserver());
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
				float scaleX = t.x / scale, scaleY = t.y / scale;
				TsMouseEvent scaledEvent = new TsMouseEvent(t.action, t.button,
						(int) scaleX, (int) scaleY);
				
				if (popupController.contains(scaleX, scaleY)) {
					popupController.recieveMouseEvent(scaledEvent, scaleX, scaleY);
				} else {
					UiForm f = getForm();
					if (f.contains(scaleX, scaleY)) {
						float rx = scaleX - f.getX();
						float ry = scaleY - f.getY();
						f.recieveMouseEvent(scaledEvent, rx, ry);
					}
				}
				
				for (Observer.Change<TsMouseEvent> c : observers)
					c.observeChange(scaledEvent);
			}
			mouseQueue.clear();
		}
	}
	
	public void watchForMouse(Observer.Change<TsMouseEvent> listener) {
		observers.add(listener);
	}
	
	public void unwatchForMouse(Observer.Change<TsMouseEvent> listener) {
		observers.remove(listener);
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
