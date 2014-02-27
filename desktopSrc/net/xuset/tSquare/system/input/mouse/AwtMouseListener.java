package net.xuset.tSquare.system.input.mouse;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import net.xuset.tSquare.util.Observer;
import net.xuset.tSquare.util.Observer.Change;

public class AwtMouseListener implements IMouseListener {
	private final ArrayList<TsMouseEvent> queue = new ArrayList<TsMouseEvent>();
	private final Observer<TsMouseEvent> observer = new Observer<TsMouseEvent>();
	
	public AwtMouseListener(Component c) {
		attachListener(c);
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
	
	public void attachListener(Component c) {
		c.addMouseListener(new MouseInput());
		c.addMouseMotionListener(new MouseInput());
	}
	
	private MouseButton getMouseButtonByAwtId(int id) {
		switch(id) {
		case MouseEvent.BUTTON1:
			return MouseButton.LEFT;
		case MouseEvent.BUTTON2:
			return MouseButton.MIDDLE;
		case MouseEvent.BUTTON3:
			return MouseButton.RIGHT;
		}
		
		return MouseButton.UNKNOWN;
	}
	
	public class MouseInput implements MouseListener, MouseMotionListener {
		@Override public void mouseClicked(MouseEvent e) { }
		@Override public void mouseEntered(MouseEvent e) { }
		@Override public void mouseExited(MouseEvent e) { }

		@Override
		public void mousePressed(MouseEvent e) {
			MouseButton btn = getMouseButtonByAwtId(e.getButton());
			addEvent(new TsMouseEvent(MouseAction.PRESS, btn, e.getX(), e.getY()));
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			MouseButton btn = getMouseButtonByAwtId(e.getButton());
			addEvent(new TsMouseEvent(MouseAction.RELEASE, btn, e.getX(), e.getY()));
		}
		
		@Override
		public void mouseMoved(MouseEvent e) {
			addEvent(new TsMouseEvent(MouseAction.MOVE, MouseButton.UNKNOWN, e.getX(), e.getY()));
		}
		

		@Override
		public void mouseDragged(MouseEvent e) {
			MouseButton button = getMouseButtonByAwtId(e.getButton());
			addEvent(new TsMouseEvent(MouseAction.DRAG, button, e.getX(), e.getY()));
		}
	}

	@Override
	public void watch(Change<TsMouseEvent> watcher) {
		observer.watch(watcher);
	}
}
