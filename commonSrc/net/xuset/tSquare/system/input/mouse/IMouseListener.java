package net.xuset.tSquare.system.input.mouse;

import net.xuset.tSquare.system.input.IInputListener;

public interface IMouseListener extends IInputListener<TsMouseEvent> {
	int getPointerCount();
	MousePointer getPointerByIndex(int index);
	MousePointer getPointerById(int id);
}
