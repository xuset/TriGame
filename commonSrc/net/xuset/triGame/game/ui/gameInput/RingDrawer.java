package net.xuset.triGame.game.ui.gameInput;

import net.xuset.tSquare.game.GameDrawable;
import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.TsColor;
import net.xuset.tSquare.math.point.IPointR;
import net.xuset.tSquare.math.point.Point;

public class RingDrawer implements GameDrawable{
	private final int ringCount;
	private final TsColor ringColor, crossColor;
	
	private IPointR center = new Point(0, 0);
	private float radius = 0.0f;
	
	public RingDrawer(int ringCount, TsColor ringColor) {
		this(ringCount, ringColor, null);
	}
	
	public RingDrawer(int ringCount, TsColor ringColor, TsColor crossColor) {
		
		this.ringCount = ringCount;
		this.ringColor = ringColor;
		this.crossColor = crossColor;
	}
	
	public void setCenterAndRadius(IPointR newCenter, float newRadius) {
		center = newCenter;
		radius = newRadius;
	}

	@Override
	public void draw(IGraphics g) {
		drawRings(g);
		if (crossColor != null)
			drawCross(g);
	}
	
	private void drawRings(IGraphics g) {
		g.setColor(ringColor);
		
		for (int i = 0; i < ringCount; i++) {
			float r = (radius / ringCount) * (i + 1);
			float x = (float) (center.getX() - r);
			float y = (float) (center.getY() - r);
			
			g.drawOval(x, y, r * 2, r * 2);
		}
	}
	
	private void drawCross(IGraphics g) {
		float centerX = (float) center.getX();
		float centerY = (float) center.getY();
		g.setColor(crossColor);
		g.drawLine(
				centerX, centerY - radius,
				centerX, centerY + radius);
		g.drawLine(
				centerX - radius, centerY,
				centerX + radius, centerY);
	}
}
