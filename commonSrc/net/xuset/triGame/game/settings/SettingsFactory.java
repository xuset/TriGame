package net.xuset.triGame.game.settings;

public class SettingsFactory {
	public static Settings createWithDefaults() {
		Settings s = new Settings();
		s.drawUiTouch = false;
		return s;
	}
}
