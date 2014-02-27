package net.xuset.triGame.game.settings;

public class SettingsContainer {
	private final SettingsItem<?> container[];
	
	SettingsContainer() {
		container = new SettingsItem<?>[Settings.values().length];
	}
	
	public void setItem(SettingsItem<?> item) {
		container[item.getKey().ordinal()] = item;
	}
	
	public SettingsItem<?> getItem(Settings key) {
		return container[key.ordinal()];
	}
	
	static void checkForMissingItemError(SettingsContainer s) {
		Settings[] settingMap = Settings.values();
		for (int i = 0; i < settingMap.length; i++) {
			if (s.getItem(settingMap[i]) == null)
				throw new IllegalStateException(
						"SettingsContainer is missing a value defined in Settings");
		}
	}
}
