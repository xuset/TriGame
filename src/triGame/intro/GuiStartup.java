package triGame.intro;

import java.awt.Dimension;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import tSquare.system.Network;
import triGame.game.CmdStartup;
import triGame.game.TriGame;
import triGame.intro.GameMode.MODES;

public class GuiStartup {

	public static void main(String[] args) throws IOException {
		if (args.length != 0)
			CmdStartup.main(args);
		else {
			TriGame game = new TriGame(gatherPlayers());
			game.load();
			game.startGame();
		}
	}
	
	private static Network gatherPlayers() {

		JFrame frame = new JFrame(); //start display where all panels are displayed
		frame.setPreferredSize(new Dimension(400, 100));
		frame.setTitle("TriGame");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		frame.add(panel);
		frame.pack();
		
		GameMode gm = new GameMode(); //show game mode panel
		panel.add(gm);
		panel.updateUI();
		MODES mode = gm.getGameMode(); //get chosen game mode
		
		AddressInfo ai = new AddressInfo(mode); //show address/port port
		panel.removeAll();
		panel.add(ai);
		panel.updateUI();
		Network net = ai.getNetwork(); //get connected network
		
		if (mode == MODES.HOST || mode == MODES.JOIN) { //if multiplayer
			Lobby lb = new Lobby(net); //display lobby so all players can join
			panel.removeAll();
			panel.add(lb);
			panel.updateUI();
			lb.waitForPlayers(); //wait for all players to join
		}
		
		frame.dispose();
		
		return net; //all players joined and connected
	}

}
