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
			TriGame game = new TriGame(gatherPlayers(), false);
			game.load();
			game.startGame();
		}
	}
	
	private static Network gatherPlayers() {

		JFrame frame = new JFrame();
		frame.setPreferredSize(new Dimension(400, 100));
		frame.setTitle("TriGame");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		frame.add(panel);
		frame.pack();
		
		GameMode gm = new GameMode();
		panel.add(gm);
		MODES mode = gm.getGameMode();
		
		AddressInfo ai = new AddressInfo(mode);
		panel.removeAll();
		panel.add(ai);
		panel.updateUI();
		Network net = ai.getNetwork();
		
		if (mode == MODES.HOST || mode == MODES.JOIN) {
			Lobby lb = new Lobby(net);
			panel.removeAll();
			panel.add(lb);
			panel.updateUI();
			lb.waitForPlayers();
		}
		
		frame.dispose();
		
		return net;
	}

}
