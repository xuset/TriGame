package net.xuset.tSquare.imaging;

import net.xuset.tSquare.math.rect.IRectangleR;

public interface IGraphics {
	void drawImage(IImage image, float dx, float dy, float dw, float dh, float sx, float sy, float sw, float sh);
	void drawImage(IImage image, float x, float y);
	void drawImageRotate(IImage image, float x, float y, double radians);
	
	void drawRect(float x, float y, float w, float h);
	void drawRoundedRect(float x, float y, float w, float h, int rx, int ry);
	void drawOval(float x, float y, float w, float h);
	void drawLine(float x1, float y1, float x2, float y2);
	
	void fillRect(float x, float y, float w, float h);
	void fillRoundedRect(float x, float y, float w, float h, int rx, int ry);
	void fillOval(float x, float y, float w, float h);
	void fillTriangle(float x, float y, float w, float h);
	
	void setFont(IFont font);
	void drawText(float x, float y, String text);
	float getTextWidth(String text);
	float getTextHeight();
	
	void setColor(int color);
	void setColor(TsColor color);
	void setColor(int r, int g, int b);
	
	void setAntiAlias(boolean antiAlias);
	boolean isAntiAliasOn();
	
	float getWidthUnits(IImage image);
	float getHeightUnits(IImage image);
	
	void clear();
	void dispose();
	IRectangleR getView();
}
