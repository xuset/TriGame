package net.xuset.triGame;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import net.xuset.triGame.settings.Settings;

public class AndroidSettings extends Settings {
	private static final String firstTimeLoadKey = "firstTimeLoad";
	private static final String uiZoomKey = "uiZoom";
	private static final String enableSoundKey = "enableSound";
	private static final String gameZoomKey = "gameZoom";
	
	private final SharedPreferences prefs;
	
	public AndroidSettings(Context context, int initBlockSize) {
		super(initBlockSize, initBlockSize / 50.0f);
		this.prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		
		resetSettings(initBlockSize);
		
		if (prefs.contains(firstTimeLoadKey))
			loadFromSaved();
		else
			saveSettings();
	}
	
	public void resetSettings(int initBlockSize) {
		gameZoom = 1.0;
		uiZoom = initBlockSize / 50.0f;
		drawUiTouch = true;
		enableSound = true;
	}
	
	public void loadFromSaved() {
		uiZoom = prefs.getFloat(uiZoomKey, uiZoom);
		enableSound = prefs.getBoolean(enableSoundKey, enableSound);
		gameZoom = prefs.getFloat(gameZoomKey, (float) gameZoom);
	}
	
	public void saveSettings() {
		Editor ed = prefs.edit();
		
		ed.putBoolean(firstTimeLoadKey, true);
		ed.putFloat(uiZoomKey, uiZoom);
		ed.putBoolean(enableSoundKey, enableSound);
		ed.putFloat(gameZoomKey, (float) gameZoom);
		
		ed.commit();
	}
	
}
