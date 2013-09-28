package triGame.intro;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import tSquare.math.IdGenerator;
import tSquare.system.Network;
import triGame.intro.GameMode.MODES;

public class AddressInfo extends JPanel{
	private static final long serialVersionUID = 7521351042232726261L;
	
	private JLabel lblHint = new JLabel();
	private JLabel lblError = new JLabel();
	private JPanel pnlInput = new JPanel();
	private JTextField txtAddress = new JTextField();
	private JButton btnConnect = new JButton();
	
	private Network network = null;
	private boolean hosting;
	
	AddressInfo(MODES mode) {
		switch(mode) {
		case SOLO:
			construct(true);
			txtAddress.setText("3000");
			conctListener.mouseClicked(null);
			break;
		case HOST:
			construct(true);
			break;
		case JOIN:
			construct(false);
			break;
		}
	}
	
	private void construct(boolean hosting) {
		this.hosting = hosting;
		lblError.setForeground(Color.red);
		pnlInput.setLayout(new BoxLayout(pnlInput, BoxLayout.X_AXIS));
		txtAddress.setPreferredSize(new Dimension(200, 20));
		btnConnect.addMouseListener(conctListener);
		add(lblHint);
		add(lblError);
		pnlInput.add(txtAddress);
		pnlInput.add(btnConnect);
		add(pnlInput);
		if (hosting)
			hostSetup();
		else
			joinSetup();
	}
	
	private void hostSetup() {
		lblHint.setText("Port number");
		btnConnect.setText("host");
	}
	
	private void joinSetup() {
		lblHint.setText("IP:PORT ex 192.168.1.7:3000");
		btnConnect.setText("join");
	}
	
	private MouseListener conctListener = new MouseListener() {
		@Override
		public void mouseClicked(MouseEvent arg0) {
			printError("");
			if (hosting) {
				try {
					int port = Integer.parseInt(txtAddress.getText());
					network = Network.startupServer(port);
				} catch (NumberFormatException ex) {
					printError("Invalid format!");
				} catch (IOException ex) {
					printError(ex.getMessage());
				}
			} else {
				try {
					String[] split = txtAddress.getText().split(":");
					String address = split[0];
					int port = Integer.parseInt(split[1]);
					network = Network.connectToServer(address, port, IdGenerator.getInstance().getId());
				} catch (UnknownHostException ex) {
					printError(ex.getMessage());
				} catch (Exception ex) {
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
	
	Network getNetwork() {
		while (network == null) {
			try { Thread.sleep(10); } catch (Exception ex) { }
		}
		return network;
	}

}
