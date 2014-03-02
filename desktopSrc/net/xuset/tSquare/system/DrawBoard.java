package net.xuset.tSquare.system;


import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;

import net.xuset.tSquare.imaging.AwtGraphics;
import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.math.rect.IRectangleR;
import net.xuset.tSquare.math.rect.RectangleR;
import net.xuset.tSquare.system.input.AwtInputHolder;
import net.xuset.tSquare.system.input.InputHolder;

public class DrawBoard implements IDrawBoard{
	private final Canvas canvas;
	private BufferStrategy strategy;
	private Graphics graphics;
	private final WindowRectangle windowRect = new WindowRectangle();
	
	public DrawBoard(int width, int height) {
		canvas = new Canvas();
		canvas.setSize(width, height);
		canvas.setIgnoreRepaint(true);
	}

	@Override
	public Canvas getBackend() { return canvas; }

	@Override
	public int getWidth() {
		return canvas.getWidth();
	}

	@Override
	public int getHeight() {
		return canvas.getHeight();
	}

	@Override
	public IGraphics getGraphics() {
		refreshGraphics();
		return new AwtGraphics(graphics, getWidth(), getHeight());
	}

	@Override
	public void flushScreen() {
		graphics.dispose();
		graphics = null;
		strategy.show();
		Toolkit.getDefaultToolkit().sync();
	}
	
	private void refreshGraphics() {
		if (strategy == null) {
	    	canvas.createBufferStrategy(2);
	    	strategy = canvas.getBufferStrategy();
			canvas.requestFocus();
		}
		if (getWidth() <= 0 || getHeight() <= 0)
			graphics = null;
		else
			graphics = strategy.getDrawGraphics();
	}

	@Override
	public void clearScreen() {
		graphics.setColor(Color.black);
		graphics.fillRect(0,  0, getWidth(), getHeight());
	}
	
	@Override
	public InputHolder createInputListener() {
		return new AwtInputHolder(canvas);
	}
	
	@Override
	public IRectangleR getWindow() { return windowRect; }
	
	private class WindowRectangle extends RectangleR {
		@Override public double getX()      { return 0; }
		@Override public double getY()      { return 0; }
		@Override public double getWidth()  { return canvas.getWidth(); }
		@Override public double getHeight() { return canvas.getHeight(); }
	}
	
}
