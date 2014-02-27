package net.xuset.tSquare.system.input;

import net.xuset.tSquare.system.input.keyboard.IKeyListener;
import net.xuset.tSquare.system.input.mouse.IMouseListener;

public interface InputHolder {
	IKeyListener getKeyboard();
	IMouseListener getMouse();
	void clearAllEvents();
}
