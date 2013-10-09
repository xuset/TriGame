package triGame.game.safeArea;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import tSquare.game.GameBoard;
import tSquare.game.entity.Entity;
import tSquare.imaging.ImageProccess;
import triGame.game.safeArea.Circle;



public class SafeAreaBoard{
	private CircleContainer circleChart = new CircleContainer();
	private GameBoard gameBoard;
	private BufferedImage areaImage;
	private Color fillColor = Color.black;
	private int drawPointX;
	private int drawPointY;
	private int drawPointWidth;
	private int drawPointHeight;
	
	private final int initialRadius = 600;
	public SafeAreaBoard(GameBoard gameBoard) {
		this.gameBoard = gameBoard;
		int firstX = gameBoard.getWidth() / 2;
		int firstY = gameBoard.getHeight() / 2;
		circleChart.addCircle(firstX, firstY, initialRadius, null);
		redrawSafeArea();
	}


	private void redrawSafeArea() {
		int[] d = circleChart.getDimensions();
		drawPointX = d[0];
		drawPointY = d[1];
		drawPointWidth = d[2];
		drawPointHeight = d[3];
		areaImage = ImageProccess.createCompatiableImage(drawPointWidth, drawPointHeight);
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
		circleChart.removeByEntity(e);
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
	
	public void draw() {
		int screenX = (int) (drawPointX - gameBoard.viewable.getX());
		int screenY = (int) (drawPointY - gameBoard.viewable.getY());
		gameBoard.getGraphics().drawImage(areaImage, screenX, screenY, null);
		Graphics g = gameBoard.getGraphics();
		g.setColor(fillColor);
		int viewX = (int) gameBoard.viewable.getX();
		int viewY = (int) gameBoard.viewable.getY();
		int viewWidth = (int) gameBoard.viewable.getWidth();
		int viewHeight = (int) gameBoard.viewable.getHeight();
		
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
