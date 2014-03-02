package net.xuset.triGame.game.survival.safeArea;
import net.xuset.tSquare.game.entity.Entity;
import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.IImage;
import net.xuset.tSquare.imaging.IImageFactory;
import net.xuset.tSquare.imaging.TsColor;
import net.xuset.tSquare.math.rect.IRectangleR;
import net.xuset.triGame.game.SafeBoard;


//TODO this class needs some love

public class SurvivalSafeBoard extends SafeBoard{
	private CircleContainer circleChart = new CircleContainer();
	private IImage areaImage;
	private TsColor fillColor = TsColor.black;
	private int drawPointX;
	private int drawPointY;
	private int drawPointWidth;
	private int drawPointHeight;
	private final IImageFactory imageFactory;
	
	private final int initialRadius = 12;
	public SurvivalSafeBoard(int centerX, int centerY, IImageFactory imageFactory) {
		this.imageFactory = imageFactory;
		circleChart.addCircle(centerX, centerY, initialRadius, null);
		redrawSafeArea();
	}


	private void redrawSafeArea() {
		double[] d = circleChart.getDimensions();
		drawPointX = (int) d[0]; //TODO maybe should not cast to int?
		drawPointY = (int) d[1];
		drawPointWidth = (int) d[2];
		drawPointHeight = (int) d[3];
		areaImage = imageFactory.createEmpty(drawPointWidth, drawPointHeight);
		IGraphics g = areaImage.getGraphics();
		g.setColor(fillColor);
		g.fillRect(0, 0, drawPointWidth, drawPointHeight);
		g.setAntiAlias(true);
		g.setErase(true);
		for (Circle c : circleChart.circles) {
			float diam = (float) (c.radius * 2);
			float x = (float) (c.firstX - drawPointX);
			float y = (float) (c.firstY - drawPointY);
			g.fillOval(x, y, diam, diam);
		}
		g.dispose();
	}

	public void addVisibilityForEntity(Entity e) {
		addVisibilityForEntity(e, 5);
	}
	@Override
	public void addVisibilityForEntity(Entity e, double radius) {
		circleChart.addCircle(e.getCenterX(), e.getCenterY(), radius, e);
		redrawSafeArea();
	}

	@Override
	public void removeVisibility(Entity e) {
		if (circleChart.removeByEntity(e))
			redrawSafeArea();
	}
	
	@Override
	public boolean insideSafeArea(double x, double y) {
		if (circleChart.isInsideACircle(x, y))
			return true;
		return false;
	}
	
	public boolean insideDrawSquare(int x, int y) {
		if (x >= drawPointX && x <= drawPointX + drawPointWidth) {
			if (y >= drawPointY && x <= drawPointY + drawPointHeight) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean insideSafeArea(double x, double y, double width, double height) {
		double[][] points = new double[][] { {x, y} , {x + width, y} , {x, y + height} , {x + width, y + height} };
		boolean atLeastOneTrue = false;
		for (double[] p : points) {
			if (circleChart.isInsideACircle(p[0], p[1]))
				atLeastOneTrue = true;
		}
		return atLeastOneTrue;
	}

	@Override
	public void update(int frameDelta) { }


	@Override
	public void draw(IGraphics g) {
		IRectangleR rect = g.getView();
		g.setColor(fillColor);
		float viewX = (float) rect.getX();
		float viewY = (float) rect.getY();
		float viewWidth = (float) rect.getWidth();
		float viewHeight = (float) rect.getHeight();
		
		float leftFill = drawPointX - viewX;
		if (leftFill > 0)
			g.fillRect(viewX, viewY, leftFill, viewHeight);
		
		float rightFill = (viewX + viewWidth) - (drawPointX + drawPointWidth);
		if (rightFill > 0)
			g.fillRect(drawPointX + drawPointWidth, viewY, rightFill, viewHeight);
		
		float topFill = drawPointY - viewY;
		if (topFill > 0)
			g.fillRect(viewX, viewY, viewWidth, topFill);
		
		float bottomFill = (viewY + viewHeight) - (drawPointY + drawPointHeight);
		if (bottomFill > 0)
			g.fillRect(viewX, drawPointY + drawPointHeight, viewWidth, bottomFill);
		

		g.drawImage(areaImage, drawPointX, drawPointY);
	}
	
}
