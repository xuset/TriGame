package triGame.game;

import java.io.IOException;

import tSquare.math.IdGenerator;
import tSquare.system.Network;

class TempStartupClient {

	public static void main(String[] args) throws IOException {
		Network network = Network.connectToServer("127.0.0.1", 3000, IdGenerator.getInstance().getId());
		TriGame g = new TriGame(network);
		g.load();
		g.startGame();
	}

}
