package net.xuset.triGame.game.ui.gameInput;

import net.xuset.tSquare.system.input.keyboard.IKeyListener;
import net.xuset.tSquare.system.input.keyboard.KeyAction;
import net.xuset.tSquare.system.input.keyboard.TsKeyEvent;
import net.xuset.tSquare.util.Observer;

public class KeyboardRoundInput implements IRoundInput {
	private boolean roundRequested = false;
	private boolean requestable = false;
	
	public KeyboardRoundInput(IKeyListener keyListener) {
		keyListener.watch(new InputObserver());
	}

	/*
	 * Return true only if the enter key was pressed within the last 1000 milliseconds
	 */
	@Override
	public boolean newRoundRequested() {
		return roundRequested;
	}
	
	private class InputObserver implements Observer.Change<TsKeyEvent> {
		
		@Override
		public void observeChange(TsKeyEvent t) {
			if (t.key == '\n' && t.action == KeyAction.RELEASE && requestable) {
				roundRequested = true;
			}
		}
		
	}

	@Override
	public void setNewRoundRequestable(boolean requestable) {
		this.requestable = requestable;
		if (!requestable)
			roundRequested = false;
	}
}
