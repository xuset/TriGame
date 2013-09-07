package tSquare.system;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Window;

import javax.swing.JFrame;
public class Screen {
	private GraphicsDevice vc;
	public Screen() {
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		vc = env.getDefaultScreenDevice();
	}
	public void setFullScreen(JFrame frame) {
		frame.setUndecorated(true);
		frame.setResizable(false);
		if (vc.isDisplayChangeSupported() == true) {
			int[] i = findBestDisplayMode();
			DisplayMode dm = new DisplayMode(i[0], i[1], 16, DisplayMode.REFRESH_RATE_UNKNOWN);
			try {
				vc.setDisplayMode(dm);
			} catch (Exception ex) {
				System.out.print(ex.getMessage());
			}
		}
	}
	public void exitFullScreen() {
		Window w = vc.getFullScreenWindow();
		if (w != null)
			w.dispose();
		vc.setFullScreenWindow(null);
	}
	public void setDisplayMode(int x, int y) {
		vc.setDisplayMode(new DisplayMode(x, y, 16, DisplayMode.REFRESH_RATE_UNKNOWN));
	}
	public int[] findBestDisplayMode()
	{
		int[] i = {800,600};
		return i;
	}
}
