package net.xuset.triGame.game.survival.safeArea;
import net.xuset.tSquare.game.entity.Entity;
import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.IImage;
import net.xuset.tSquare.imaging.IImageFactory;
import net.xuset.tSquare.imaging.TsColor;
import net.xuset.tSquare.math.rect.IRectangleR;
import net.xuset.triGame.game.SafeBoard;


public class SurvivalSafeBoard extends SafeBoard{
	private static final int initialRadius = 12;
	
	private final CircleContainer circleChart = new CircleContainer();
	private final IImageFactory imageFactory;
	
	private IImage areaImage;
	private IGraphics areaGraphics;
	private double lastWidth = 0.0, lastHeight = 0.0;
	private double lastX = 0.0, lastY = 0.0;
	private boolean needsRedraw = true;
	
	
	public SurvivalSafeBoard(double centerX, double centerY, IImageFactory imageFactory) {
		this.imageFactory = imageFactory;
		circleChart.addCircle(centerX, centerY, initialRadius, null);
	}
	
	private void checkIfRecreateImageIsNeeded(IRectangleR view) {
		if (view.getWidth() != lastWidth || view.getHeight() != lastHeight) {
			lastWidth = view.getWidth();
			lastHeight = view.getHeight();
			
			int newW = (lastWidth - (int) lastWidth == 0.0) ? 
					(int) lastWidth : 1 + (int) lastWidth;
			int newH = (lastHeight - (int) lastHeight == 0.0) ? 
					(int) lastHeight : 1 + (int) lastHeight;
			areaImage = imageFactory.createEmpty(newW, newH);
			if (areaGraphics != null)
				areaGraphics.dispose();
			needsRedraw = true;
			areaGraphics = areaImage.getGraphics();
			areaGraphics.setColor(TsColor.black);
		}
	}


	private void redrawSafeArea(IRectangleR view) {
		if (needsRedraw || view.getX() != lastX || view.getY() != lastY) {
			needsRedraw = false;
			lastX = view.getX();
			lastY = view.getY();
			
			IGraphics g = areaGraphics;
			g.setErase(false);
			g.fillRect(0, 0, (float) view.getWidth(), (float) view.getHeight());
			g.setErase(true);
			for (Circle c : circleChart.circles) {
				float diam = (float) (c.radius * 2);
				float x = (float) (c.firstX - view.getX());
				float y = (float) (c.firstY - view.getY());
				g.fillOval(x, y, diam, diam);
			}
		}
	}
	
	@Override
	public void addVisibilityForEntity(Entity e, double radius) {
		needsRedraw = true;
		circleChart.addCircle(e.getCenterX(), e.getCenterY(), radius, e);
	}

	@Override
	public void removeVisibility(Entity e) {
		needsRedraw = true;
		circleChart.removeByEntity(e);
	}
	
	@Override
	public boolean insideSafeArea(double x, double y) {
		return circleChart.isInsideACircle(x, y);
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
		IRectangleR view = g.getView();
		checkIfRecreateImageIsNeeded(view);
		redrawSafeArea(view);
		g.drawImage(areaImage, (float) view.getX(), (float) view.getY());
	}
	
}
