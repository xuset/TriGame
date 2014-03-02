package net.xuset.tSquare.demo;

import net.xuset.tSquare.game.Game;
import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.IImage;
import net.xuset.tSquare.imaging.ImageFactory;
import net.xuset.tSquare.imaging.TsColor;
import net.xuset.tSquare.imaging.TsFont;
import net.xuset.tSquare.imaging.TsTypeFace;
import net.xuset.tSquare.math.point.IPointW;
import net.xuset.tSquare.math.point.Point;
import net.xuset.tSquare.system.IDrawBoard;
import net.xuset.tSquare.system.Network;
import net.xuset.tSquare.system.input.InputHolder;
import net.xuset.tSquare.system.input.keyboard.TsKeyEvent;
import net.xuset.tSquare.system.input.mouse.IMouseListener;
import net.xuset.tSquare.system.input.mouse.MouseAction;
import net.xuset.tSquare.system.input.mouse.TsMouseEvent;
import net.xuset.tSquare.ui.UiButton;
import net.xuset.tSquare.ui.UiComponent;
import net.xuset.tSquare.ui.UiController;
import net.xuset.tSquare.ui.UiForm;
import net.xuset.tSquare.util.Observer;

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
	
	private static class DemoGame extends Game{
		private final IDrawBoard drawBoard;
		private final InputHolder input;
		private final UiController ui;
		private final double speed = 200;
		private IPointW player = new Point(200, 200);
		private IPointW target = new Point(500, 500);
		private final IImage sprite;
		
		public DemoGame(IDrawBoard drawBoard) {
			super(Network.createOffline());
			this.drawBoard = drawBoard;
			sprite = createSprite();
			
			ui = createUI();
			
			input = drawBoard.createInputListener();
			input.getKeyboard().watch(new InputObserver());
		}

		@Override
		protected void logicLoop() {
			handleInput();
			
			if (player.calcDistance(target) > 5) {
				double moveDistance = speed * getDelta() / 1000.0;
				double angle = player.calcAngle(target);
				player.translate(Math.cos(angle) * moveDistance,
						-Math.sin(angle) * moveDistance);
			}
		}

		@Override
		protected void displayLoop() {
			IGraphics g = drawBoard.getGraphics();
			if (g != null) {
				g.clear();
				g.setAntiAlias(true);
				double spinAngle = player.calcAngle(target);
				g.drawImageRotate(sprite, (int) player.getX(), (int) player.getY(), spinAngle);
				
				g.setColor(new TsColor(0, 255, 255));
				g.setFont(new TsFont("Arial", 70, TsTypeFace.BOLD));
				String message = "GAME DEMO #1";
				float messageWidth = g.getTextWidth(message);
				int textX = (int) (g.getView().getWidth() / 2 - messageWidth / 2);
				g.drawText(textX, 200, message);
				
				ui.draw(g);
				
				drawBoard.flushScreen();
			}
		}
		
		private UiController createUI() {
			UiController ui = new UiController(drawBoard.createInputListener().getMouse());
			UiForm form = ui.getForm();
			
			UiComponent lblHello = new UiButton("Hello");
			lblHello.setLocation(50, 50);
			form.getLayout().add(lblHello);
			
			return ui;
		}
		
		private IImage createSprite() {
			IImage sprite = new ImageFactory().createEmpty(100, 100);
			IGraphics g = sprite.getGraphics();
			g.setColor(TsColor.darkGray);
			g.fillTriangle(0, 0, 100, 100);
			g.setColor(TsColor.cyan);
			g.fillTriangle(12, 12, 76, 76);
			g.dispose();
			return sprite;
		}
		
		private void handleInput() {
			TsMouseEvent e = null;
			IMouseListener mouse = input.getMouse();
			while ((e = mouse.pollEvent()) != null) {
				if (e.action == MouseAction.PRESS || e.action == MouseAction.DRAG) {
					target.setTo(e.x - sprite.getWidth() / 2, e.y - sprite.getHeight() / 2);
				}
			}
		}
		
		private class InputObserver implements Observer.Change<TsKeyEvent> {

			@Override
			public void observeChange(TsKeyEvent t) {
				
			}
			
		}
	}
}
