package tSquare.game;


import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;

import tSquare.system.Display;

public class DrawBoard extends Canvas{
	private static final long serialVersionUID = -2837590450949463066L;
	private BufferStrategy strategy;
	private Display display;
	
	private Graphics graphics;
	
	public Display getDisplay() { return display; }
	
	public DrawBoard(Display display) {
		this(display.getWidth(), display.getHeight(), display);
	}
	
	public Graphics getDrawing() {
		return graphics;
	}
	
	public DrawBoard(int width, int height, Display display) {
		setSize(width, height);
		setIgnoreRepaint(true);
		display.add(this, BorderLayout.CENTER);
    	this.createBufferStrategy(2);
    	strategy = this.getBufferStrategy();
    	graphics = (Graphics2D) strategy.getDrawGraphics();
    	this.display = display;
	}
	
	public void exportToScreen() {
		graphics.dispose();
		graphics = null;
		strategy.show();
		Toolkit.getDefaultToolkit().sync();
		graphics = (Graphics2D) strategy.getDrawGraphics();
	}
	
	public void clearBoard() {
		graphics.setColor(Color.black);
		graphics.fillRect(0, 0, getWidth(), getHeight());
	}
	
	
}
