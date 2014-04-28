package net.xuset.triGame;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Container;
import java.awt.Dimension;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JFrame;

import net.xuset.tSquare.files.FileFactory;
import net.xuset.tSquare.system.DrawBoard;
import net.xuset.tSquare.system.IDrawBoard;
import net.xuset.triGame.game.GameMode.GameType;
import net.xuset.triGame.intro.IpGetterIFace;
import net.xuset.triGame.intro.MainStartup;
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
		
		net.xuset.triGame.game.DevStart.startSolo(
				GameType.SURVIVAL, db1, new FileFactory(), settings);
//		net.xuset.triGame.game.DevStart.startLocalMultiplayer(
//				GameType.SURVIVAL, db1, createWindow(Params.GAME_NAME + " - client"),
//				new FileFactory(), settings);
//		
//		new net.xuset.tSquare.demo.Demo(db1).start();
	}
	
	private static void mainStartup() {
		IDrawBoard drawBoard = createWindow(Params.GAME_NAME);
		createGame(drawBoard);
	}
	
	private static void createGame(IDrawBoard drawBoard) {
		new MainStartup(drawBoard,
				new FileFactory(),
				createDefaultSettings(),
				new DesktopIpGetter(),
				new DesktopBrowserOpener());
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

}
