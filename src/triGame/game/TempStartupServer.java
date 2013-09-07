package triGame.game;

import tSquare.system.Network;


class TempStartupServer {
	
	public static void main(String[] args) {
		Network network = Network.startupServer(3000, 3l);
		TriGame g = new TriGame(network, true);
		g.load();
		g.startGame();
	}

}
