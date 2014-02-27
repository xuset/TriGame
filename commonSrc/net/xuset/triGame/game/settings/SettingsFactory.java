package net.xuset.triGame.game.settings;

public abstract class SettingsFactory {
	
	public static SettingsContainer createWithDefaults() {
		SettingsContainer s = new SettingsContainer();
		
		s.setItem(new SettingsItem<Boolean>(Settings.DRAW_UI_TOUCH_CONTROLS, true));
		
		SettingsContainer.checkForMissingItemError(s);
		return s;
	}
}
