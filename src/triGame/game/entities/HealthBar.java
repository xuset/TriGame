package triGame.game.entities;


import java.awt.Color;
import java.awt.Graphics;

import tSquare.game.GameBoard;
import tSquare.game.entity.Entity;

public class HealthBar {
	private static final int barWidth = 30;
	private static final int barHeight = 5;
	
	private GameBoard gameBoard;
	private Entity entity;
	
	public int width = barWidth;
	public int height = barHeight;
	public int relativeX;
	public int relativeY = -15;
	public int maxHealth = 100;
	public Color fillColor = Color.green;
	
	public HealthBar(GameBoard gameBoard, Entity entity) { //let constructor take care of which color to draw the healthbar
		this.gameBoard = gameBoard;
		this.entity = entity;
		relativeX = entity.getWidth() / 2 - barWidth / 2;
	}
	
	public void recenter() {
		relativeX = width / 2;
	}
	
	public void drawHealthBar() {
		drawHealthBar((int) (entity.getX() - gameBoard.viewable.getX() + relativeX), (int) (entity.getY() - gameBoard.viewable.getY() + relativeY), (int) entity.getHealth());
	}
	
	public void drawHealthBar(int x, int y, int health) {
		Graphics g = gameBoard.getGraphics();
		g.setColor(Color.black);
		g.fillRect(x, y, width, height);
		if (entity.getHealth() > 0) {
			g.setColor(determineColor(health));
			g.fillRect(x, y,(int) (width * entity.getHealth() / maxHealth), height);
		}
	}
	
	private Color determineColor(double health) {
		double mult = health / maxHealth;
		int cb = (int) Math.min(255, fillColor.getBlue() * mult);
		int cg = (int) Math.min(255, fillColor.getGreen() * mult);
		//int cr = (int) Math.min(255, fillColor.getRed() * mult);
		int blue = cb;
		int green = cg;
		//int red = (int) Math.max(cr, Math.min(255, 255 - 255 * ((health - maxHealth / 2) / maxHealth)));
		int red = (int) Math.max(fillColor.getRed(), 255 - ((int) Math.min(255, 255.0 / maxHealth * entity.getHealth())));
		Color c = new Color(red, green, blue);
		return c;
	}
}
