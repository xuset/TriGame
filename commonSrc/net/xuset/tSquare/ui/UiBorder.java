package net.xuset.tSquare.ui;

import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.TsColor;

public interface UiBorder{
	float getThickness();
	void setThickness(float thickness);
	
	void setColor(TsColor c);
	TsColor getColor();
	
	void draw(IGraphics g, float x, float y, float w, float h);
	
	void setVisibility(boolean visible);
	boolean isVisible();
}
