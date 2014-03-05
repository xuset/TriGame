package net.xuset.triGame.intro.solo;

import net.xuset.tSquare.system.Network;
import net.xuset.tSquare.ui.UiComponent;
import net.xuset.triGame.game.GameInfo;
import net.xuset.triGame.game.GameInfo.NetworkType;
import net.xuset.triGame.game.GameMode.GameType;
import net.xuset.triGame.intro.IntroForm;

public class IntroSolo implements IntroForm {

	@Override
	public GameInfo getCreatedGameInfo() {
		return new GameInfo(Network.createOffline(), GameType.SURVIVAL, NetworkType.SOLO);
	}

	@Override
	public UiComponent getForm() {
		// TODO Auto-generated method stub
		return null;
	}

}
