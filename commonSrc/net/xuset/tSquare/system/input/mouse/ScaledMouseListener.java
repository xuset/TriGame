package net.xuset.tSquare.system.input.mouse;

import java.util.ArrayList;

import net.xuset.tSquare.util.Observer.Change;

public class ScaledMouseListener implements IMouseListener{
	private final MouseObserver mouseObserver = new MouseObserver();
	private final IMouseListener mouseListener;
	private final ArrayList<Change<TsMouseEvent>> listeners =
			new ArrayList<Change<TsMouseEvent>>();
	
	private float scale;
	
	public ScaledMouseListener(IMouseListener mouseListener, float scale) {
		this.mouseListener = mouseListener;
		this.scale = scale;
		mouseListener.watch(mouseObserver);
	}
	
	public float getScale() {
		return scale;
	}
	
	public void setScale(float scale) {
		this.scale = scale;
	}
	
	public void unwatchSource() {
		mouseListener.unwatch(mouseObserver);
	}

	@Override
	public void clearListeners() {
		mouseListener.clearListeners();
		listeners.clear();
	}

	@Override
	public void watch(Change<TsMouseEvent> watcher) {
		listeners.add(watcher);
	}

	@Override
	public boolean unwatch(Change<TsMouseEvent> watcher) {
		return listeners.remove(watcher);
	}

	@Override
	public int getPointerCount() {
		return mouseListener.getPointerCount();
	}

	@Override
	public MousePointer getPointerByIndex(int index) {
		return createScaledPointer(mouseListener.getPointerByIndex(index));
	}

	@Override
	public MousePointer getPointerById(int id) {
		return createScaledPointer(mouseListener.getPointerById(id));
	}
	
	private TsMouseEvent createScaledEvent(TsMouseEvent old) {
		return new TsMouseEvent(
				createScaledPointer(old.pointer),
				old.action, old.button,
				(int) (old.x / scale), 
				(int) (old.y / scale) );
		
	}
	
	private MousePointer createScaledPointer(MousePointer old) {
		return new ScaledMousePointer(old, scale);
	}
	
	private static class ScaledMousePointer extends MousePointer{
		private final MousePointer mp;
		private final float scale;
		
		public ScaledMousePointer(MousePointer mp, float scale) {
			super(mp.getId(), mp.getX(), mp.getY());
			this.mp = mp;
			this.scale = scale;
		}
		
		@Override
		public float getX() {
			return mp.getX() / scale;
		}
		
		@Override
		public float getY() {
			return mp.getY() / scale;
		}
		
		@Override
		public boolean isPressed() {
			return mp.isPressed();
		}
	}

	private class MouseObserver implements Change<TsMouseEvent> {

		@Override
		public void observeChange(TsMouseEvent t) {
			TsMouseEvent scaled = createScaledEvent(t);
			for (Change<TsMouseEvent> c : listeners) {
				c.observeChange(scaled);
			}
			
		}
		
	}

}
