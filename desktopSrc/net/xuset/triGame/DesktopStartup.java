package net.xuset.triGame;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Container;
import java.awt.Dimension;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Enumeration;

import javax.swing.JFrame;

import net.xuset.tSquare.demo.Demo;
import net.xuset.tSquare.files.FileFactory;
import net.xuset.tSquare.system.DrawBoard;
import net.xuset.tSquare.system.IDrawBoard;
import net.xuset.triGame.game.DevStart;
import net.xuset.triGame.game.GameMode.GameType;
import net.xuset.triGame.intro.GameIntro;
import net.xuset.triGame.intro.IpGetterIFace;
import net.xuset.triGame.settings.Settings;


@SuppressWarnings("unused")
public class DesktopStartup {

	public static void main(String[] args) {
		if (!System.getProperty("os.name").toLowerCase().contains("windows"))
			System.setProperty("sun.java2d.opengl", "True");
		
		Settings settings = createDefaultSettings();
		IDrawBoard db1 = createWindow("TriGame Dev - server");
		//IDrawBoard db2 = createWindow("TriGame Dev - client");
		
		GameIntro gameIntro = new GameIntro(db1, new FileFactory(), settings, new IpGetter());
		gameIntro.createGame().startGame();
		//DevStart.startSolo(GameType.SURVIVAL, db1, new FileFactory(), settings);
		//DevStart.startLocalMultiplayer(GameType.SURVIVAL, db1, db2, new FileFactory(), settings);
		
		//Demo demo = new Demo(db1);
		//demo.start();
	}
	
	private static IDrawBoard createWindow(String title) {
		JFrame frame = new JFrame();
		frame.setPreferredSize(new Dimension(800, 600));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle(title);

		DrawBoard drawBoard = new DrawBoard(800, 600);
		Container pane = frame.getContentPane();
		Canvas canvas = drawBoard.getBackend();
		pane.add(canvas, BorderLayout.CENTER);

		frame.pack();
		frame.setVisible(true);
		
		return drawBoard;
	}
	
	private static final Settings createDefaultSettings() {
		Settings settings = new Settings();
		settings.drawUiTouch = false;
		return settings;
	}
	
	private static class IpGetter implements IpGetterIFace {
		private InetAddress address = null;
		
		public IpGetter() {
			new Thread(new Worker(), "LocalIpFinder").start();
		}

		@Override
		public InetAddress getLocalIP() {
			return address;
		}
		
		private class Worker implements Runnable {
			
			@Override
			public void run() {
				try {
					address = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					System.err.println("Error: Could not determine local IP");
					e.printStackTrace();
				}
			}
			
		}
		
	}

}
