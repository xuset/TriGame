package net.xuset.triGame.intro;

import net.xuset.tSquare.ui.UiComponent;
import net.xuset.triGame.game.GameInfo;

interface IntroForm {
	UiComponent getForm();
	GameInfo getCreatedGameInfo();
	void onFocusGained();
	void onFocusLost();
	void update();
}
