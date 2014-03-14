package net.xuset.triGame.intro;

import java.util.ArrayList;

import net.xuset.tSquare.game.GameDrawable;
import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.IImage;
import net.xuset.tSquare.imaging.TsColor;
import net.xuset.tSquare.math.point.Point;
import net.xuset.tSquare.math.rect.IRectangleR;
import net.xuset.triGame.game.Load;

public class IntroAnimator implements GameDrawable{
	private final int maxZombies = 10;
	private final double offscreenOffset = 200.0;
	private final long timeDuration = 6000L;
	private final TriangleAnimator person;
	private final ArrayList<TriangleAnimator> zombies = new ArrayList<TriangleAnimator>();
	private final IImage zombieImage;
	
	private boolean goingLeft = true;
	private long timeToEnd = 0L;
	
	public IntroAnimator() {
		zombieImage = Load.triangleImage(TsColor.red, 80);
		IImage playerImage = Load.triangleImage(TsColor.cyan, 80);
		person = new TriangleAnimator(playerImage);
	}

	@Override
	public void draw(IGraphics g) {
		long currentTime = System.currentTimeMillis();
		if (currentTime > timeToEnd)
			resetAnimation(g.getView());
		
		double progress = (timeDuration - timeToEnd + currentTime + 0.0) / timeDuration;
		
		for (TriangleAnimator a : zombies)
			a.draw(g, progress);
		
		person.draw(g, progress + 0.15);
	}
	
	private void resetAnimation(IRectangleR view) {
		if (zombies.size() < maxZombies)
			zombies.add(new TriangleAnimator(zombieImage));
		
		timeToEnd = System.currentTimeMillis() + timeDuration;
		goingLeft = !goingLeft;
		double goalY = Math.random() * (view.getHeight() - 200) + 100;
		double goalX = goingLeft ? -offscreenOffset : view.getWidth() + offscreenOffset;
		person.setGoal(goalX, goalY);
		
		final double goalVarianceY = 300;
		final double goalVarianceX = 100;
		for (TriangleAnimator a : zombies) {
			double varY = Math.random() * goalVarianceY - goalVarianceY / 2;
			double varX = Math.random() * goalVarianceX - goalVarianceX / 2;
			a.setGoal(goalX, goalY + varY, person.startX + varX, person.startY + varY);
		}
	}
	
	private class TriangleAnimator {
		private final IImage image;
		private double goalX, goalY, startX, startY;
		
		public TriangleAnimator(IImage image) {
			this.image = image;
		}
		
		public void draw(IGraphics g, double progress) {
			float currentX = (float) ((goalX - startX) * progress + startX);
			float currentY = (float) ((goalY - startY) * progress + startY);
			double angle = Point.angle(startX, startY, goalX, goalY);
			
			g.drawImageRotate(image, currentX, currentY, angle);
		}
		
		public void setGoal(double x, double y) {
			startX = goalX;
			startY = goalY;
			goalX = x;
			goalY = y;
		}
		
		public void setGoal(double x, double y, double startX, double startY) {
			goalX = x;
			goalY = y;
			this.startX = startX;
			this.startY = startY;
		}
	}
	
}
