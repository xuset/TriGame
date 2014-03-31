package net.xuset.tSquare.system.input;

import net.xuset.tSquare.system.input.keyboard.AndroidKeyboardListener;
import net.xuset.tSquare.system.input.keyboard.IKeyListener;
import net.xuset.tSquare.system.input.mouse.AndroidMouseListener;
import net.xuset.tSquare.system.input.mouse.IMouseListener;

import android.view.View;

public class AndroidInputHolder implements InputHolder {
	private final AndroidMouseListener mouse;
	private final AndroidKeyboardListener keyboard;
	
	public AndroidInputHolder(View v) {
		mouse = new AndroidMouseListener(v);
		keyboard = new AndroidKeyboardListener(v);
	}

	@Override
	public IKeyListener getKeyboard() {
		return keyboard;
	}

	@Override
	public IMouseListener getMouse() {
		return mouse;
	}

}
