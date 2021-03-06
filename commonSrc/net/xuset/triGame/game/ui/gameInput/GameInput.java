package net.xuset.triGame.game.ui.gameInput;

import net.xuset.tSquare.system.input.keyboard.IKeyListener;
import net.xuset.tSquare.ui.layout.UiBorderLayout;
import net.xuset.triGame.game.guns.GunType;
import net.xuset.triGame.settings.Settings;

public class GameInput implements IGameInput{
	private final Settings settings;
	
	private final CombinePlayer playerInput;
	private final CombineGun gunInput;
	private final CombineRound roundInput;

	public GameInput(Settings settings, IKeyListener keyboard,
			IGunInput gunArsenalInput, UiBorderLayout layout) {
		
		this.settings = settings;
		
		playerInput = new CombinePlayer(layout, keyboard);
		gunInput = new CombineGun(layout, keyboard, gunArsenalInput);
		roundInput = new CombineRound(layout, keyboard);
	}
	
	public boolean contains(float x, float y) {
		return  (settings.drawUiTouch &&
				(playerInput.touchPlayer.contains(x, y) ||
				gunInput.touchGun.contains(x, y) ||
				roundInput.touchRound.contains(x, y)));
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
		roundInput.touchRound.setEnableTouchMode(isVisible);
	}
	
	private boolean isUiTouchOn() {
		return settings.drawUiTouch;
	}
	
	private class CombinePlayer implements IPlayerInput {
		private final UiPlayerInput touchPlayer;
		private final KeyboardPlayerInput keyboardPlayer;
		private boolean keyboardsTurn = true;
		
		private CombinePlayer(UiBorderLayout layout, IKeyListener keyboard) {
			touchPlayer = new UiPlayerInput();
			keyboardPlayer = new KeyboardPlayerInput(keyboard);
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
		
		private CombineGun(UiBorderLayout layout, IKeyListener keyboard,
				IGunInput gunArsenalInput) {
			
			this.gunArsenalInput = gunArsenalInput;
			touchGun = new UiShootInput();
			keyboardGun = new KeyboardGunInput(keyboard);
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
		private boolean requestNew = false;
		
		private CombineRound(UiBorderLayout layout, IKeyListener keyboard) {
			touchRound = new UiRoundInput();
			keyboardRound = new KeyboardRoundInput(keyboard);
			layout.add(touchRound, UiBorderLayout.BorderPosition.NORTH);
		}
		
		@Override
		public boolean newRoundRequested() {
			boolean requested = wasNewRoundRequested();
			if (requested)
				touchRound.displayWaitText();
			return requested;
		}
		
		private boolean wasNewRoundRequested() {
			boolean keyboard = keyboardRound.newRoundRequested();
			boolean touch = touchRound.newRoundRequested() && isUiTouchOn();
			
			requestNew = keyboard || touch;
			return requestNew;
		}
		@Override
		public void setNewRoundRequestable(boolean requestable) {
			touchRound.setNewRoundRequestable(requestable);
			keyboardRound.setNewRoundRequestable(requestable);
		}
	}

}
