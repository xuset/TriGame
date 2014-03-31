package net.xuset.tSquare.demo;

import java.util.ArrayList;
import java.util.Iterator;

import net.xuset.tSquare.game.Game;
import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.ScaledGraphics;
import net.xuset.tSquare.imaging.TsColor;
import net.xuset.tSquare.math.point.IPointR;
import net.xuset.tSquare.math.point.Point;
import net.xuset.tSquare.system.IDrawBoard;
import net.xuset.tSquare.system.Network;
import net.xuset.tSquare.system.input.mouse.IMouseListener;
import net.xuset.tSquare.system.input.mouse.MousePointer;
import net.xuset.tSquare.system.input.mouse.ScaledMouseListener;

public class Demo {
	private final DemoGame demoGame;
	private Thread thread = null;
	
	public Demo(IDrawBoard drawBoard) {
		demoGame = new DemoGame(drawBoard);
	}
	
	public void start() {
		if (thread == null || !thread.isAlive()) {
			thread = new Thread(demoGame);
			thread.start();
		}
	}
	
	private static class Circle {
		private final long timeCreated;
		private final IPointR point;
		
		public Circle(double x, double y) {
			timeCreated = System.currentTimeMillis();
			point = new Point(x, y);
		}
		
		@Override
		public boolean equals(Object o) {
			if (o instanceof Circle) {
				Circle c = (Circle) o;
				return c.point.equals(this.point);
			}
			return super.equals(o);
		}
	}
	
	private static class DemoGame extends Game{
		private static final float scale = 2.0f;
		
		private final ArrayList<Circle> circles = new ArrayList<Circle>();
		private final IDrawBoard drawBoard;
		private final IMouseListener input;
		
		public DemoGame(IDrawBoard drawBoard) {
			super(Network.createOffline());
			this.drawBoard = drawBoard;
			
			
			input = new ScaledMouseListener(drawBoard.createInputListener().getMouse(), scale);
			//input.getMouse().watch(new MouseObserver());
		}

		@Override
		protected void logicLoop() {
			handleInput();
			removeOldCirlces();
		}

		@Override
		protected void displayLoop() {
			IGraphics g = drawBoard.getGraphics();
			if (g != null) {
				g = new ScaledGraphics(g, scale);
				g.clear();
				g.setAntiAlias(true);
				
				drawCircles(g);
				
				drawBoard.flushScreen();
			}
		}
		
		private void drawCircles(IGraphics g) {
			final float diameter = 50.0f;
			g.setColor(TsColor.red);
			synchronized(circles) {
				for (Circle c : circles) {
					float x = (float) (c.point.getX() - diameter / 2);
					float y = (float) (c.point.getY() - diameter / 2);
					g.drawOval(x, y, diameter, diameter);
				}
			}
		}
		
		private void removeOldCirlces() {
			final long timeAlive = 500; //ms
			synchronized(circles) {
				for (Iterator<Circle> it = circles.iterator(); it.hasNext(); ) {
					Circle c = it.next();
					if (c.timeCreated + timeAlive < System.currentTimeMillis())
						it.remove();
				}
			}
		}
		
		private void handleInput() {
			for (int i = 0; i < input.getPointerCount(); i++) {
				MousePointer pointer = input.getPointerByIndex(i);
				if (pointer.isPressed())
					attemptToAddCircle((int) pointer.getX(), (int) pointer.getY());
			}
		}
		
		private void attemptToAddCircle(int x, int y) {
			synchronized(circles) {
				Circle c = new Circle(x, y);
				circles.remove(c);
				circles.add(c);
			}
		}
		
		/*private class MouseObserver implements Observer.Change<TsMouseEvent> {

			@Override
			public void observeChange(TsMouseEvent t) {
				synchronized(circles) {
					Circle c = new Circle(t.x, t.y);
					circles.remove(c);
					circles.add(c);
				}
			}
			
		}*/
	}
}
