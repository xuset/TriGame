package triGame.intro;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import tSquare.imaging.ImageProcess;
import triGame.game.Params;

class IntroPanel {
	private final JFrame frame = new JFrame();
	private final JPanel container = new JPanel();
	private final JPanel pnlText = new JPanel();
	private final JLabel lblNewer = new JLabel("");
	private final JLabel lblImage;
	private final JLabel lblAuthor = new JLabel("By: Austin Middleton (xuset1@gmail.com)");
	private final JLabel lblWebsite = new JLabel("https://github.com/xuset/");
	private final JLabel lblCopyright = new JLabel("© Copyright 2013 (not really... shhhh)");
	private final JLabel lblLicense = new JLabel("Licensed under the GPLv2 (really)");
	private final Thread newVersionChecker;
	private boolean newerVersion = false;
	
	IntroPanel(JPanel firstPanel) {
		lblImage = new JLabel(new ImageIcon(ImageProcess.loadImage("media/MainScreen.png")));
		
		frame.setPreferredSize(new Dimension(600, 400));
		frame.setTitle("Attack of the Triangles!");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		pnlText.setLayout(new BoxLayout(pnlText, BoxLayout.Y_AXIS));
		
		lblNewer.setForeground(Color.red);
		lblImage.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblAuthor.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblWebsite.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblCopyright.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblLicense.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		pnlText.add(lblAuthor);
		pnlText.add(lblWebsite);
		pnlText.add(lblCopyright);
		pnlText.add(lblLicense);
		
		frame.add(container);		
		reconstructContainer(firstPanel);
		frame.setVisible(true);
		
		newVersionChecker = new Thread(setNewVersionLabel);
		newVersionChecker.setName("Updater");
		newVersionChecker.start();
	}
	
	void dispose() {
		frame.dispose();
		newVersionChecker.interrupt();
	}
	
	void reconstructContainer(JPanel toAdd) {
		container.removeAll();
		toAdd.setBorder(BorderFactory.createEtchedBorder());
		container.add(toAdd);
		container.add(Box.createVerticalGlue());
		container.add(lblImage);
		container.add(Box.createVerticalGlue());
		container.add(lblNewer);
		container.add(Box.createVerticalGlue());
		container.add(pnlText);
		container.add(Box.createVerticalGlue());
		frame.pack();
	}
	
	private void resetVersionLabel() {
		if (newerVersion) {
			lblNewer.setText("<html><body style='width:" +  frame.getWidth() +
					";' ><div style='text-align:center; margin-left:auto;" +
					" margin-right:auto;'>A newer version is available for " +
					"download! https://github.com/xuset/TriGame/releases</div></body></html>");	
		}
	}
	
	private final Runnable setNewVersionLabel = new Runnable() {

		@Override
		public void run() {
			try {
				URL github = new URL("https://raw.github.com/xuset/TriGame/master/version");
				InputStream stream = github.openStream();
				String contents = new BufferedReader(new InputStreamReader(stream)).readLine();
				//TODO get rid of the readline() call. it blocks and doesn't check the thread's interrupt flag.
				newerVersion = (!contents.equals(Params.VERSION));
				resetVersionLabel();
			} catch (IOException e) {
				System.err.println("Error while contacting update site. " + e.getMessage());
			}
		}
		
	};
	
	
}
