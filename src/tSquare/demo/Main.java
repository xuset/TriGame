package tSquare.demo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;

import tSquare.game.DrawBoard;
import tSquare.game.Game;
import tSquare.game.GameBoard;
import tSquare.game.entity.Entity;
import tSquare.game.entity.EntityManager;
import tSquare.imaging.Sprite;
import tSquare.system.Display;
import tSquare.system.Network;
import tSquare.system.PeripheralInput;

public class Main {

	public static void main(String[] args) {
		new Server();
		new Client();
	}
	
	public static class MyGame extends Game {
		Display display;
		DrawBoard drawBoard;
		GameBoard gameBoard;
		PeripheralInput.Keyboard keyboard;

		EntityManager manager;
		Entity player;
		
		boolean scallingUp = true;
		
		public MyGame(Network network) {
			super(network);
			display = new Display(800, 600);
			drawBoard = new DrawBoard(800, 600, display);
			gameBoard = new GameBoard(800, 600, drawBoard);
			keyboard = new PeripheralInput().keyboard;
			drawBoard.addKeyListener(keyboard);
			
			manager = new EntityManager(this.managerController);
			
			createCircle("red", Color.red);
			createCircle("blue", Color.blue);
			
			if (network.isServer)
				player = manager.create("red", 0, 0);
			else
				player = manager.create("blue", 500, 500);
		}

		@Override
		protected void logicLoop() {
			boolean up = keyboard.isUpPressed();
			boolean down = keyboard.isDownPressed();
			boolean left = keyboard.isLeftPressed();
			boolean right = keyboard.isRightPressed();
			
			if (up)
				player.setAngle(90);
			if (down)
				player.setAngle(270);
			if (left)
				player.setAngle(180);
			if (right)
				player.setAngle(0);
			
			double distance = 100 * getDelta() / 1000;
			if (up || down || left || right) {
				player.moveForward(distance);
				while (player.collided(manager.list))
					player.moveForward(-1 * distance);
			}
			
			double scale = player.getScaleX();
			if (scallingUp)
				scale += 0.3 * this.getDelta() / 1000;
			else
				scale -= 0.3 * this.getDelta() / 1000;
			if (scale > 2)
				scallingUp = false;
			if (scale < 1)
				scallingUp = true;
			player.setScale(scale, scale);
		}

		@Override
		protected void displayLoop() {
			drawBoard.clearBoard();
			manager.draw(drawBoard.getDrawing(), gameBoard.viewable);
			drawBoard.exportToScreen();
		}
		
		private Sprite createCircle(String name, Color c) {
			BufferedImage image = new BufferedImage(70, 70, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = (Graphics2D) image.getGraphics();
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setColor(Color.black);
			g.fillRect(0, 0, 70, 70);
			g.setColor(c);
			g.fillOval(0, 0, 70, 70);
			g.dispose();
			return new Sprite(name, image);
		}
	}
	
	public static class Server extends Thread {
		public Server() {
			start();
		}
		
		@Override
		public void run() {
			try {
				Network sNet = Network.startupServer(3000);
				sNet.waitForClientsToConnect(1, Integer.MAX_VALUE);
				new MyGame(sNet).startGame();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static class Client extends Thread {
		public Client() {
			start();
		}
		
		@Override
		public void run() {
			try {
				Network cNet = Network.connectToServer("127.0.0.1", 3000, 9l);
				new MyGame(cNet).startGame();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
}



