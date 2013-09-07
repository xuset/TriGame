package triGame.game.playerInterface;


import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import tSquare.imaging.ImageProccess;
import tSquare.imaging.Sprite;
import triGame.game.TriGame;
import triGame.game.shopping.UpgradeManager;

public class UpgradePanel extends JPanel{
	private static final long serialVersionUID = -648238926436666505L;
	private static final int maxUpgradeSections = 3;
	private static final ImageIcon blankImage = new ImageIcon(ImageProccess.createCompatiableImage(TriGame.BLOCK_WIDTH, TriGame.BLOCK_HEIGHT));
	
	private JPanel upgradePreview = new JPanel();
	private JLabel previewIconLabel = new JLabel("", JLabel.CENTER);
	private UpgradeManager upgradeManager;
	private UpgradeSection[] sections = new UpgradeSection[maxUpgradeSections];
	
	public UpgradeManager getUpgradeManager() { return upgradeManager; }
	
	public UpgradePanel() {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createTitledBorder("Upgrade"));
		this.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.add(Box.createVerticalGlue());
		upgradePreview.setBorder(BorderFactory.createTitledBorder("Selected item"));
		upgradePreview.setAlignmentX(Component.CENTER_ALIGNMENT);
		previewIconLabel.setIcon(blankImage);
		upgradePreview.add(previewIconLabel);
		this.add(upgradePreview);
		this.setAlignmentX(Component.CENTER_ALIGNMENT);
		for (int i = 0; i < maxUpgradeSections; i++) {
			this.add(Box.createVerticalGlue());
			sections[i] = new UpgradeSection(this);
			this.add(sections[i]);
		}
	}
	
	public void clearUpgradeManager() {
		this.upgradeManager = null;
		this.previewIconLabel.setIcon(blankImage);
		this.previewIconLabel.setText("");
		for (int i = 0; i < sections.length; i++) {
			sections[i].setUpgradeItem(null);
		}
	}
	
	public void setUpgradeManager(UpgradeManager upgradeManager, String spriteId) {
		if (upgradeManager != null) {
			this.upgradeManager = upgradeManager;
			if (spriteId != null && Sprite.exists(spriteId))
				this.previewIconLabel.setIcon(new ImageIcon(Sprite.get(spriteId).getImage()));
			else
				this.previewIconLabel.setIcon(blankImage);
			this.previewIconLabel.setText(upgradeManager.getName());
			for (int i = 0; i < sections.length; i++) {
				if (i < upgradeManager.items.size())
					sections[i].setUpgradeItem(upgradeManager.items.get(i));
				else
					sections[i].setUpgradeItem(null);
			}
		}
		else
			clearUpgradeManager();
	}
}
