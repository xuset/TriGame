package net.xuset.tSquare.ui;

import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.TsColor;

public class UiLineBorder implements UiBorder {
	private float thickness;
	private TsColor color;
	boolean visible;
	
	public UiLineBorder(float thickness, TsColor color, boolean visible) {
		this.thickness = thickness;
		this.color = color;
		this.visible = visible;
	}
	
	public UiLineBorder(boolean visible) {
		this(1.0f, TsColor.black, visible);
	}
	
	public UiLineBorder() {
		this(false);
	}

	@Override
	public float getThickness() {
		return thickness;
	}

	@Override
	public void setThickness(float thickness) {
		this.thickness = thickness;
	}

	@Override
	public void setColor(TsColor color) {
		this.color = color;
	}

	@Override
	public TsColor getColor() {
		return color;
	}

	@Override
	public void setVisibility(boolean visible) {
		this.visible = visible;
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	@Override
	public void draw(IGraphics g, float x, float y, float w, float h) {
		//TODO somehow implement drawing rectangles of different thicknesses. maybe..
		g.setColor(color);
		g.drawRect(x, y, w, h);
	}

}
