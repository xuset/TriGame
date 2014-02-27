package net.xuset.tSquare.system.input.mouse;

import java.util.ArrayList;

import net.xuset.tSquare.util.Observer;
import net.xuset.tSquare.util.Observer.Change;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class AndroidMouseListener implements IMouseListener {
	private final ArrayList<TsMouseEvent> queue = new ArrayList<TsMouseEvent>();
	private final Observer<TsMouseEvent> observer = new Observer<TsMouseEvent>();
	
	public AndroidMouseListener(View v) {
		v.setOnTouchListener(new TouchInput());
	}

	@Override
	public TsMouseEvent pollEvent() {
		if (queue.isEmpty())
			return null;
		return queue.remove(0);
	}

	@Override
	public void addEvent(TsMouseEvent e) {
		queue.add(e);
		observer.notifyWatchers(e);
	}

	@Override
	public void clearEvents() {
		queue.clear();
	}

	@Override
	public TsMouseEvent searchForEvent(MouseButton button, MouseAction action) {
		for (int i = 0; i < queue.size(); i++) {
			TsMouseEvent e = queue.get(i);
			if (e.button == button && e.action == action)
				return e;
		}
		return null;
	}

	@Override
	public void watch(Change<TsMouseEvent> watcher) {
		observer.watch(watcher);
	}
	
	private class TouchInput implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			MouseAction action = translateToMouseAction(event.getAction());
			addEvent(new TsMouseEvent(action, MouseButton.LEFT, (int) event.getX(), (int) event.getY()));
			return true;
		}
		
		private MouseAction translateToMouseAction(int action) {
			switch(action) {
			case MotionEvent.ACTION_UP:
				return MouseAction.RELEASE;
			case MotionEvent.ACTION_DOWN:
				return MouseAction.PRESS;
			case MotionEvent.ACTION_MOVE:
				return MouseAction.DRAG;
			}
			return MouseAction.UNKNOWN;
		}
	}

}
