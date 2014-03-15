package net.xuset.tSquare.system.input.keyboard;

import java.util.ArrayList;

import net.xuset.tSquare.util.Observer;
import net.xuset.tSquare.util.Observer.Change;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;

public class AndroidKeyboardListener implements IKeyListener {
	private final ArrayList<TsKeyEvent> queue = new ArrayList<TsKeyEvent>();
	private final Observer<TsKeyEvent> observer = new Observer<TsKeyEvent>();
	
	public AndroidKeyboardListener(View v) {
		v.setOnKeyListener(new AndroidKeyListener());
	}

	@Override
	public TsKeyEvent pollEvent() {
		if (queue.isEmpty())
			return null;
		return queue.remove(0);
	}

	@Override
	public void addEvent(TsKeyEvent e) {
		queue.add(e);
		observer.notifyWatchers(e);
	}

	@Override
	public void clearEvents() {
		queue.clear();
	}

	@Override
	public TsKeyEvent searchForEvent(int keyId, KeyAction action) {
		for (int i = 0; i < queue.size(); i++) {
			TsKeyEvent e = queue.get(i);
			if (e.action == action && e.keyId == keyId)
				return e;
		}
		return null;
	}
	
	//TODO finish AndroidKeyboardListener implementations

	@Override
	public TsKeyEvent searchForEvent(char key, KeyAction action) {
		for (int i = 0; i < queue.size(); i++) {
			TsKeyEvent e = queue.get(i);
			if (e.action == action && e.key == key)
				return e;
		}
		return null;
	}@Override
	public boolean isUpPressed() {
		return searchForEvent(KeyEvent.KEYCODE_DPAD_UP, KeyAction.PRESS) != null ||
				searchForEvent(KeyEvent.KEYCODE_W, KeyAction.PRESS) != null;
	}

	@Override
	public boolean isDownPressed() {
		return searchForEvent(KeyEvent.KEYCODE_DPAD_DOWN, KeyAction.PRESS) != null ||
				searchForEvent(KeyEvent.KEYCODE_S, KeyAction.PRESS) != null;
	}

	@Override
	public boolean isLeftPressed() {
		return searchForEvent(KeyEvent.KEYCODE_DPAD_LEFT, KeyAction.PRESS) != null ||
				searchForEvent(KeyEvent.KEYCODE_A, KeyAction.PRESS) != null;
	}

	@Override
	public boolean isSpacePressed() {
		return searchForEvent(KeyEvent.KEYCODE_SPACE, KeyAction.PRESS) != null;
	}

	@Override
	public boolean isEnterPressed() {
		return searchForEvent(KeyEvent.KEYCODE_ENTER, KeyAction.PRESS) != null;
	}

	@Override
	public boolean isRightPressed() {
		return searchForEvent(KeyEvent.KEYCODE_DPAD_RIGHT, KeyAction.PRESS) != null ||
				searchForEvent(KeyEvent.KEYCODE_D, KeyAction.PRESS) != null;
	}

	@Override
	public void clearListeners() {
		observer.unwatchAll();
	}
	
	private class AndroidKeyListener implements OnKeyListener {

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			KeyAction action = null;
			if (event.getAction() == KeyEvent.ACTION_DOWN)
				action = KeyAction.PRESS;
			else if (event.getAction() == KeyEvent.ACTION_UP)
				action = KeyAction.RELEASE;
			if (action != null)
				addEvent(new TsKeyEvent(action, event.getDisplayLabel(), keyCode));
			return false;
		}
		
	}

	@Override
	public void watch(Change<TsKeyEvent> watcher) {
		observer.watch(watcher);
	}

}
