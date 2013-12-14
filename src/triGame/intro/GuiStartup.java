package triGame.intro;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
			TriGame game = new TriGame(new GuiStartup().gatherPlayers());
			game.startGame();
			game.shutdown();
		}
	}
	
	private GuiStartup() {
		versionThread = new Thread(new Runnable() {
				@Override public void run() {
					if (isNewerVersion()) {
						lblVersion.setText("A newer version is available for download!");
					}
				}
		});
		
		versionThread.start();
	}
	
	private Thread versionThread;
	private JLabel lblVersion = new JLabel();
	private final String myVersion = "1.0.0";
	private Network gatherPlayers() {

		JFrame frame = new JFrame(); //start display where all panels are displayed
		frame.setPreferredSize(new Dimension(400, 350));
		frame.setTitle("Attack of the Triangles!");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		frame.add(panel);
		frame.pack();
		
		JLabel lblAuthor = new JLabel("By: Austin Middleton", JLabel.CENTER);
		JLabel lblWebsite = new JLabel("https://github.com/xuset/", JLabel.CENTER);
		JLabel lblImage = new JLabel(new ImageIcon(loadImage("media/MainScreen.png")));
		JLabel lblCopyright = new JLabel("© Copyright 2013 (not really... shhhh)");
		JLabel lblLicense = new JLabel("Licensed under the GPLv2 (really)");
		lblVersion.setForeground(Color.red);
		lblVersion.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblAuthor.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblWebsite.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblImage.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblCopyright.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblLicense.setAlignmentX(Component.CENTER_ALIGNMENT);
		GameMode gm = new GameMode(); //show game mode panel
		panel.add(gm);
		panel.add(Box.createVerticalGlue());
		panel.add(lblImage);
		panel.add(Box.createVerticalGlue());
		panel.add(lblVersion);
		panel.add(lblAuthor);
		panel.add(lblWebsite);
		panel.add(lblCopyright);
		panel.add(lblLicense);
		panel.updateUI();
		MODES mode = gm.getGameMode(); //get chosen game mode
		
		if (versionThread.isAlive())
			versionThread.interrupt();

		AddressInfo ai = new AddressInfo(mode); //show address/port port
		panel.removeAll();
		frame.setSize(400, 100);
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
	
	private BufferedImage loadImage(String url) {
		File f = new File(url);
		try {
			if (f.exists() && f.isFile())
				return ImageIO.read(f);
			
			URL stream = getClass().getResource("/" + url);
			if (stream == null) {
				return null;
			}
			return ImageIO.read(stream);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	private boolean isNewerVersion() {
		try {
			URL github = new URL("https://raw.github.com/xuset/TriGame/master/version");
			InputStream stream = github.openStream();
			String contents = new BufferedReader(new InputStreamReader(stream)).readLine();
			return !contents.equals(myVersion);
			
		} catch (IOException e) {
			System.err.println("Error while contacting update site. " + e.getMessage());
		}
		return false;
	}

}
