package triGame.intro;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;

import triGame.intro.GameInfo.NetworkType;

class GameMode extends JPanel{
	private static final long serialVersionUID = 4135487615630400457L;

	private final JButton btnSolo = new JButton("Solo");
	private final JButton btnHost = new JButton("Host");
	private final JButton btnJoin = new JButton("Join");
	private NetworkType chosen = null;
	
	GameMode() {
		
		btnSolo.addActionListener(soloListener);
		btnHost.addActionListener(hostListener);
		btnJoin.addActionListener(joinListener);
		
		add(Box.createHorizontalGlue());
		add(btnSolo);
		add(Box.createHorizontalGlue());
		add(btnHost);
		add(Box.createHorizontalGlue());
		add(btnJoin);
		add(Box.createHorizontalGlue());
	}
	
	NetworkType getGameMode() {
		while (chosen == null) {
			try { Thread.sleep(10); } catch (Exception ex) { }
		}
		return chosen;
	}
	
	private ActionListener soloListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			chosen = NetworkType.SOLO;
		}
	};
	
	private ActionListener hostListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			chosen = NetworkType.HOST;
		}
	};
	
	private ActionListener joinListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			chosen = NetworkType.JOIN;
		}
	};
}
