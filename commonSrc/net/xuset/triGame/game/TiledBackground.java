package net.xuset.triGame.game;

import net.xuset.tSquare.game.GameDrawable;
import net.xuset.tSquare.game.entity.Entity;
import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.IImage;
import net.xuset.tSquare.imaging.IImageFactory;
import net.xuset.tSquare.imaging.TsColor;
import net.xuset.tSquare.math.rect.IRectangleR;


public class TiledBackground implements GameDrawable{
	private static final int groupSize = 2;
	
	private IImageFactory imgFactory;
	private double drawX = 0;
	private double drawY = 0;
	private IImage tileImage;
	
	private Entity centerTo;
	
	TiledBackground(IImageFactory imgFactory) {
		setImageFactory(imgFactory);
	}
	
	public void setCenter(Entity e) {
		centerTo = e;
	}
	
	private void setImageFactory(IImageFactory imgFactory) {
		this.imgFactory = imgFactory;
		resizeTileImage();
	}
	
	private void resizeTileImage() {
		final float gutter = (float) (5 / 50.0f);
		tileImage = imgFactory.createEmpty(groupSize, groupSize);
		IGraphics g = tileImage.getGraphics();
		g.setColor(TsColor.darkGray);
		g.fillRect(0, 0, groupSize, groupSize);
		g.setColor(225, 225, 225);
		for (int x = 0; x < groupSize; x++) {
			for (int y = 0; y < groupSize; y++) {
				g.fillRect(x + gutter, y + gutter,
						1 - 2 * gutter, 1 - 2 * gutter);
			}
		}
		
		g.dispose();
	}
	
	private void positionBackground(IRectangleR rect) {
		double centerX = 0;
		double centerY = 0;
		if (centerTo != null) {
			centerX = centerTo.getCenterX();
			centerY = centerTo.getCenterY();
		}
		
		double pivotX = centerX - rect.getWidth()/2.0;
		double pivotY = centerY - rect.getHeight()/2.0;

		drawX = rect.getX() + (-1 * (pivotX - ((int) (pivotX))));
		drawY = rect.getY() + (-1 * (pivotY - ((int) (pivotY))));
	}
	
	@Override
	public void draw(IGraphics g) {
		positionBackground(g.getView());
		
		double tileWidth = tileImage.getWidth(g);
		double tileHeight = tileImage.getHeight(g);
		double widthBlocks = g.getView().getWidth() + 1;
		double heightBlocks = g.getView().getHeight() + 1;
		
		for (double x = drawX; x < drawX + widthBlocks; x += tileWidth) {
			for (double y = drawY; y < drawY + heightBlocks; y += tileHeight) {
				g.drawImage(tileImage, (float) x, (float) y);
			}
		}
	}
}
