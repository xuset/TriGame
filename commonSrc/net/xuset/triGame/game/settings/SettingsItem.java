package net.xuset.triGame.game.settings;

public class SettingsItem<T> {
	private final Settings key;
	private T value;
	
	public Settings getKey() { return key; }
	public T getValue() { return value; }
	public void setValue(T t) { value = t; }
	
	public SettingsItem(Settings key) {
		this(key, null);
	}
	
	public SettingsItem(Settings key, T value) {
		this.key = key;
		this.value = value;
	}
}
