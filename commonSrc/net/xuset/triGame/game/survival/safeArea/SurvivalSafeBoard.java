package net.xuset.triGame.game.survival.safeArea;
import net.xuset.tSquare.game.entity.Entity;
import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.TsColor;
import net.xuset.triGame.game.GameGrid;
import net.xuset.triGame.game.SafeBoard;


public class SurvivalSafeBoard extends SafeBoard{
	private static final int initialRadius = 12;
	
	private final CircleContainer circleChart = new CircleContainer();
	private final GameGrid gameGrid;
	
	public SurvivalSafeBoard(GameGrid gameGrid) {
		this.gameGrid = gameGrid;
		
		circleChart.addCircle(
				gameGrid.getGridWidth() / 2.0 + 0.5,
				gameGrid.getGridHeight() / 2.0 + 0.5, 
				initialRadius, null);
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
		x = Math.floor(x) + 0.5;
		y = Math.floor(y) + 0.5;
		return circleChart.isInsideACircle(x, y);
	}
	
	@Override
	public boolean insideSafeArea(double x, double y, double width, double height) {
		return insideSafeArea(x + width/2, y + height/2);
	}

	@Override
	public void update(int frameDelta) { }

	@Override
	public void draw(IGraphics g) {
		int initX = (int) Math.floor(g.getView().getX());
		int initY = (int) Math.floor(g.getView().getY());
		int width = (int) Math.ceil(g.getView().getWidth());
		int height = (int) Math.ceil(g.getView().getHeight());
		
		g.setColor(TsColor.black);
		for (int x = initX; x <= width + initX; x++) {
			for (int y = initY; y <= height + initY; y++) {
				double cx = x + 1/2.0, cy = y + 1/2.0;
				if (!circleChart.isInsideACircle(cx, cy) || !gameGrid.isInside(cx, cy))
					g.fillRect(x, y, 1.01f, 1.01f);
			}
		}
	}
	
}
