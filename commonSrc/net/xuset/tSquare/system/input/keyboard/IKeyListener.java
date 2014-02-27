package net.xuset.tSquare.system.input.keyboard;

import net.xuset.tSquare.system.input.IInputListener;
import net.xuset.tSquare.util.Observer;

public interface IKeyListener extends IInputListener <TsKeyEvent>{
	TsKeyEvent searchForEvent(int keyId, KeyAction action);
	TsKeyEvent searchForEvent(char key, KeyAction action);
	
	void watch(Observer.Change<TsKeyEvent> watcher);
	
	boolean isUpPressed();
	boolean isDownPressed();
	boolean isLeftPressed();
	boolean isRightPressed();
	boolean isSpacePressed();
	boolean isEnterPressed();
}
