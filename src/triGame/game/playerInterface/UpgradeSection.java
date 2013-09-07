package triGame.game.playerInterface;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import triGame.game.shopping.UpgradeItem;

public class UpgradeSection extends JPanel implements MouseListener{
	private static final long serialVersionUID = 4767675831956771198L;
	
	private JLabel fractionLabel = new JLabel("---", JLabel.LEFT);
	private JButton upgradeButton = new JButton("Upgrade");
	private UpgradeItem item;
	private UpgradePanel panel;
	
	public UpgradeSection(UpgradePanel panel) {
		this.panel = panel;
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.setBorder(BorderFactory.createTitledBorder("Upgrades"));
		this.setAlignmentX(Component.CENTER_ALIGNMENT);
		upgradeButton.addMouseListener(this);
		this.add(fractionLabel);
		this.add(Box.createHorizontalGlue());
		this.add(upgradeButton);
	}
	
	public void setUpgradeItem(UpgradeItem item) {
		this.item = item;
		if (item == null) {
			this.fractionLabel.setText("---");
			this.upgradeButton.setEnabled(false);
			this.setBorder(BorderFactory.createTitledBorder("Upgrades"));
		} else {
			this.fractionLabel.setText((item.upgradeCount) + "/" + item.maxUpgrades);
			this.setBorder(BorderFactory.createTitledBorder(item.shopItem.getName()));
			if (item.upgradeCount < item.maxUpgrades)
				upgradeButton.setEnabled(true);
			else
				upgradeButton.setEnabled(false);
		}
		
	}
	
	public void mouseClicked(MouseEvent arg0) {
		if (item != null) {
			panel.getUpgradeManager().upgrade(item);
			setUpgradeItem(item);
		}
	}
	
	public void mouseEntered(MouseEvent arg0) { }
	public void mouseExited(MouseEvent arg0) { }
	public void mousePressed(MouseEvent arg0) { }
	public void mouseReleased(MouseEvent arg0) { }
}
