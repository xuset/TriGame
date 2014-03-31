package net.xuset.tSquare.system.input.keyboard;

import net.xuset.tSquare.util.Observer;
import net.xuset.tSquare.util.Observer.Change;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;

public class AndroidKeyboardListener implements IKeyListener {
	private final Observer<TsKeyEvent> observer = new Observer<TsKeyEvent>();
	
	public AndroidKeyboardListener(View v) {
		v.setOnKeyListener(new AndroidKeyListener());
	}

	@Override
	public void clearListeners() {
		observer.unwatchAll();
	}

	@Override
	public void watch(Change<TsKeyEvent> watcher) {
		observer.watch(watcher);
	}

	@Override
	public boolean unwatch(Change<TsKeyEvent> watcher) {
		return observer.unwatch(watcher);
	}
	
	private class AndroidKeyListener implements OnKeyListener {

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			KeyAction action = null;
			if (event.getAction() == KeyEvent.ACTION_DOWN)
				action = KeyAction.PRESS;
			else if (event.getAction() == KeyEvent.ACTION_UP)
				action = KeyAction.RELEASE;
			if (action != null) {
				TsKeyEvent e = new TsKeyEvent(action, event.getDisplayLabel(), keyCode);
				observer.notifyWatchers(e);
			}
			return false; //TODO change this to true and see if input works?
		}
		
	}

}
