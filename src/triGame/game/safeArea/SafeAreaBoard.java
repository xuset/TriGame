package triGame.game.safeArea;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import tSquare.game.GameBoard.ViewRect;
import tSquare.game.GameIntegratable;
import tSquare.game.entity.Entity;
import tSquare.imaging.ImageProcess;
import triGame.game.Params;
import triGame.game.safeArea.Circle;



public class SafeAreaBoard implements GameIntegratable{
	private CircleContainer circleChart = new CircleContainer();
	private BufferedImage areaImage;
	private Color fillColor = Color.black;
	private int drawPointX;
	private int drawPointY;
	private int drawPointWidth;
	private int drawPointHeight;
	
	private final int initialRadius = 600;
	public SafeAreaBoard() {
		int firstX = Params.GAME_WIDTH / 2;
		int firstY = Params.GAME_HEIGHT / 2;
		circleChart.addCircle(firstX, firstY, initialRadius, null);
		redrawSafeArea();
	}


	private void redrawSafeArea() {
		int[] d = circleChart.getDimensions();
		drawPointX = d[0];
		drawPointY = d[1];
		drawPointWidth = d[2];
		drawPointHeight = d[3];
		areaImage = ImageProcess.createCompatiableImage(drawPointWidth, drawPointHeight);
		Graphics2D g = (Graphics2D) areaImage.getGraphics();
		g.setColor(fillColor);
		g.fillRect(0, 0, drawPointWidth, drawPointHeight);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setComposite(AlphaComposite.Clear);
		for (Circle c : circleChart.circles) {
			g.fillOval(c.firstX - drawPointX, c.firstY - drawPointY, 2 * c.radius, 2 * c.radius);
		}
		g.setComposite(AlphaComposite.SrcOver);
		g.dispose();
	}
	
	public void addVisibilityForEntity(Entity e) {
		addVisibilityForEntity(e, 250);
	}
	public void addVisibilityForEntity(Entity e, int radius) {
		circleChart.addCircle((int) e.getCenterX(), (int) e.getCenterY(), radius, e);
		redrawSafeArea();
	}
	
	public void removeVisibility(Entity e) {
		if (circleChart.removeByEntity(e))
			redrawSafeArea();
	}
	
	public boolean insideSafeArea(int x, int y) {
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
	
	public boolean insideSafeArea(int x, int y, int width, int height) {
		int[][] points = new int[][] { {x, y} , {x + width, y} , {x, y + height} , {x + width, y + height} };
		for (int[] p : points) {
			if (circleChart.isInsideACircle(p[0], p[1]) == false)
				return false;
		}
		return true;
	}

	@Override
	public void performLogic(int frameDelta) { }


	@Override
	public void draw(Graphics2D g, ViewRect rect) {

		int screenX = (int) (drawPointX - rect.getX());
		int screenY = (int) (drawPointY - rect.getY());
		g.drawImage(areaImage, screenX, screenY, null);
		g.setColor(fillColor);
		int viewX = (int) rect.getX();
		int viewY = (int) rect.getY();
		int viewWidth = (int) rect.getWidth();
		int viewHeight = (int) rect.getHeight();
		
		int leftFill = drawPointX - viewX;
		if (leftFill > 0)
			g.fillRect(0, 0, leftFill, viewHeight);
		
		int rightFill = (viewX + viewWidth) - (drawPointX + drawPointWidth);
		if (rightFill > 0)
			g.fillRect(drawPointX + drawPointWidth - viewX - 1, 0, rightFill, viewHeight);
		
		int topFill = drawPointY - viewY;
		if (topFill > 0)
			g.fillRect(0, 0, viewWidth, topFill);
		
		int bottomFill = (viewY + viewHeight) - (drawPointY + drawPointHeight);
		if (bottomFill > 0)
			g.fillRect(0, drawPointY + drawPointHeight - viewY, viewWidth, bottomFill);
	}
	
}
