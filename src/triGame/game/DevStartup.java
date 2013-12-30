package triGame.game;

import java.io.IOException;

import tSquare.math.IdGenerator;
import tSquare.system.Network;
import triGame.game.GameMode.GameType;
import triGame.intro.GameInfo;
import triGame.intro.GameInfo.NetworkType;

public class DevStartup {
	private static final GameType gameType = GameType.SURVIVAL;
	private static final boolean multiplayer = true;
	
	public static void main(String[] main) {
		if (!System.getProperty("os.name").toLowerCase().contains("windows"))
			System.setProperty("sun.java2d.opengl", "True");
		new Server();
		if (multiplayer)
			new Client();

	}
	
	private static class Server extends Thread{
		public Server() {
			start();
		}
		
		public void run() {
			try {
				Network network = Network.startupServer(3000);
				if (multiplayer)
					network.waitForClientsToConnect(1, Integer.MAX_VALUE);
				GameInfo info = new GameInfo(network, gameType, NetworkType.HOST);
				TriGame g = new TriGame(info);
				g.startGame();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static class Client extends Thread{
		public Client() {
			start();
		}
		
		public void run() {
			try {
				Network network = Network.connectToServer("127.0.0.1", 3000, IdGenerator.getNext());
				GameInfo info = new GameInfo(network, gameType, NetworkType.JOIN);
				TriGame g = new TriGame(info);
				g.startGame();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
