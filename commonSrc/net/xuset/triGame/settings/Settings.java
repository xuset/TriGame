package net.xuset.triGame.settings;

public class Settings {
	
	public Settings(int defaultBlockSize, float initUiZoom) {
		this.defaultBlockSize = defaultBlockSize;
		this.initUiZoom = initUiZoom;
		uiZoom = initUiZoom;
	}
	
	public boolean drawUiTouch = false;
	public double gameZoom = 1;
	public final int defaultBlockSize;
	public boolean enableSound = true;
	public double wallGenCoefficient = 1.0;
	public float uiZoom = 1.0f;
	public final float initUiZoom;
}
