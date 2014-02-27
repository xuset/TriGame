package net.xuset.tSquare.system.input;

import java.awt.Component;

import net.xuset.tSquare.system.input.keyboard.AwtKeyListener;
import net.xuset.tSquare.system.input.keyboard.IKeyListener;
import net.xuset.tSquare.system.input.mouse.AwtMouseListener;
import net.xuset.tSquare.system.input.mouse.IMouseListener;

public class AwtInputHolder implements InputHolder {
	private final AwtKeyListener keyListener;
	private final AwtMouseListener mouseListener;
	
	public AwtInputHolder(Component c) {
		keyListener = new AwtKeyListener(c);
		mouseListener = new AwtMouseListener(c);
	}

	@Override
	public IKeyListener getKeyboard() {
		return keyListener;
	}

	@Override
	public IMouseListener getMouse() {
		return mouseListener;
	}

	@Override
	public void clearAllEvents() {
		keyListener.clearEvents();
		mouseListener.clearEvents();
	}


}
