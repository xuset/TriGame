package triGame.intro;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class GameMode extends JPanel{
	private static final long serialVersionUID = 4135487615630400457L;

	private JButton btnSolo = new JButton("Solo");
	private JButton btnHost = new JButton("Host");
	private JButton btnJoin = new JButton("Join");
	private MODES chosen = null;
	
	public static enum MODES { SOLO, HOST, JOIN }
	
	public GameMode() {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		btnSolo.addMouseListener(soloListener);
		btnHost.addMouseListener(hostListener);
		btnJoin.addMouseListener(joinListener);
		add(Box.createHorizontalGlue());
		add(btnSolo);
		add(Box.createHorizontalGlue());
		add(btnHost);
		add(Box.createHorizontalGlue());
		add(btnJoin);
		add(Box.createHorizontalGlue());
	}
	
	public MODES getGameMode() {
		while (chosen == null) {
			try { Thread.sleep(10); } catch (Exception ex) { }
		}
		return chosen;
	}
	
	private MouseListener soloListener = new MouseListener() {
		@Override
		public void mouseClicked(MouseEvent arg0) {
			chosen = MODES.SOLO;
		}
		public void mouseEntered(MouseEvent arg0) { }
		public void mouseExited(MouseEvent arg0) { }
		public void mousePressed(MouseEvent arg0) { }
		public void mouseReleased(MouseEvent arg0) { }
	};
	
	private MouseListener hostListener = new MouseListener() {
		@Override
		public void mouseClicked(MouseEvent arg0) {
			chosen = MODES.HOST;
		}
		public void mouseEntered(MouseEvent arg0) { }
		public void mouseExited(MouseEvent arg0) { }
		public void mousePressed(MouseEvent arg0) { }
		public void mouseReleased(MouseEvent arg0) { }
	};
	
	private MouseListener joinListener = new MouseListener() {
		@Override
		public void mouseClicked(MouseEvent arg0) {
			chosen = MODES.JOIN;
		}
		public void mouseEntered(MouseEvent arg0) { }
		public void mouseExited(MouseEvent arg0) { }
		public void mousePressed(MouseEvent arg0) { }
		public void mouseReleased(MouseEvent arg0) { }
	};
}
