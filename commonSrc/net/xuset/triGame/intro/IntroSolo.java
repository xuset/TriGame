package net.xuset.triGame.intro;

import net.xuset.tSquare.system.Network;
import net.xuset.tSquare.ui.UiComponent;
import net.xuset.triGame.game.GameInfo;
import net.xuset.triGame.game.GameInfo.NetworkType;
import net.xuset.triGame.game.GameMode.GameType;

class IntroSolo implements IntroForm {

	@Override
	public GameInfo getCreatedGameInfo() {
		return new GameInfo(Network.createOffline(), GameType.SURVIVAL, NetworkType.SOLO);
	}

	@Override
	public UiComponent getForm() {
		return null;
	}

	@Override
	public void onFocusGained() {
		//Do nothing
	}

	@Override
	public void onFocusLost() {
		//Do nothing
	}

	@Override
	public void update() {
		//Do nothing
	}

}
