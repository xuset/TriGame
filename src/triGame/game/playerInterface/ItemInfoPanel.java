package triGame.game.playerInterface;


import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import triGame.game.entities.wall.Barrier;


public class ItemInfoPanel extends JPanel{
	private static final long serialVersionUID = -8198956458375553032L;
	
	private JLabel itemCostLabel;
	private JLabel itemNameLabel;
	
	public ItemInfoPanel() {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createTitledBorder("Item info"));
		this.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.setAlignmentY(Component.TOP_ALIGNMENT);
		itemCostLabel = new JLabel("", JLabel.CENTER);
		itemNameLabel = new JLabel("", JLabel.CENTER);
		this.add(itemNameLabel);
		this.add(itemCostLabel);
		setCost(Barrier.NEW_BARRIER.getCost());
		setName(Barrier.NEW_BARRIER.getName());
		
	}
	
	public void setCost(String text) {
		itemCostLabel.setText("Cost: " + text);
	}
	
	public void setCost(int cost) {
		itemCostLabel.setText("Cost: " + cost);
	}
	
	public void setItemName(String name) {
		itemNameLabel.setText("Name: " + name);
	}
}
