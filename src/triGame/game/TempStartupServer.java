package triGame.game;

import java.io.IOException;

import tSquare.system.Network;


class TempStartupServer {
	
	public static void main(String[] args) throws IOException {
		Network network = Network.startupServer(3000);
		network.waitForClientsToConnect(1, Integer.MAX_VALUE);
		TriGame g = new TriGame(network);
		g.load();
		g.startGame();
	}

}
