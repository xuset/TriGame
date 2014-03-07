package net.xuset.triGame.intro.settings;

import net.xuset.tSquare.ui.UiComponent;
import net.xuset.triGame.game.GameInfo;
import net.xuset.triGame.intro.IntroForm;
import net.xuset.triGame.settings.Settings;
import net.xuset.triGame.settings.UiSettingsForm;

public class IntroSettings implements IntroForm {
	private final UiSettingsForm uiSettings;
	
	public IntroSettings(Settings settings) {
		uiSettings = new UiSettingsForm(settings);
	}

	@Override
	public UiComponent getForm() {
		return uiSettings.getUiComponent();
	}

	@Override
	public GameInfo getCreatedGameInfo() {
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
