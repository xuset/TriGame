package net.xuset.tSquare.system;

import net.xuset.tSquare.imaging.AndroidGraphics;
import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.math.rect.RectangleR;
import net.xuset.tSquare.math.rect.IRectangleR;
import net.xuset.tSquare.system.input.AndroidInputHolder;
import net.xuset.tSquare.system.input.InputHolder;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class DrawBoard implements IDrawBoard {
	private final SurfaceView view;
	private final SurfaceHolder holder;
	private final WindowRect windowRect = new WindowRect();
	private Canvas lockedCanvas = null;
	
	public DrawBoard(Context c) {
		view = new SurfaceView(c);
		holder = view.getHolder();
	}
	
	@Override
	public SurfaceView getBackend() { return view; }

	@Override
	public int getWidth() {
		return view.getWidth();
	}

	@Override
	public int getHeight() {
		return view.getHeight();
	}

	@Override
	public IGraphics getGraphics() {
		//TODO need to catch for IllegalStateException
		lockedCanvas = holder.lockCanvas();
		if (lockedCanvas == null)
			return null;
		return new AndroidGraphics(lockedCanvas);
	}

	@Override
	public void flushScreen() {
		holder.unlockCanvasAndPost(lockedCanvas);
	}

	@Override
	public void clearScreen() {
		if (lockedCanvas != null)
			lockedCanvas.drawColor(Color.BLACK);
	}

	@Override
	public InputHolder createInputListener() {
		return new AndroidInputHolder(view);
	}
	
	@Override
	public IRectangleR getWindow() { return windowRect; }
	
	private class WindowRect extends RectangleR {

		@Override public double getX() { return 0; }
		@Override public double getY() { return 0; }
		@Override public double getWidth() { return view.getWidth(); }
		@Override public double getHeight() { return view.getHeight(); }
		
	}

}
