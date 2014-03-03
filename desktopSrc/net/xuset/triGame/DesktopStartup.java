package net.xuset.triGame;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JFrame;

import net.xuset.tSquare.files.FileFactory;
import net.xuset.tSquare.system.DrawBoard;
import net.xuset.tSquare.system.IDrawBoard;
import net.xuset.triGame.intro.GameIntro;


public class DesktopStartup {

	public static void main(String[] args) {
		if (!System.getProperty("os.name").toLowerCase().contains("windows"))
			System.setProperty("sun.java2d.opengl", "True");
		
		IDrawBoard db1 = createWindow("TriGame Dev - server");
		//IDrawBoard db2 = createWindow("TriGame Dev - client");
		
		GameIntro gameIntro = new GameIntro(db1, new FileFactory());
		gameIntro.createGame().startGame();
		//DevStart.startSolo(GameType.SURVIVAL, db1, new FileFactory());
		//DevStart.startLocalMultiplayer(GameType.SURVIVAL, db1, db2, new FileFactory());
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

}