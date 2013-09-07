package triGame.game.playerInterface;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import triGame.game.shopping.ShopManager;

public class PlayerInfoPanel extends JPanel{
	private static final long serialVersionUID = -7072565070399817780L;
	
	private JLabel pointLabel;
	private JLabel roundLabel;
	private ShopManager shop;
	
	
	public PlayerInfoPanel(ShopManager shop) {
		this.shop = shop;
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setAlignmentY(Component.TOP_ALIGNMENT);
		this.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.setBorder(BorderFactory.createTitledBorder("Player"));
		pointLabel = new JLabel();
		roundLabel = new JLabel();
		this.add(pointLabel);
		this.add(roundLabel);
	}
	
	public void setPointsAndRound(int roundNumber) {
		setPoints(shop.points);
		setRound(roundNumber);
	}
	
	public void setPoints(int points) {
		pointLabel.setText("Points: " + points);
	}
	
	public void setRound(int roundNumber) {
		roundLabel.setText("Round: " + roundNumber);
	}

}
