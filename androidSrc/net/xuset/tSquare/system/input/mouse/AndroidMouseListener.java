package net.xuset.tSquare.system.input.mouse;

import java.util.ArrayList;
import java.util.Iterator;

import net.xuset.tSquare.util.Observer;
import net.xuset.tSquare.util.Observer.Change;

import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class AndroidMouseListener implements IMouseListener {
	private final Observer<TsMouseEvent> observer = new Observer<TsMouseEvent>();
	private final ArrayList<MousePointer> pointers = new ArrayList<MousePointer>();
	
	public AndroidMouseListener(View v) {
		v.setOnTouchListener(new TouchInput());
	}

	@Override
	public void watch(Change<TsMouseEvent> watcher) {
		observer.watch(watcher);
	}

	@Override
	public boolean unwatch(Change<TsMouseEvent> watcher) {
		return observer.unwatch(watcher);
	}

	@Override
	public void clearListeners() {
		observer.unwatchAll();
	}
	
	private void updatePointerById(int id, float x, float y) {
		synchronized(pointers) {
			for (MousePointer mp : pointers) {
				if (mp.getId() == id) {
					mp.x = x;
					mp.y = y;
				}
			}
		}
	}
	
	private void updatePointers(MotionEvent event) {
		for (int i = 0; i < event.getPointerCount(); i++) {
			int id = MotionEventCompat.getPointerId(event, i);
			float x = MotionEventCompat.getX(event, i);
			float y = MotionEventCompat.getY(event, i);
			updatePointerById(id, x, y);
		}
	}
	
	private void removePointer(int id) {
		synchronized(pointers) {
			for (Iterator<MousePointer> it = pointers.iterator(); it.hasNext(); ) {
				MousePointer mp = it.next();
				if (mp.getId() == id) {
					mp.isPressed = false;
					it.remove();
					startEvent(mp, MouseAction.RELEASE, MouseButton.LEFT);
					return;
				}
			}
		}
	}
	
	private void startEvent(MousePointer mp, MouseAction action, MouseButton button) {
		observer.notifyWatchers(new TsMouseEvent(
				mp, action, button, (int) mp.x, (int) mp.y));
	}
	
	private void removeAllPointers() {
		synchronized(pointers) {
			for (MousePointer mp : pointers) {
				mp.isPressed = false;
			}
			pointers.clear();
		}
	}
	
	private void addPointer(MotionEvent event, int pointerIndex, int pointerId) {
		float x = MotionEventCompat.getX(event, pointerIndex);
		float y = MotionEventCompat.getY(event, pointerIndex);
		MousePointer mp = new MousePointer(pointerId, x, y);
		
		synchronized(pointers) {
			pointers.add(mp);
		}
		startEvent(mp, MouseAction.PRESS, MouseButton.LEFT);
	}
	
	private void sendMoveEvent(int pointerId) {
		MousePointer mp = getPointerById(pointerId);
		startEvent(mp, MouseAction.MOVE, MouseButton.UNKNOWN);
	}
	
	private class TouchInput implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			updatePointers(event);
			
			int pointerIndex = MotionEventCompat.getActionIndex(event);
			int pointerAction = MotionEventCompat.getActionMasked(event);
			int pointerId = MotionEventCompat.getPointerId(event, pointerIndex);
			
			switch (pointerAction) {
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_POINTER_DOWN:
				addPointer(event, pointerIndex, pointerId);
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
				removePointer(pointerId);
				break;
			case MotionEvent.ACTION_MOVE:
				sendMoveEvent(pointerId);
				break;
			case MotionEvent.ACTION_CANCEL:
				removeAllPointers();
				break;
			}
			
			return true;
		}
	}

	@Override
	public int getPointerCount() {
		return pointers.size();
	}

	@Override
	public MousePointer getPointerByIndex(int index) {
		//TODO an ArrayIndexOutOfBounds may occur here if the caller calls right after
		//a pointer was remove the list.
		synchronized(pointers) {
			return pointers.get(index);
		}
	}

	@Override
	public MousePointer getPointerById(int id) {
		synchronized(pointers) {
			for (MousePointer mp : pointers) {
				if (mp.getId() == id)
					return mp;
			}
		}
		return null;
	}

}
