package tSquare.util;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PopUp implements MouseListener {
	public JFrame frame = new JFrame();
	public JPanel panel = new JPanel();
	public JLabel label = new JLabel();
	public JButton button = new JButton("Ok");
	
	public PopUp(int w, int h, String title, String content) {
		frame.setTitle(title);
		label.setText(content);
		frame.setSize(w, h);
		frame.add(panel);
		panel.add(label);
		panel.add(button);
		button.addMouseListener(this);
	}
	
	public PopUp() {
		this(300, 200, "Message", "");
	}
	
	public void display() {
		frame.setVisible(true);
		frame.pack();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		this.frame.dispose();
	}

	@Override public void mousePressed(MouseEvent e) { }
	@Override public void mouseReleased(MouseEvent e) { }
	@Override public void mouseEntered(MouseEvent e) { }
	@Override public void mouseExited(MouseEvent e) { }
}
