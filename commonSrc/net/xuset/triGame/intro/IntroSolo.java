package net.xuset.triGame.intro;

import net.xuset.tSquare.system.Network;
import net.xuset.tSquare.ui.UiComponent;
import net.xuset.tSquare.ui.UiForm;
import net.xuset.tSquare.ui.UiLabel;
import net.xuset.triGame.game.GameInfo;
import net.xuset.triGame.game.GameInfo.NetworkType;
import net.xuset.triGame.game.GameMode.GameType;

class IntroSolo implements IntroForm {
	private final UiForm frmMain = new UiForm();
	
	IntroSolo() {
		frmMain.getLayout().add(new UiLabel("Loading..."));
	}

	@Override
	public GameInfo getCreatedGameInfo() {
		return new GameInfo(Network.createOffline(), GameType.SURVIVAL, NetworkType.SOLO);
	}

	@Override
	public UiComponent getForm() {
		return frmMain;
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
