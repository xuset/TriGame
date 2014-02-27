package net.xuset.tSquare.system;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;


public class Display {
	public final JFrame frame = new JFrame();
	
	public Display(int width, int height) {
		this(width, height, "");
	}
	
	public Display(int width, int height, String title) {
		//com.apple.eawt.FullScreenUtilities.setWindowCanFullScreen(this,true);
		frame.setTitle(title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(width, height);
	}
	
	public void finishInit() {
		frame.pack();
		frame.setVisible(true);
	}
	
	public int getWidth() { return frame.getWidth(); }
	public int getHeight() { return frame.getHeight(); }
	
	public void setFullScreen() {
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		if (gd.isFullScreenSupported())
			gd.setFullScreenWindow(frame);
	}
	
	public void maximizeWindow() {
		frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
	}
}
