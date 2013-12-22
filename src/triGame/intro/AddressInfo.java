package triGame.intro;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import tSquare.math.IdGenerator;
import tSquare.system.Network;
import triGame.game.GameMode.GameType;
import triGame.intro.GameInfo.NetworkType;

public class AddressInfo extends JPanel{
	private static final long serialVersionUID = 7521351042232726261L;
	
	private final JLabel lblHint = new JLabel();
	private final JLabel lblError = new JLabel();
	private final JPanel pnlInput = new JPanel();
	private final JTextField txtAddress = new JTextField();
	private final JButton btnConnect = new JButton();
	private final ButtonGroup modeGroup = new ButtonGroup();
	private final JRadioButton btnSurvival = new JRadioButton();
	private final JRadioButton btnVersus = new JRadioButton();
	private final boolean hosting;
	
	private GameInfo gameInfo = new GameInfo();
	
	AddressInfo(NetworkType mode) {
		gameInfo.networkType = mode;
		hosting = (mode == NetworkType.HOST || mode == NetworkType.SOLO);
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		lblHint.setAlignmentX(CENTER_ALIGNMENT);
		lblError.setForeground(Color.red);
		lblError.setAlignmentX(CENTER_ALIGNMENT);
		txtAddress.setPreferredSize(new Dimension(200, 20));
		btnConnect.addMouseListener(conctListener);

		btnSurvival.setSelected(true);
		btnSurvival.setText("Survival");
		btnVersus.setText("Versus");
		modeGroup.add(btnSurvival);
		modeGroup.add(btnVersus);
		
		add(lblHint);
		add(lblError);
		pnlInput.add(txtAddress);
		pnlInput.add(btnConnect);
		add(pnlInput);
		
		if (hosting)
			hostSetup();
		else
			joinSetup();
		
		if (mode == NetworkType.SOLO)
			conctListener.mouseClicked(null);
	}
	
	GameInfo getGameInfo() {
		while (gameInfo.network == null) {
			try { Thread.sleep(10); } catch (Exception ex) { }
		}
		return gameInfo;
	}
	
	private void hostSetup() {
		lblHint.setText("Port number");
		btnConnect.setText("host");
		txtAddress.setText("3000");
		add(btnSurvival);
		add(btnVersus);
	}
	
	private void joinSetup() {
		lblHint.setText("ip:port example 192.168.1.7:3000");
		btnConnect.setText("join");
	}
	
	private MouseListener conctListener = new MouseListener() {
		@Override
		public void mouseClicked(MouseEvent arg0) {
			printError("");
			if (gameInfo.network != null)
				gameInfo.network.disconnect();
			if (hosting) {
				try {
					int port = Integer.parseInt(txtAddress.getText());
					gameInfo.network = Network.startupServer(port);
					gameInfo.mode = (btnSurvival.isSelected()) ? GameType.SURVIVAL : GameType.VERSUS;
				} catch (NumberFormatException ex) {
					printError("Invalid format!");
				} catch (IOException ex) {
					ex.printStackTrace();
					printError(ex.getMessage());
				}
			} else {
				try {
					String total = txtAddress.getText();
					int index = total.lastIndexOf(":");
					if (index == -1)
						throw new NumberFormatException();

					String address = total.substring(0, index);
					int port = Integer.parseInt(total.substring(index + 1));
					gameInfo.network = Network.connectToServer(address, port, IdGenerator.getNext());
				} catch (IOException ex) {
					ex.printStackTrace();
					printError(ex.getMessage());
				} catch (NumberFormatException ex) {
					printError("Invalid format!");
				}
			}
		}
		public void mouseEntered(MouseEvent arg0) { }
		public void mouseExited(MouseEvent arg0) { }
		public void mousePressed(MouseEvent arg0) { }
		public void mouseReleased(MouseEvent arg0) { }
	};
	
	private void printError(String error) {
		if (error != null && error != "")
			lblError.setText(txtAddress.getText() + " - " + error);
		else
			lblError.setText("");
	}
}
