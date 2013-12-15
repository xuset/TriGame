package triGame.intro;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;

class GameMode extends JPanel{
	enum Modes { SOLO, HOST, JOIN }
	private static final long serialVersionUID = 4135487615630400457L;

	private final JButton btnSolo = new JButton("Solo");
	private final JButton btnHost = new JButton("Host");
	private final JButton btnJoin = new JButton("Join");
	private Modes chosen = null;
	
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
	
	Modes getGameMode() {
		while (chosen == null) {
			try { Thread.sleep(10); } catch (Exception ex) { }
		}
		return chosen;
	}
	
	private ActionListener soloListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			chosen = Modes.SOLO;
		}
	};
	
	private ActionListener hostListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			chosen = Modes.HOST;
		}
	};
	
	private ActionListener joinListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			chosen = Modes.JOIN;
		}
	};
}
