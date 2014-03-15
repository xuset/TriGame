package net.xuset.tSquare.ui;

import java.util.ArrayList;

import net.xuset.tSquare.game.GameDrawable;
import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.system.input.mouse.TsMouseEvent;

public class UiPopupController implements GameDrawable{
	private final ArrayList<UiComponent> popups = new ArrayList<UiComponent>();
	
	public void addPopup(UiComponent c) {
		popups.add(c);
	}
	
	public void removePopup(int index) {
		popups.remove(index);
	}
	
	public void removePopup(UiComponent c) {
		popups.remove(c);
	}
	
	public int getPopupCount() { 
		return popups.size();
	}
	
	public void clearPopups() {
		popups.clear();
	}

	@Override
	public void draw(IGraphics g) {
		for (UiComponent c : popups) {
			c.draw(g);
		}
	}
	
	public boolean contains(UiComponent c) {
		return popups.contains(c);
	}
	
	public boolean contains(float x, float y) {
		for (UiComponent c : popups) {
			if (c.contains(x, y))
				return true;
		}
		return false;
	}
	
	public void recieveMouseEvent(TsMouseEvent e, float x, float y) {
		for (UiComponent c : popups) {
			if (c.contains(x, y)) {
				float rx = x - c.getX();
				float ry = y - c.getY();
				c.recieveMouseEvent(e, rx, ry);
			}
		}
	}

}
