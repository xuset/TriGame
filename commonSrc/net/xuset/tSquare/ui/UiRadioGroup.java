package net.xuset.tSquare.ui;


import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.WindowGraphics;
import net.xuset.tSquare.math.rect.IRectangleR;
import net.xuset.tSquare.math.rect.Rectangle;
import net.xuset.tSquare.system.input.mouse.MouseAction;
import net.xuset.tSquare.system.input.mouse.TsMouseEvent;
import net.xuset.tSquare.ui.layout.UiQueueLayout;

public class UiRadioGroup extends UiComponent{
	private final UiQueueLayout layout;
	private UiRadioButton selected = null;

	public UiRadioGroup() {
		super(0, 0, 0, 0);
		layout = new UiQueueLayout(this);
		layout.setOrientation(Axis.Y_AXIS);
		getBorder().setVisibility(true);
	}
	
	public void removeButton(UiRadioButton btn) {
		if (btn == selected)
			setSelectedButton(null);
		layout.remove(btn);
	}
	
	public int getButtonCount() {
		return layout.getComponentCount();
	}
	
	public void addButton(String text) {
		addButton(new UiRadioButton(text));
	}
	
	public void addButton(UiRadioButton btn) {
		layout.add(btn);
		if (selected == null)
			setSelectedButton(btn);
	}
	
	public UiRadioButton getSelectedButton() {
		return selected;
	}
	
	public void setSelectedButton(int index) {
		setSelectedButton((UiRadioButton) layout.getComponent(index));
	}
	
	public void setSelectedButton(UiRadioButton newSelected) {
		selected = newSelected;
		for (int i = 0; i < layout.getComponentCount(); i++) {
			UiRadioButton c = (UiRadioButton) layout.getComponent(i);
			c.setSelected(newSelected == c);
		}
	}
	
	@Override
	public void draw(IGraphics g) {
		if (!isVisible())
			return;

		setSize(layout.getWidth(), layout.getHeight());
		layout.organize();
		super.draw(g);
		
		IRectangleR view = new Rectangle(getX(), getY(), getWidth(), getHeight());
		WindowGraphics windowG = new WindowGraphics(view, g);
		for (int i = 0; i < layout.getComponentCount(); i++) {
			layout.getComponent(i).draw(windowG);
		}
	}

	@Override
	protected void recieveMouseEvent(TsMouseEvent e, float x, float y) {
		super.recieveMouseEvent(e, x, y);
		
		if (e.action == MouseAction.RELEASE) {
			UiRadioButton newSelected = getButtonByLocation(x, y);
			if (newSelected != null)
				setSelectedButton(newSelected);
		}
	}
	
	private UiRadioButton getButtonByLocation(float x, float y) {
		for (int i = 0; i < layout.getComponentCount(); i++) {
			UiRadioButton c  = (UiRadioButton) layout.getComponent(i);
			if (c.contains(x, y))
				return c;
		}
		return null;
	}

}
