package net.xuset.tSquare.system.input.keyboard;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import net.xuset.tSquare.util.Observer;
import net.xuset.tSquare.util.Observer.Change;

public class AwtKeyListener implements IKeyListener {
	private final ArrayList<TsKeyEvent> queue = new ArrayList<TsKeyEvent>();
	private final Observer<TsKeyEvent> observer = new Observer<TsKeyEvent>();

	//TODO maybe think about a fixed size queue... ponder it...
	
	public AwtKeyListener(Component c) {
		attachListener(c);
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

	@Override
	public TsKeyEvent searchForEvent(char key, KeyAction action) {
		for (int i = 0; i < queue.size(); i++) {
			TsKeyEvent e = queue.get(i);
			if (e.action == action && e.key == key)
				return e;
		}
		return null;
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
	public boolean isUpPressed() {
		return searchForEvent(KeyEvent.VK_UP, KeyAction.PRESS) != null ||
				searchForEvent(KeyEvent.VK_W, KeyAction.PRESS) != null;
	}

	@Override
	public boolean isDownPressed() {
		return searchForEvent(KeyEvent.VK_DOWN, KeyAction.PRESS) != null ||
				searchForEvent(KeyEvent.VK_S, KeyAction.PRESS) != null;
	}

	@Override
	public boolean isLeftPressed() {
		return searchForEvent(KeyEvent.VK_LEFT, KeyAction.PRESS) != null ||
				searchForEvent(KeyEvent.VK_A, KeyAction.PRESS) != null;
	}

	@Override
	public boolean isSpacePressed() {
		return searchForEvent(KeyEvent.VK_SPACE, KeyAction.PRESS) != null;
	}

	@Override
	public boolean isEnterPressed() {
		return searchForEvent(KeyEvent.VK_ENTER, KeyAction.PRESS) != null;
	}

	@Override
	public boolean isRightPressed() {
		return searchForEvent(KeyEvent.VK_RIGHT, KeyAction.PRESS) != null ||
				searchForEvent(KeyEvent.VK_D, KeyAction.PRESS) != null;
	}
	
	public void attachListener(Component c) {
		c.addKeyListener(new KeyboardInput());
	}
	
	public class KeyboardInput implements KeyListener {
		@Override public void keyTyped(KeyEvent e) { }

		@Override
		public void keyPressed(KeyEvent e) {
			addEvent(new TsKeyEvent(KeyAction.PRESS, e.getKeyChar(), e.getKeyCode()));
		}

		@Override
		public void keyReleased(KeyEvent e) {
			addEvent(new TsKeyEvent(KeyAction.RELEASE, e.getKeyChar(), e.getKeyCode()));
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

}
