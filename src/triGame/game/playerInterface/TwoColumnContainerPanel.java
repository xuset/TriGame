package triGame.game.playerInterface;

import java.awt.Component;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class TwoColumnContainerPanel extends JPanel{
	private static final long serialVersionUID = 9068644278064811129L;

	private JPanel leftColumn = new JPanel();
	private JPanel rightColumn = new JPanel();
	
	public TwoColumnContainerPanel() {
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.setAlignmentY(Component.TOP_ALIGNMENT);
		this.setAlignmentX(Component.CENTER_ALIGNMENT);
		leftColumn.setLayout(new BoxLayout(leftColumn, BoxLayout.Y_AXIS));
		leftColumn.setAlignmentY(Component.TOP_ALIGNMENT);
		rightColumn.setLayout(new BoxLayout(rightColumn, BoxLayout.Y_AXIS));
		rightColumn.setAlignmentY(Component.TOP_ALIGNMENT);
		this.add(leftColumn);
		this.add(Box.createHorizontalStrut(25));
		this.add(rightColumn);
	}
	
	public Component[] getLeftColumnComponents() {
		return leftColumn.getComponents();
	}
	
	public Component[] getRightColumnComponents() {
		return rightColumn.getComponents();
	}
	
	public void addArsenal(Component comp) {
		if (leftColumn.getComponentCount() == rightColumn.getComponentCount()) {
			if (leftColumn.getComponentCount() != 0)
				leftColumn.add(Box.createVerticalGlue());
			leftColumn.add(comp);
		} else {
			if (rightColumn.getComponentCount() != 0)
				rightColumn.add(Box.createVerticalGlue());
			rightColumn.add(comp);
		}
	}
}
