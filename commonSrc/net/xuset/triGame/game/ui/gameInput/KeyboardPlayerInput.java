package net.xuset.triGame.game.ui.gameInput;

import net.xuset.tSquare.system.input.keyboard.IKeyListener;
import net.xuset.tSquare.system.input.keyboard.TsKeyEvent;
import net.xuset.tSquare.util.Observer;

public class KeyboardPlayerInput implements IPlayerInput {
	private boolean upPressed = false;
	private boolean downPressed = false;
	private boolean leftPressed = false;
	private boolean rightPressed = false;
	private double angle = 90;
	
	public KeyboardPlayerInput(IKeyListener keyboard) {
		keyboard.watch(new KeyObserver());
	}
	
	public boolean moveKeysPressed() {
		return (upPressed || rightPressed || leftPressed || downPressed);
	}

	@Override
	public double getMoveCoEfficient() {
		if (moveKeysPressed())
			return 1.0;
		return 0;
	}

	@Override
	public double getMoveAngle() {
		if (!moveKeysPressed())
			return Math.toRadians(angle);
		
		if (upPressed) {
			if (leftPressed && !rightPressed)
				angle = 135;
			else if (rightPressed && !leftPressed)
				angle = 45;
			else if (!leftPressed && !rightPressed)
				angle = 90;
		} else if (downPressed) {
			if (leftPressed && !rightPressed)
				angle = 225;
			else if (rightPressed && !leftPressed)
				angle = 315;
			else if (!leftPressed && !rightPressed)
				angle = 270;
		} else if (leftPressed && !rightPressed) {
			angle = 180;
		} else if (rightPressed && !leftPressed) {
			angle = 0;
		}
		return Math.toRadians(angle);
	}
	
	private class KeyObserver implements Observer.Change<TsKeyEvent> {

		@Override
		public void observeChange(TsKeyEvent t) {
			switch(t.action) {
			case PRESS:
				setKey(t.key, true);
				break;
			case RELEASE:
				setKey(t.key, false);
				break;
			}
		}
		
		private void setKey(char key, boolean val) {
			switch(key) {
			case 'w':
			case 'W':
				upPressed = val;
				return;
			case 'S':
			case 's':
				downPressed = val;
				return;
			case 'a':
			case 'A':
				leftPressed = val;
				return;
			case 'd':
			case 'D':
				rightPressed = val;
				return;
			}
			return;
		}
	}

}
