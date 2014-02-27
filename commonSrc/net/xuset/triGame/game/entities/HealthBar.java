package net.xuset.triGame.game.entities;

import net.xuset.tSquare.game.GameIntegratable;
import net.xuset.tSquare.game.entity.Entity;
import net.xuset.tSquare.game.particles.Particle;
import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.TsColor;

public class HealthBar extends Particle implements GameIntegratable {
	private static final double barWidth = 3 / 5.0;
	private static final double barHeight = 1 / 10.0;
	
	private final Entity entity;
	
	public double width = barWidth;
	public double height = barHeight;
	public double relativeX;
	public double relativeY = -15 / 50.0;
	public int maxHealth = 100;
	public TsColor fillColor = TsColor.green;
	
	public HealthBar(Entity entity) {
		this.entity = entity;
		relativeX = entity.getWidth() / 2 - barWidth / 2;
	}
	
	public void recenter() {
		relativeX = entity.getWidth() / 2 - barWidth / 2;
	}
	
	private TsColor determineColor(double health) {
		double mult = health / maxHealth;
		int cb = (int) Math.min(255, fillColor.getBlue() * mult);
		int cg = (int) Math.min(255, fillColor.getGreen() * mult);
		//int cr = (int) Math.min(255, fillColor.getRed() * mult);
		int blue = cb;
		int green = cg;
		//int red = (int) Math.max(cr, Math.min(255, 255 - 255 * ((health - maxHealth / 2) / maxHealth)));
		int red = (int) Math.max(fillColor.getRed(), 255 - ((int) Math.min(255, 255.0 / maxHealth * entity.getHealth())));
		TsColor c = new TsColor(red, green, blue);
		return c;
	}

	@Override
	public void draw(IGraphics g) {
		double x = (entity.getX() + relativeX);
		double y = (entity.getY() + relativeY);
		int health = (int) entity.getHealth();
		
		g.setColor(TsColor.black);
		g.fillRect((float) x, (float) y, (float) width, (float) height);
		if (health > 0) {
			g.setColor(determineColor(health));
			g.fillRect((float) x, (float) y, (float) (width * entity.getHealth() / maxHealth), (float) height);
		}
	}

	@Override
	public void update(int frameDelta) { }

	@Override
	public void draw(int delta, IGraphics g) {
		draw(g);
		
	}

	@Override
	public boolean isExpired() {
		return entity == null || entity.removeRequested();
	}
}
