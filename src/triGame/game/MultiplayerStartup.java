package triGame.game;

import java.io.IOException;

import tSquare.math.IdGenerator;
import tSquare.system.Network;

public class MultiplayerStartup {
	public static void main(String[] main) {
		new Server();
		new Client();

	}
	
	private static class Server extends Thread{
		public Server() {
			start();
		}
		
		public void run() {
			try {
				Network network = Network.startupServer(3000);
				network.waitForClientsToConnect(1, Integer.MAX_VALUE);
				TriGame g = new TriGame(network);
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
				TriGame g = new TriGame(network);
				g.startGame();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
