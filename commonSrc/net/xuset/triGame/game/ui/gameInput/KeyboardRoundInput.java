package net.xuset.triGame.game.ui.gameInput;

import net.xuset.tSquare.system.input.keyboard.IKeyListener;
import net.xuset.tSquare.system.input.keyboard.KeyAction;
import net.xuset.tSquare.system.input.keyboard.TsKeyEvent;
import net.xuset.tSquare.util.Observer;

public class KeyboardRoundInput implements IRoundInput {
	private long timeRequested = 0L;
	private boolean roundRequested = false;
	
	public KeyboardRoundInput(IKeyListener keyListener) {
		keyListener.watch(new InputObserver());
	}

	/*
	 * Return true only if the enter key was pressed within the last 1000 milliseconds
	 */
	@Override
	public boolean newRoundRequested() {
		return roundRequested && timeRequested + 1000 > System.currentTimeMillis();
	}
	
	private class InputObserver implements Observer.Change<TsKeyEvent> {
		
		@Override
		public void observeChange(TsKeyEvent t) {
			if (t.key == '\n' && t.action == KeyAction.PRESS) { //if <enter> is pressed
				roundRequested = true;
				timeRequested = System.currentTimeMillis();
			}
		}
		
	}

	@Override
	public void setNewRoundRequestable(boolean requestable) {
		if (!requestable)
			roundRequested = false;
	}
}
