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
import javax.swing.JOptionPane;

import net.xuset.tSquare.demo.Demo;
import net.xuset.tSquare.files.FileFactory;
import net.xuset.tSquare.system.DrawBoard;
import net.xuset.tSquare.system.IDrawBoard;
import net.xuset.triGame.game.DevStart;
import net.xuset.triGame.game.GameMode.GameType;
import net.xuset.triGame.intro.IpGetterIFace;
import net.xuset.triGame.intro.MainStartup;
import net.xuset.triGame.intro.UpdateChecker;
import net.xuset.triGame.settings.Settings;


@SuppressWarnings("unused")
public class DesktopStartup {

	public static void main(String[] args) {
		if (!System.getProperty("os.name").toLowerCase().contains("windows"))
			System.setProperty("sun.java2d.opengl", "True");
		
		mainStartup();
	}
	
	private static void devStartup() {
		Settings settings = createDefaultSettings();
		IDrawBoard db1 = createWindow(Params.GAME_NAME);
		//IDrawBoard db2 = createWindow("TriGame Dev - client");
		
		DevStart.startSolo(GameType.SURVIVAL, db1, new FileFactory(), settings);
		//DevStart.startLocalMultiplayer(GameType.SURVIVAL, db1, db2, new FileFactory(), settings);
		
		//Demo demo = new Demo(db1);
		//demo.start();
	}
	
	private static void mainStartup() {
		JFrame frame = new JFrame();
		IDrawBoard drawBoard = createWindow(frame, Params.GAME_NAME);
		
		if (Params.DEBUG_MODE)
			createGame(drawBoard);
		else
			catchAllAndCreateGame(frame, drawBoard);
	}
	
	private static void catchAllAndCreateGame(JFrame frame, IDrawBoard drawBoard) {
		try {
			createGame(drawBoard);
		} catch (Exception ex) {
			System.err.println("Error while handling another error");
			ex.printStackTrace();
			JFrame errorFrame = new JFrame();
			errorFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			JOptionPane.showMessageDialog(errorFrame,
					createStackTrace(ex),
					Params.GAME_NAME + " : An eror has occured.",
					JOptionPane.ERROR_MESSAGE);
			errorFrame.dispose();
			frame.dispose();
		}
	}
	
	private static void createGame(IDrawBoard drawBoard) {
		new MainStartup(drawBoard, new FileFactory(), createDefaultSettings(),
				new IpGetter(), new DesktopBrowserOpener());
	}
	
	private static String createStackTrace(Exception ex) {
		StringBuilder builder = new StringBuilder();
		builder.append("An error has occured and the game has ended.\r\n");
		builder.append("Sorry about this. Below is a description of the error.\r\n\r\n");
		builder.append(ex.toString()).append("\r\n");
		for (StackTraceElement e : ex.getStackTrace()) {
			builder.append("     at ").append(e.toString()).append("\r\n");
		}
		return builder.toString();
	}
	
	private static IDrawBoard createWindow(JFrame frame, String title) {
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
	
	private static IDrawBoard createWindow(String title) {
		return createWindow(new JFrame(), title);
	}
	
	private static final Settings createDefaultSettings() {
		Settings settings = new Settings(50, 1.0f);
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
