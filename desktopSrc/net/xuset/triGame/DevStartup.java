package net.xuset.triGame;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JFrame;

import net.xuset.tSquare.files.FileFactory;
import net.xuset.tSquare.system.DrawBoard;
import net.xuset.tSquare.system.IDrawBoard;
import net.xuset.tSquare.system.Network;
import net.xuset.triGame.game.GameInfo;
import net.xuset.triGame.game.TriGame;
import net.xuset.triGame.game.GameInfo.NetworkType;
import net.xuset.triGame.game.GameMode.GameType;


public class DevStartup {

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setPreferredSize(new Dimension(800, 600));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("TriGame port");

		IDrawBoard drawBoard = new DrawBoard(800, 600);
		Container pane = frame.getContentPane();
		Canvas canvas = (Canvas) drawBoard.getBackend();
		pane.add(canvas, BorderLayout.CENTER);

		frame.pack();
		frame.setVisible(true);

		Network n = Network.createOffline();
		GameInfo info = new GameInfo(n, GameType.SURVIVAL, NetworkType.SOLO);
		TriGame tGame = new TriGame(info, drawBoard, new FileFactory());
		tGame.startGame();
		
		/*Demo d = new Demo(drawBoard);
		d.start();*/

	}

}