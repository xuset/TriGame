package net.xuset.tSquare.demo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.IOException;

import net.xuset.tSquare.system.DrawBoard;
import net.xuset.tSquare.game.Game;
import net.xuset.tSquare.game.entity.Entity;
import net.xuset.tSquare.game.entity.EntityManager;
import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.IImage;
import net.xuset.tSquare.imaging.ImageFactory;
import net.xuset.tSquare.imaging.Sprite;
import net.xuset.tSquare.system.Display;
import net.xuset.tSquare.system.Network;
import net.xuset.tSquare.system.input.InputHolder;
import net.xuset.tSquare.system.input.keyboard.IKeyListener;
import net.xuset.tSquare.system.input.keyboard.KeyAction;
import net.xuset.tSquare.system.input.keyboard.TsKeyEvent;

public class MultiplayerDemo {

	public static void main(String[] args) {
		new Server();
		new Client();
		//new Offline();
	}
	
	public static class MyGame extends Game {
		Display display;
		DrawBoard drawBoard;
		InputHolder input;

		EntityManager manager;
		Entity player;
		
		boolean up = false;
		boolean down = false;
		boolean left = false;
		boolean right = false;
		
		public MyGame(Network network) {
			super(network);
			display = new Display(800, 600);
			drawBoard = new DrawBoard(800, 600);
			display.frame.add(drawBoard.getBackend(), BorderLayout.CENTER);
			display.finishInit();
			
			input = drawBoard.createInputListener();
			
			manager = new EntityManager(this.managerController);
			
			createCircle("red", Color.red);
			createCircle("blue", Color.blue);
			
			if (network.isServer)
				player = manager.create("red", 0, 50);
			else
				player = manager.create("blue", 0, 200);
		}

		@Override
		protected void logicLoop() {
			handleInput();
			
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
			
			manager.updateList();
		}

		@Override
		protected void displayLoop() {
			IGraphics g = drawBoard.getGraphics();
			g.clear();
			manager.draw(g);
			drawBoard.flushScreen();
		}
		
		private void handleInput() {
			TsKeyEvent key = null;
			IKeyListener keyboard = input.getKeyboard();
			while ((key = keyboard.pollEvent()) != null) {
				switch(key.key) {
				case 'w':
					up = key.action == KeyAction.PRESS;
					break;
				case 's':
					down = key.action == KeyAction.PRESS;
					break;
				case 'a':
					left = key.action == KeyAction.PRESS;
					break;
				case 'd':
					right = key.action == KeyAction.PRESS;
					break;
				}
			}
		}
		
		private Sprite createCircle(String name, Color c) {
			IImage image = new ImageFactory().createEmpty(70, 70);
			IGraphics g = image.getGraphics();
			g.setColor(c.getRGB());
			g.drawRect(0, 0, 70, 70);
			
			return Sprite.add(new Sprite(name, image, 1.0f));
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
	
	public static class Offline {
		public Offline() {
			new MyGame(Network.createOffline()).startGame();
		}
	}
}



