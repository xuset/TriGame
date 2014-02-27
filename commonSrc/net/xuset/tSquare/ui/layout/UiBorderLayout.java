package net.xuset.tSquare.ui.layout;

import net.xuset.tSquare.math.point.IPointR;
import net.xuset.tSquare.ui.Alignment;
import net.xuset.tSquare.ui.Axis;
import net.xuset.tSquare.ui.UiComponent;
import net.xuset.tSquare.util.Observer;
import net.xuset.tSquare.util.Observer.Change;

public class UiBorderLayout implements UiLayout {
	
	public static enum BorderPosition { NORTH, EAST, SOUTH, WEST, CENTER }
	private static final BorderPosition[] positions = BorderPosition.values();
	
	private final UiComponent parent;
	private final Observer<IPointR> sizeWatcher = new Observer<IPointR>();
	private final UiComponent[] uiStore = new UiComponent[positions.length];
	
	private Axis axis = Axis.X_AXIS;
	private Alignment alignmentX = Alignment.FRONT;
	private Alignment alignmentY = Alignment.FRONT;
	private float width = 0.0f, height = 0.0f;
	
	public UiBorderLayout(UiComponent parent) {
		this.parent = parent;
	}

	@Override
	public void watchDimensions(Change<IPointR> oc) {
		sizeWatcher.watch(oc);
	}

	@Override
	public void unwatchDimensions(Change<IPointR> oc) {
		sizeWatcher.unwatch(oc);
	}

	@Override
	public void add(UiComponent c) {
		add(c, BorderPosition.CENTER);
	}
	
	public void add(UiComponent c, BorderPosition pos) {
		uiStore[pos.ordinal()] = c;
	}

	@Override
	public void remove(UiComponent component) {
		for (int i = 0; i < uiStore.length; i++) {
			if (component.equals(uiStore[i]))
				uiStore[i] = null;
		}
	}

	@Override
	public int getComponentCount() {
		int count = 0;
		for (int i = 0; i < uiStore.length; i++) {
			if (uiStore[i] != null)
				count++;
		}
		return count;
	}

	@Override
	public UiComponent getComponent(int wanted) {
		
		for (int i = 0; i < uiStore.length; i++) {
			if (uiStore[i] != null)
				wanted--;
			if (wanted == -1)
				return uiStore[i];
		}
		
		throw new ArrayIndexOutOfBoundsException();
	}

	@Override
	public void clearComponents() {
		for (int i = 0; i < uiStore.length; i++)
			uiStore[i] = null;
	}

	@Override
	public void setOrientation(Axis a) {
		axis = a;
	}

	@Override
	public Axis getOrientation() {
		return axis;
	}

	@Override
	public void setAlignment(Axis axis, Alignment a) {
		if (axis == Axis.X_AXIS)
			alignmentX = a;
		else
			alignmentY = a;
	}

	@Override
	public Alignment getAlignment(Axis axis) {
		if (axis == Axis.X_AXIS)
			return alignmentX;
		else
			return alignmentY ;
	}

	@Override
	public float getWidth() {
		return width;
	}

	@Override
	public float getHeight() {
		return height;
	}

	@Override
	public void organize() {
		final float pWidth = parent.getWidth();
		final float pHeight = parent.getHeight();
		
		for (int i = 0; i < uiStore.length; i++) {
			UiComponent c = uiStore[i];
			if (c == null)
				continue;
			
			if (i == BorderPosition.WEST.ordinal()) {
				c.setLocation(0, pHeight/2 - c.getHeight()/2);
			} else if (i == BorderPosition.EAST.ordinal()) {
				c.setLocation(pWidth - c.getWidth(), pHeight/2 - c.getHeight()/2);
			} else if (i == BorderPosition.NORTH.ordinal()) {
				c.setLocation(pWidth/2 - c.getWidth()/2, 0);
			} else if (i == BorderPosition.SOUTH.ordinal()) {
				c.setLocation(pWidth/2 - c.getWidth()/2, pHeight - c.getHeight());
			} else if (i == BorderPosition.CENTER.ordinal()) {
				c.setLocation(pWidth/2 - c.getWidth()/2, pHeight/2 - c.getHeight()/2);
			}
		}
	}

}
