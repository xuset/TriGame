package net.xuset.tSquare.system.input.mouse;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import net.xuset.tSquare.util.Observer;
import net.xuset.tSquare.util.Observer.Change;

public class AwtMouseListener implements IMouseListener {
	private final Observer<TsMouseEvent> observer = new Observer<TsMouseEvent>();
	private final MousePointer mainPointer;
	
	public AwtMouseListener(Component c) {
		attachListener(c);
		mainPointer = new MousePointer(0, 0.0f, 0.0f);
		mainPointer.isPressed = false;
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
	
	private void updatePointer(MouseEvent e, boolean isPressed) {
		mainPointer.x = e.getX();
		mainPointer.y = e.getY();
		mainPointer.isPressed = isPressed;
	}
	
	public class MouseInput implements MouseListener, MouseMotionListener {
		@Override public void mouseClicked(MouseEvent e) { }
		@Override public void mouseEntered(MouseEvent e) { }
		@Override public void mouseExited(MouseEvent e) { }

		@Override
		public void mousePressed(MouseEvent e) {
			updatePointer(e, true);
			observer.notifyWatchers(createEvent(e, MouseAction.PRESS));
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			updatePointer(e, false);
			observer.notifyWatchers(createEvent(e, MouseAction.RELEASE));
		}
		
		@Override
		public void mouseMoved(MouseEvent e) {
			updatePointer(e, false);
			observer.notifyWatchers(createEvent(e, MouseAction.MOVE));
		}
		

		@Override
		public void mouseDragged(MouseEvent e) {
			updatePointer(e, true);
			observer.notifyWatchers(createEvent(e, MouseAction.MOVE));
		}
	}

	@Override
	public void watch(Change<TsMouseEvent> watcher) {
		observer.watch(watcher);
	}

	@Override
	public void clearListeners() {
		observer.unwatchAll();
	}

	@Override
	public int getPointerCount() {
		return 1;
	}

	@Override
	public MousePointer getPointerByIndex(int index) {
		if (index != 0)
			throw new IndexOutOfBoundsException();
		return mainPointer;
	}
	
	@Override
	public MousePointer getPointerById(int id) {
		if (id != mainPointer.getId())
			return null;
		return mainPointer;
	}
	
	private TsMouseEvent createEvent(MouseEvent e, MouseAction action) {
		return new TsMouseEvent(mainPointer, action,
				getMouseButtonByAwtId(e.getButton()),
				e.getX(), e.getY());
	}

	@Override
	public boolean unwatch(Change<TsMouseEvent> watcher) {
		return observer.unwatch(watcher);
	}
}
