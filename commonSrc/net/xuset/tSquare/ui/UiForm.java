package net.xuset.tSquare.ui;

import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.WindowGraphics;
import net.xuset.tSquare.math.point.IPointR;
import net.xuset.tSquare.math.rect.IRectangleR;
import net.xuset.tSquare.math.rect.Rectangle;
import net.xuset.tSquare.system.input.mouse.TsMouseEvent;
import net.xuset.tSquare.ui.layout.UiLayout;
import net.xuset.tSquare.ui.layout.UiQueueLayout;
import net.xuset.tSquare.util.Observer.Change;

public class UiForm extends UiComponent implements UiAddable {
	private UiLayout layout = null;
	private Change<IPointR> sizeWatcher = null;

	public UiForm() {
		super(0, 0, 0, 0);
		setLayout(new UiQueueLayout(this));
	}
	
	public UiForm(UiLayout layout) {
		super(0, 0, 0, 0);
		setLayout(layout);
	}
	
	public void setLayout(UiLayout l) {
		if (layout != null && sizeWatcher != null) {
			layout.unwatchDimensions(sizeWatcher);
		}
		
		layout = l;
	}
	
	protected void watchLayoutSize(Change<IPointR> watcher) {
		if (layout != null && sizeWatcher != null)
			layout.unwatchDimensions(sizeWatcher);
		
		sizeWatcher = watcher;
		layout.watchDimensions(watcher);
	}
	
	@Override
	public UiLayout getLayout() { return layout; }
	
	@Override
	public void draw(IGraphics g) {
		if (!isVisible())
			return;

		getLayout().organize();
		super.draw(g);
		
		IRectangleR subView = new Rectangle(getX(), getY(), getWidth(), getHeight());
		WindowGraphics window = new WindowGraphics(subView, g);
		for (int i = 0; i < layout.getComponentCount(); i++)
			layout.getComponent(i).draw(window);
		
		//g.setColor(net.xuset.tSquare.imaging.TsColor.black);
		//g.drawRect(getX(), getY(), getWidth(), getHeight());
	}
	
	@Override
	protected void recieveMouseEvent(TsMouseEvent e, float x, float y) {
		super.recieveMouseEvent(e, x, y);
		
		for (int i = 0; i < layout.getComponentCount(); i++) {
			UiComponent c = layout.getComponent(i);
			
			if (c.contains(x, y)) {
				float rx = x - c.getX();
				float ry = y - c.getY();
				c.recieveMouseEvent(e, rx, ry);
			}
		}
	}

	@Override
	public float getWidth() {
		return layout.getWidth();
	}
	
	@Override
	public float getHeight() {
		return layout.getHeight();
	}

}
