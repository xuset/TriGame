package tSquare.system;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;

import tSquare.math.Point;


public class Display extends JFrame{
	private static final long serialVersionUID = -6572800359562361356L;
	public Point dimensions;
	public int widthPixels;
	public int heightPixels;
	public GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	public GraphicsConfiguration gc = gd.getDefaultConfiguration();
	
	public JFrame getFrame() { return this; }
	
	public Display(int width, int height, String title) {
		dimensions = new Point(width, height);
		widthPixels = width;
		heightPixels = height;
		//this.setUndecorated(true);
		this.setTitle(title);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(width, height);
		this.setVisible(true);
	}
	public Display(int width, int height) {
		this(width, height, "");
	}
	
	/*public void setFullScreen() {
		if (gd.isFullScreenSupported())
			gd.setFullScreenWindow(container);
		if (gd.isDisplayChangeSupported()) {
			int width = 1000;
			int height = 700;
			DisplayMode[] modes = gd.getDisplayModes();
			int bestIndex = -1;
			int bestScore = 9999999;
			for (int i = 0; i < modes.length; i++) {
				int w = (int) Math.abs(modes[i].getWidth() - width);
				int h = (int) Math.abs(modes[i].getHeight() - height);
				if (w + h < bestScore) {
					bestScore = w + h;
					bestIndex = i;
				}
			}
			if (bestIndex > -1)
				gd.setDisplayMode(modes[bestIndex]);
				//gd.setDisplayMode(new DisplayMode(widthPixels, heightPixels, 32, DisplayMode.REFRESH_RATE_UNKNOWN));
		}
	}*/
	
	public void setFullScreen() {
		if (gd.isFullScreenSupported())
			gd.setFullScreenWindow(this);
	}
	
	public void maximizeWindow() {
		this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
	}
}
