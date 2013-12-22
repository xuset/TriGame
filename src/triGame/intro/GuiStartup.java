package triGame.intro;

import java.io.IOException;

import triGame.game.TriGame;
import triGame.intro.GameInfo.NetworkType;

public class GuiStartup {

	public static void main(String[] args) throws IOException {
		if (!System.getProperty("os.name").toLowerCase().contains("windows"))
			System.setProperty("sun.java2d.opengl", "True");
	
		//ErrorHandler handler = new ErrorHandler(new ErrorHandler.Task() {
		//	@Override public void run() {
				TriGame game = new TriGame(GuiStartup.gatherPlayers());
				game.startGame();
				game.shutdown();
		//	}
		//});
		
		//handler.startTask();
		System.exit(0);
	}
	
	private static GameInfo gatherPlayers() {
		GameMode gm = new GameMode();
		IntroPanel introPanel = new IntroPanel(gm);
		
		NetworkType mode = gm.getGameMode(); //get chosen game mode

		AddressInfo ai = new AddressInfo(mode); //show address/port port
		introPanel.reconstructContainer(ai);

		GameInfo gameInfo = ai.getGameInfo(); //get connected network
		
		if (mode == NetworkType.HOST || mode == NetworkType.JOIN) { //if multiplayer
			Lobby lb = new Lobby(gameInfo); //display lobby so all players can join
			introPanel.reconstructContainer(lb);
			lb.waitForPlayers(); //wait for all players to join
		}
		
		introPanel.dispose();
		
		return gameInfo; //all players joined and connected
	}
}
