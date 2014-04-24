package net.xuset.triGame.game.survival.safeArea;
import net.xuset.tSquare.game.entity.Entity;
import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.TsColor;
import net.xuset.triGame.game.SafeBoard;


public class SurvivalSafeBoard extends SafeBoard{
	private static final int initialRadius = 12;
	
	private final CircleContainer circleChart = new CircleContainer();
	
	public SurvivalSafeBoard(double centerX, double centerY) {
		circleChart.addCircle(centerX, centerY, initialRadius, null);
	}
	
	@Override
	public void addVisibilityForEntity(Entity e, double radius) {
		circleChart.addCircle(e.getCenterX(), e.getCenterY(), radius, e);
	}

	@Override
	public void removeVisibility(Entity e) {
		circleChart.removeByEntity(e);
	}
	
	@Override
	public boolean insideSafeArea(double x, double y) {
		return circleChart.isInsideACircle(x, y);
	}
	
	@Override
	public boolean insideSafeArea(double x, double y, double width, double height) {
		return circleChart.isInsideACircle(x + width/2, y + height/2);
	}

	@Override
	public void update(int frameDelta) { }

	@Override
	public void draw(IGraphics g) {
		int initX = (int) g.getView().getX();
		int initY = (int) g.getView().getY();
		int width = (int) Math.ceil(g.getView().getWidth());
		int height = (int) Math.ceil(g.getView().getHeight());
		
		g.setColor(TsColor.black);
		for (int x = initX; x <= width + initX; x++) {
			int heightFill = 1;
			for (int y = initY; y <= height + initY; y++) {
				if (!circleChart.isInsideACircle(x + 1/2.0, y + 1/2.0))
					g.fillRect(x, y, 1, heightFill);
				else
					heightFill = 1;
			}
		}
	}
	
}
