package net.xuset.tSquare.demo;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JFrame;

import net.xuset.tSquare.demo.Demo;
import net.xuset.tSquare.system.DrawBoard;
import net.xuset.tSquare.system.IDrawBoard;


public class DemoStartup {
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setPreferredSize(new Dimension(800, 600));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("TSquare game demo");
		
		IDrawBoard drawBoard = new DrawBoard(800, 600);
		frame.add((Canvas) drawBoard.getBackend(), BorderLayout.CENTER);
		
		frame.pack();
		frame.setVisible(true);
		
		Demo demo = new Demo(drawBoard);
		demo.start();
	}
}
