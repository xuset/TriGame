package net.xuset.triGame.game.ui.gameInput;

import net.xuset.tSquare.game.GameUpdatable;

public interface IGameInput extends GameUpdatable{
	IGunInput getGunInput();
	IPlayerInput getPlayerInput();
	IRoundInput getRoundInput();
}
