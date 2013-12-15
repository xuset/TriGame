package triGame.intro;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import tSquare.math.IdGenerator;
import tSquare.system.Network;
import triGame.intro.GameMode.Modes;

public class AddressInfo extends JPanel{
	private static final long serialVersionUID = 7521351042232726261L;
	
	private final JLabel lblHint = new JLabel();
	private final JLabel lblError = new JLabel();
	private final JPanel pnlInput = new JPanel();
	private final JTextField txtAddress = new JTextField();
	private final JButton btnConnect = new JButton();
	private final boolean hosting;
	
	private Network network = null;
	
	AddressInfo(Modes mode) {
		hosting = (mode == Modes.HOST || mode == Modes.SOLO);
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		lblHint.setAlignmentX(CENTER_ALIGNMENT);
		lblError.setForeground(Color.red);
		lblError.setAlignmentX(CENTER_ALIGNMENT);
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
		
		if (mode == Modes.SOLO)
			conctListener.mouseClicked(null);
	}
	
	Network getNetwork() {
		while (network == null) {
			try { Thread.sleep(10); } catch (Exception ex) { }
		}
		return network;
	}
	
	private void hostSetup() {
		lblHint.setText("Port number");
		btnConnect.setText("host");
		txtAddress.setText("3000");
	}
	
	private void joinSetup() {
		lblHint.setText("ip:port example 192.168.1.7:3000");
		btnConnect.setText("join");
	}
	
	private MouseListener conctListener = new MouseListener() {
		@Override
		public void mouseClicked(MouseEvent arg0) {
			printError("");
			if (network != null)
				network.disconnect();
			if (hosting) {
				try {
					int port = Integer.parseInt(txtAddress.getText());
					network = Network.startupServer(port);
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
					network = Network.connectToServer(address, port, IdGenerator.getNext());
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
