package net.xuset.tSquare.system.input.keyboard;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import net.xuset.tSquare.util.Observer;
import net.xuset.tSquare.util.Observer.Change;

public class AwtKeyListener implements IKeyListener {
	private final Observer<TsKeyEvent> observer = new Observer<TsKeyEvent>();

	public AwtKeyListener(Component c) {
		attachListener(c);
	}
	
	public void attachListener(Component c) {
		c.addKeyListener(new KeyboardInput());
	}
	
	public class KeyboardInput implements KeyListener {
		@Override public void keyTyped(KeyEvent e) { }

		@Override
		public void keyPressed(KeyEvent e) {
			observer.notifyWatchers(
					new TsKeyEvent(KeyAction.PRESS, e.getKeyChar(), e.getKeyCode()));
			
		}

		@Override
		public void keyReleased(KeyEvent e) {
			observer.notifyWatchers(
					new TsKeyEvent(KeyAction.RELEASE, e.getKeyChar(), e.getKeyCode()));
		}
	}

	@Override
	public void watch(Change<TsKeyEvent> watcher) {
		observer.watch(watcher);
	}

	@Override
	public void clearListeners() {
		observer.unwatchAll();
	}

	@Override
	public boolean unwatch(Change<TsKeyEvent> watcher) {
		return observer.unwatch(watcher);
	}

}
