package net.xuset.tSquare.imaging;

public class TsColor {
	public static final TsColor white     = new TsColor(255, 255, 255);
	public static final TsColor pink      = new TsColor(255, 175, 175);
	public static final TsColor red       = new TsColor(255, 0, 0);
	public static final TsColor orange    = new TsColor(255, 200, 0);
	public final static TsColor yellow    = new TsColor(255, 255, 0);
	public static final TsColor green     = new TsColor(0, 255, 0);
	public final static TsColor magenta   = new TsColor(255, 0, 255);
	public final static TsColor cyan      = new TsColor(0, 255, 255);
	public static final TsColor blue      = new TsColor(0, 0, 255);
	public static final TsColor gray      = new TsColor(128, 128, 128);
	public static final TsColor lightGray = new TsColor(192, 192, 192);
	public static final TsColor darkGray  = new TsColor(64, 64, 64);
	public static final TsColor black     = new TsColor(0, 0, 0);
	
	private static final int deltaColor = 16;
	
	public static int rgb(int r, int g, int b) {
		return rgba(r, g, b, 255);
	}
	
	public static int rgba(int r, int g, int b, int a) {
		int rgba = a;
		rgba = (rgba << 8) + r;
		rgba = (rgba << 8) + g;
		rgba = (rgba << 8) + b;
		return rgba;
	}
	
	private final int r, g, b, a;
	
	public TsColor(int r, int g, int b, int a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
	public TsColor(int r, int g, int b) {
		this(r, g, b, 255);
	}
	
	public TsColor(int rgba) {
		b = (rgba)&0xFF;
		g = (rgba>>8)&0xFF;
		r = (rgba>>16)&0xFF;
		a = (rgba>>24)&0xFF;
	}
	
	public int getRed() { return r; }
	public int getGreen() { return g; }
	public int getBlue() { return b; }
	public int getAlpha() { return a; }
	
	public int getRGBA() {
		return rgba(r, g, b, a);
	}
	
	public int lighter() {
		return shade(deltaColor);
	}
	
	public int darker() {
		return shade(-deltaColor);
	}
	
	public int shade(int delta) {
		int newR = Math.max(0, Math.min(r + delta, 255));
		int newG = Math.max(0, Math.min(g + delta, 255));
		int newB = Math.max(0, Math.min(b + delta, 255));
		int newA = Math.max(0, Math.min(a + delta, 255));
		
		return rgba(newR, newG, newB, newA);
	}
}
