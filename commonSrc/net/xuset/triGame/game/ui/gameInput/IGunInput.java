package net.xuset.triGame.game.ui.gameInput;

import net.xuset.triGame.game.guns.GunType;

public interface IGunInput {
	boolean shootRequested();
	boolean changeGunRequested();
	GunType getCurrentGunType();
}
