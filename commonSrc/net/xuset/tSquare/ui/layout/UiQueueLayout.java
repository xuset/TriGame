package net.xuset.tSquare.ui.layout;

import net.xuset.tSquare.ui.Axis;
import net.xuset.tSquare.ui.UiComponent;

public class UiQueueLayout extends UiLayoutList {
	private final UiComponent parent;
	private float hGap, vGap;
	
	public UiQueueLayout(float hGap, float vGap, UiComponent parent) {
		this.hGap = hGap;
		this.vGap = vGap;
		this.parent = parent;
	}
	
	public UiQueueLayout(UiComponent parent) {
		this(2.0f, 2.0f, parent);
	}
	
	@Override
	public void organize() {
		final float width = getWidth();
		final float height = getHeight();
		float currentX = getTopLeftX(width) + hGap;
		float currentY = getTopLeftY(height) + vGap;

		for (int i = 0; i < getComponentCount(); i++) {
			UiComponent c = getComponent(i);
			float offset = getOppositeOffset(width, height, c);
			
			switch(getOrientation()) {
			case X_AXIS:
				c.setLocation(currentX, currentY + offset);
				currentX += c.getWidth() + hGap;
				break;
			case Y_AXIS:
				c.setLocation(currentX + offset, currentY);
				currentY += c.getHeight() + vGap;
				break;
			}
		}
	}
	
	private float getOppositeOffset(float w, float h, UiComponent c) {
		float off = getOrientation() == Axis.X_AXIS ? h : w;
		float compOff = getOrientation() == Axis.X_AXIS ? c.getHeight() : c.getWidth();
		
		Axis oppAxis = getOppositeOrientation();
		switch(getAlignment(oppAxis)) {
		case FRONT:
			return 0.0f;
		case CENTER:
			return (off - 2 * getGap(oppAxis)) / 2 - compOff / 2;
		case BACK:
			return (off - 2 * getGap(oppAxis)) - compOff;
		}
		
		return 0.0f;
	}
	
	private float getTopLeftX(float w) {
		switch(getAlignment(Axis.X_AXIS)) {
		case FRONT:
			return 0.0f;
		case CENTER:
			return parent.getWidth() / 2 - w / 2;
		case BACK:
			return parent.getWidth() - w; 
		}
		return 0.0f;
	}
	
	private float getTopLeftY(float h) {
		switch(getAlignment(Axis.Y_AXIS)) {
		case FRONT:
			return 0.0f;
		case CENTER:
			return parent.getHeight() / 2 - h / 2;
		case BACK:
			return parent.getHeight() - h; 
		}
		return 0.0f;
	}
	
	private float getGapLength(Axis a) {
		switch(a) {
		case X_AXIS:
			return hGap;
		case Y_AXIS:
			return vGap;
		}
		return hGap;
	}
	
	private float getGap(Axis axis) {
		if (axis == Axis.X_AXIS)
			return hGap;
		else
			return vGap;
	}

	@Override
	public float getWidth() {
		return getLength(Axis.X_AXIS);
	}

	@Override
	public float getHeight() {
		return getLength(Axis.Y_AXIS);
	}
	
	private float getLength(Axis axis) {
		if (getOrientation() == axis) {
			float usedLength = DimensionFinder.sum(this, getOrientation());
			usedLength += (1 + getComponentCount()) * getGapLength(getOrientation());
			return usedLength;
		} else {
			float oppositeMax = DimensionFinder.max(this, getOppositeOrientation());
			oppositeMax += 2 * getGapLength(getOppositeOrientation());
			return oppositeMax;
		}
	}
}
