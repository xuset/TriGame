package triGame.game;

import java.io.IOException;

import tSquare.system.Network;


class TempStartupServer {
	
	public static void main(String[] args) throws IOException {
		Network network = Network.startupServer(3000, 3l);
		TriGame g = new TriGame(network, true);
		g.load();
		g.startGame();
	}

}
