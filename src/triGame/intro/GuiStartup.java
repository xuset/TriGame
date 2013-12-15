package triGame.intro;

import java.io.IOException;

import tSquare.system.Network;
import triGame.game.TriGame;
import triGame.intro.GameMode.Modes;

public class GuiStartup {

	public static void main(String[] args) throws IOException {
		if (!System.getProperty("os.name").toLowerCase().contains("windows"))
			System.setProperty("sun.java2d.opengl", "True");
	
		ErrorHandler handler = new ErrorHandler(new ErrorHandler.Task() {
			@Override public void run() {
				TriGame game = new TriGame(GuiStartup.gatherPlayers());
				game.startGame();
				game.shutdown();
			}
		});
		
		handler.startTask();
		System.exit(0);
	}
	
	private static Network gatherPlayers() {
		GameMode gm = new GameMode();
		IntroPanel introPanel = new IntroPanel(gm);
		
		Modes mode = gm.getGameMode(); //get chosen game mode

		AddressInfo ai = new AddressInfo(mode); //show address/port port
		introPanel.reconstructContainer(ai);

		Network net = ai.getNetwork(); //get connected network
		
		if (mode == Modes.HOST || mode == Modes.JOIN) { //if multiplayer
			Lobby lb = new Lobby(net); //display lobby so all players can join
			introPanel.reconstructContainer(lb);
			lb.waitForPlayers(); //wait for all players to join
		}
		
		introPanel.dispose();
		
		return net; //all players joined and connected
	}
}
