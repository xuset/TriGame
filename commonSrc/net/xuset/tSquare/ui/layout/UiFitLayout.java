package net.xuset.tSquare.ui.layout;

import net.xuset.tSquare.ui.UiComponent;

public class UiFitLayout extends UiLayoutList{
	private final UiComponent parent;
	
	public UiFitLayout(UiComponent parent) {
		super();
		this.parent = parent;
		throw new RuntimeException("UiFitLayout is under construction!");
	}
	
	@Override
	public void organize() {
		float usedLength = getUsedLength();
		int spacerCount = getComponentCount() + 2;
		float spacerLength = (getComponentLength(parent) - usedLength) / spacerCount;
		
		float currentOffset = spacerLength;
		for (int i = 0; i < getComponentCount(); i++) {
			UiComponent c = getComponent(i);
			setNewOffset(c, currentOffset);
			currentOffset += getComponentLength(c) + spacerLength;
		}
	}
	
	private void setNewOffset(UiComponent c, float offset) {
		switch(getOrientation()) {
		case X_AXIS:
			c.setLocation(offset, c.getY());
			break;
		case Y_AXIS:
			c.setLocation(c.getX(), offset);
			break;
		}
	}
	
	private float getComponentLength(UiComponent c) {
		switch(getOrientation()) {
		case X_AXIS:
			return c.getWidth();
		case Y_AXIS:
			return c.getHeight();
		}
		return 0;
	}
	
	private float getUsedLength() {
		float length = 0.0f;

		for (int i = 0; i < getComponentCount(); i++) {
			UiComponent c = getComponent(i);
			length += getComponentLength(c);
		}
		
		return length;
	}

	@Override
	public float getWidth() {
		return 0;
	}

	@Override
	public float getHeight() {
		return 0;
	}
}
