package net.xuset.triGame.settings;

public class Settings {
	public boolean drawUiTouch = false;
	public int blockSize = 50;
	public int defaultBlockSize = 50;
	public boolean enableSound = true;
	public double wallGenCoefficient = 1.0;
	public float uiZoom = 1.0f;
	
	
	public Settings createCopy() {
		Settings s = new Settings();
		s.drawUiTouch = drawUiTouch;
		s.blockSize = blockSize;
		s.defaultBlockSize = defaultBlockSize;
		return s;
	}
}
