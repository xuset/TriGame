package triGame.game;

import java.net.UnknownHostException;

import tSquare.math.IdGenerator;
import tSquare.system.Network;

class TempStartupClient {

	public static void main(String[] args) throws UnknownHostException {
		Network network = Network.connectToServer("127.0.0.1", 3000, IdGenerator.getInstance().getId());
		TriGame g = new TriGame(network, false);
		g.load();
		g.startGame();
	}

}
