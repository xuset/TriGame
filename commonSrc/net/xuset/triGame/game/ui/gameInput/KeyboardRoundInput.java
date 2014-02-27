package net.xuset.triGame.game.ui.gameInput;

import net.xuset.tSquare.system.input.keyboard.IKeyListener;
import net.xuset.tSquare.system.input.keyboard.KeyAction;
import net.xuset.tSquare.system.input.keyboard.TsKeyEvent;
import net.xuset.tSquare.util.Observer;

public class KeyboardRoundInput implements IRoundInput {
	private boolean roundRequested = false;
	
	public KeyboardRoundInput(IKeyListener keyListener) {
		keyListener.watch(new InputObserver());
	}

	/*
	 * TODO What if the enter key was pressed in the middle of the round.
	 * would roundRequested still be true?
	 * If so, that could produce undesired results.
	 */
	
	@Override
	public boolean newRoundRequested() {
		return roundRequested;
	}
	
	private class InputObserver implements Observer.Change<TsKeyEvent> {
		
		@Override
		public void observeChange(TsKeyEvent t) {
			if (t.key == '\n' && t.action == KeyAction.PRESS) { //if <enter> is pressed
				roundRequested = true;
			}
		}
		
	}

	@Override
	public void setNewRoundRequestable(boolean requestable) {
		if (!requestable)
			roundRequested = false;
	}
}
