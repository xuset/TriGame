package tSquare.system;


import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import tSquare.math.Point;

public class PeripheralInput {
	public Keyboard keyboard = new Keyboard();
	public Mouse mouse = new Mouse();
	
	public class Keyboard implements KeyListener {
		private ArrayList<Integer> keyPressedList = new ArrayList<Integer>();
		
		public Keyboard() {
			keyPressedList.ensureCapacity(10);
		}
		
		public synchronized void keyPressed(KeyEvent e) {
			//System.out.println("pressed: " + e.getKeyCode());
			if (keyPressedList.contains((Integer) e.getKeyCode()) == false)
				keyPressedList.add(e.getKeyCode());
		}

		public synchronized void keyReleased(KeyEvent e) {
			//System.out.println("released: " + e.getKeyCode());
			keyPressedList.remove((Integer) e.getKeyCode());
		}

		public void keyTyped(KeyEvent e) { }
		
		public boolean isPressed(int keyEventVK) {
			return keyPressedList.contains(keyEventVK);
		}
		
		public boolean isUpPressed() {
			return keyPressedList.contains(KeyEvent.VK_W) || keyPressedList.contains(KeyEvent.VK_UP);
		}
		
		public boolean isDownPressed() {
			return keyPressedList.contains(KeyEvent.VK_S) || keyPressedList.contains(KeyEvent.VK_DOWN);
		}
		
		public boolean isLeftPressed() {
			return keyPressedList.contains(KeyEvent.VK_A) || keyPressedList.contains(KeyEvent.VK_LEFT);
		}
		
		public boolean isRightPressed() {
			return keyPressedList.contains(KeyEvent.VK_D) || keyPressedList.contains(KeyEvent.VK_RIGHT);
		}
	}
	
	public class Mouse implements MouseMotionListener, MouseListener{
		public Point location = new Point(0,0);
		public int x = 0;
		public int y = 0;
		public boolean leftPressed = false;
		public boolean rightPressed = false;
		public boolean isInside = true;
		
		public void attachComponent(Component c) {
			c.addMouseListener(this);
			c.addMouseMotionListener(this);
		}

		public void mouseClicked(MouseEvent arg0) {
			
		}

		public void mouseEntered(MouseEvent arg0) {
			isInside = true;
		}

		public void mouseExited(MouseEvent arg0) {
			isInside = false;
		}

		public void mousePressed(MouseEvent arg0) {
			if (arg0.getButton() == MouseEvent.BUTTON1)
				leftPressed = true;
			if (arg0.getButton() == MouseEvent.BUTTON3)
				rightPressed = true;
		}

		public void mouseReleased(MouseEvent arg0) {
			if (arg0.getButton() == MouseEvent.BUTTON1)
				leftPressed = false;
			if (arg0.getButton() == MouseEvent.BUTTON3)
				rightPressed = false;
		}

		public void mouseDragged(MouseEvent arg0) {
			
		}

		public void mouseMoved(MouseEvent arg0) {
			location.set(arg0.getX(), arg0.getY());
			x = arg0.getX();
			y = arg0.getY();
		} 
		
	}
}
