package triGame.game.playerInterface;

import triGame.game.TriGame;



public class PlayerInterface {
	public static final int WIDTH = 175;
	
	private Container container;
	
	public Container getContainer() { return container; }
	
	public PlayerInterface(TriGame game) {
		container = new Container(game);
	}
	
	public void displayPointsAndRound() {
		container.displayPointsAndRound();
	}
	
	public Attacher getAttacher() {
		return container.getAttacher();
	}
}
