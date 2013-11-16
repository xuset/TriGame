package triGame.game;

import java.io.IOException;

import tSquare.math.IdGenerator;
import tSquare.system.Network;

public class CmdStartup {
	public static void main(String[] args) throws IOException {
		if (args.length == 0 || args.length > 3|| args.length == 1 && (args[0] == "--help" || args[0] == "-h" || args[0] == "/h")) {
			displayHelp();
		} else {
			Network network = null;
			if (args[0].equals("-solo")) {
				network = Network.startupServer(3000);
			} else if (args[0].equals("-host")) {
				int port = Integer.parseInt(args[1]);
				int numOfPlayers = Integer.parseInt(args[2]);
				network = Network.startupServer(port);
				System.out.println("Waiting for " + numOfPlayers + "players to connect");
				network.waitForClientsToConnect(numOfPlayers, Integer.MAX_VALUE);
			} else if (args[0].equals("-join")) {
				int port = Integer.parseInt(args[1]);
				network = Network.connectToServer(args[2], port, IdGenerator.getNext());
			}
			if (network != null) {
				TriGame g = new TriGame(network);
				g.startGame();
			} else {
				displayHelp();
			}
		}
	}
	
	public static void displayHelp() {
		System.out.println("Trigame");
		System.out.println("-to play solo");
		System.out.println("	-java -jar jarFile -solo");
		System.out.println("-to start server");
		System.out.println("     -java -jar jarFile -host [port] [num of players]");
		System.out.println("-to connect to server");
		System.out.println("     -java -jar jarFile -join[port] [host]");
		System.out.println("exclude the braces");
	}
}
