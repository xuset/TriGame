package triGame.game.entities;


import java.awt.Color;
import java.awt.Graphics2D;

import tSquare.game.GameBoard.ViewRect;
import tSquare.game.GameIntegratable;
import tSquare.game.entity.Entity;

public class HealthBar implements GameIntegratable{
	private static final int barWidth = 30;
	private static final int barHeight = 5;
	
	private final Entity entity;
	
	public int width = barWidth;
	public int height = barHeight;
	public int relativeX;
	public int relativeY = -15;
	public int maxHealth = 100;
	public Color fillColor = Color.green;
	
	public HealthBar(Entity entity) {
		this.entity = entity;
		relativeX = entity.getWidth() / 2 - barWidth / 2;
	}
	
	public void recenter() {
		relativeX = entity.getWidth() / 2 - barWidth / 2;
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

	@Override
	public void draw(Graphics2D g, ViewRect rect) {
		int x = (int) (entity.getX() - rect.getX() + relativeX);
		int y = (int) (entity.getY() - rect.getY() + relativeY);
		int health = (int) entity.getHealth();
		
		g.setColor(Color.black);
		g.fillRect(x, y, width, height);
		if (health > 0) {
			g.setColor(determineColor(health));
			g.fillRect(x, y,(int) (width * entity.getHealth() / maxHealth), height);
		}
	}

	@Override
	public void performLogic(int frameDelta) { }
}
