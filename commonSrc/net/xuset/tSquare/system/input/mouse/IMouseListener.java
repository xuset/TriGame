package net.xuset.tSquare.system.input.mouse;

import net.xuset.tSquare.system.input.IInputListener;
import net.xuset.tSquare.util.Observer;

public interface IMouseListener extends IInputListener<TsMouseEvent> {
	TsMouseEvent searchForEvent(MouseButton button, MouseAction action);
	
	void watch(Observer.Change<TsMouseEvent> watcher);
}
