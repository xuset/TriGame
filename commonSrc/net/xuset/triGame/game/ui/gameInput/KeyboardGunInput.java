package net.xuset.triGame.game.ui.gameInput;

import net.xuset.tSquare.system.input.keyboard.IKeyListener;
import net.xuset.tSquare.system.input.keyboard.KeyAction;
import net.xuset.tSquare.system.input.keyboard.TsKeyEvent;
import net.xuset.tSquare.util.Observer;
import net.xuset.triGame.game.guns.GunType;

public class KeyboardGunInput implements IGunInput {
	
	private boolean shootRequested = false;
	private boolean changeRequested = false;
	private GunType selectedGun = GunType.PISTOL;
	
	public KeyboardGunInput(IKeyListener keyListener) {
		keyListener.watch(new GunInputObserver());
	}

	@Override
	public boolean shootRequested() {
		return shootRequested;
	}

	@Override
	public boolean changeGunRequested() {
		return changeRequested;
	}
	
	@Override
	public GunType getCurrentGunType() {
		return selectedGun;
	}
	
	private class GunInputObserver implements Observer.Change<TsKeyEvent> {

		@Override
		public void observeChange(TsKeyEvent t) {
			switch(t.key) {
			case ' ':
				shootRequested = (t.action == KeyAction.PRESS);
				break;
			case '1':
				changeRequested = selectedGun != GunType.PISTOL;
				selectedGun = GunType.PISTOL;
				break;
			case '2':
				changeRequested = selectedGun != GunType.SHOT_GUN;
				selectedGun = GunType.SHOT_GUN;
				break;
			case '3':
				changeRequested = selectedGun != GunType.SUB;
				selectedGun = GunType.SUB;
				break;
				
			}
		}
		
	}

}
