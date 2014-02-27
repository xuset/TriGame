package net.xuset.triGame.game.ui.gameInput;

import net.xuset.tSquare.system.input.InputHolder;
import net.xuset.tSquare.ui.layout.UiBorderLayout;
import net.xuset.triGame.game.guns.GunType;
import net.xuset.triGame.game.settings.Settings;

public class GameInput implements IGameInput{
	private final Settings settings;
	private final InputHolder input;
	
	private final CombinePlayer playerInput;
	private final CombineGun gunInput;
	private final CombineRound roundInput;

	public GameInput(Settings settings, InputHolder input,
			IGunInput gunArsenalInput, UiBorderLayout layout) {
		
		this.settings = settings;
		this.input = input;
		
		playerInput = new CombinePlayer(layout);
		gunInput = new CombineGun(layout, gunArsenalInput);
		roundInput = new CombineRound(layout);
	}

	@Override
	public IGunInput getGunInput() {
		return gunInput;
	}

	@Override
	public IPlayerInput getPlayerInput() {
		return playerInput;
	}

	@Override
	public IRoundInput getRoundInput() {
		return roundInput;
	}
	
	@Override
	public void update(int deltaFrame) {
		setUiTouchVisibility(isUiTouchOn());
	}
	
	private void setUiTouchVisibility(boolean isVisible) {
		playerInput.touchPlayer.setVisibile(isVisible);
		gunInput.touchGun.setVisibile(isVisible);
		roundInput.touchRound.respondToMouse(isVisible);
		roundInput.touchRound.setText(isVisible ?
				"Tap to start round" : "\'Enter\' to start round");
	}
	
	private boolean isUiTouchOn() {
		return settings.drawUiTouch;
	}
	
	private class CombinePlayer implements IPlayerInput {
		private final UiPlayerInput touchPlayer;
		private final KeyboardPlayerInput keyboardPlayer;
		private boolean keyboardsTurn = true;
		
		private CombinePlayer(UiBorderLayout layout) {
			touchPlayer = new UiPlayerInput();
			keyboardPlayer = new KeyboardPlayerInput(input.getKeyboard());
			layout.add(touchPlayer, UiBorderLayout.BorderPosition.WEST);
		}

		@Override
		public double getMoveCoEfficient() {
			setTurn();
			if (keyboardsTurn)
				return keyboardPlayer.getMoveCoEfficient();
			else
				return touchPlayer.getMoveCoEfficient();
		}

		@Override
		public double getMoveAngle() {
			if (keyboardsTurn)
				return keyboardPlayer.getMoveAngle();
			else
				return touchPlayer.getMoveAngle();
		}
		
		private void setTurn() {
			if (keyboardsTurn) {
				if (touchPlayer.isBeingTouched() && !keyboardPlayer.moveKeysPressed())
					keyboardsTurn = false;
			} else {
				if (!touchPlayer.isBeingTouched() && keyboardPlayer.moveKeysPressed())
					keyboardsTurn = true;
			}
			
			if (!isUiTouchOn())
				keyboardsTurn = true;
		}
		
	}
	
	private class CombineGun implements IGunInput {
		private final UiShootInput touchGun;
		private final KeyboardGunInput keyboardGun;
		private final IGunInput gunArsenalInput;
		private boolean keyboardsChange = true;
		
		//TODO add support for changing guns within arsenal gun form.
		
		private CombineGun(UiBorderLayout layout, IGunInput gunArsenalInput) {
			this.gunArsenalInput = gunArsenalInput;
			touchGun = new UiShootInput();
			keyboardGun = new KeyboardGunInput(input.getKeyboard());
			layout.add(touchGun, UiBorderLayout.BorderPosition.EAST);
		}

		@Override
		public boolean shootRequested() {
			if (keyboardGun.shootRequested())
				return true;
			if (!isUiTouchOn())
				return false;
			return touchGun.shootRequested();
		}

		@Override
		public boolean changeGunRequested() {
			if (keyboardGun.changeGunRequested()) {
				keyboardsChange = true;
				return true;
			} else if (gunArsenalInput.changeGunRequested()) {
				keyboardsChange = false;
				return true;
			}
			
			return false;
		}

		@Override
		public GunType getCurrentGunType() {
			if (keyboardsChange)
				return keyboardGun.getCurrentGunType();
			else
				return gunArsenalInput.getCurrentGunType();
		}
		
	}
	
	private class CombineRound implements IRoundInput {
		private final UiRoundInput touchRound;
		private final KeyboardRoundInput keyboardRound;
		
		private CombineRound(UiBorderLayout layout) {
			touchRound = new UiRoundInput();
			keyboardRound = new KeyboardRoundInput(input.getKeyboard());
			layout.add(touchRound, UiBorderLayout.BorderPosition.NORTH);
		}
		
		@Override
		public boolean newRoundRequested() {
			if (keyboardRound.newRoundRequested())
				return true;
			if (!isUiTouchOn())
				return false;
			return touchRound.newRoundRequested();
		}
		@Override
		public void setNewRoundRequestable(boolean requestable) {
			touchRound.setNewRoundRequestable(requestable);
			keyboardRound.setNewRoundRequestable(requestable);
		}
	}

}
