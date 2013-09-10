package triGame.game;

import java.io.IOException;

import tSquare.math.IdGenerator;
import tSquare.system.Network;

public class CmdStartup {
	public static void main(String[] args) throws IOException {
		if (args.length == 1 && (args[0] == "--help" || args[0] == "-h" || args[0] == "/h")) {
			displayHelp();
		} else if (args.length < 3){
			boolean wait = true;
			Network network = null;
			if (args.length == 0) {
				network = Network.startupServer(3000, 3l);
				wait = false;
			} else if (args.length == 1) {
				int port = Integer.parseInt(args[0]);
				network = Network.startupServer(port, 3l);
			} else if (args.length == 2) {
				int port = Integer.parseInt(args[0]);
				network = Network.connectToServer(args[1], port, IdGenerator.getInstance().getId());
			}
			if (network != null) {
				TriGame g = new TriGame(network, wait);
				g.load();
				g.startGame();
			} else {
				displayHelp();
			}
		}
	}
	
	public static void displayHelp() {
		System.out.println("Trigame");
		System.out.println("to start server");
		System.out.println("     java -jar jarFile [port]");
		System.out.println("to connect to server");
		System.out.println("     java -jar jarFile [port] [host]");
		System.out.println("exclude the braces");
	}
}
