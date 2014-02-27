package net.xuset.triGame.game;

import net.xuset.tSquare.game.entity.Entity;
import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.IImage;
import net.xuset.tSquare.imaging.ImageFactory;
import net.xuset.tSquare.imaging.TsColor;
import net.xuset.tSquare.math.rect.IRectangleR;


public class TiledBackground {
	private double drawX = 0;
	private double drawY = 0;
	private IImage backgroundImage;
	private IImage tileImage;
	
	private double lastWidth = 0, lastHeight = 0; 
	int lastBlockSize = 0;
	
	private Entity centerTo;
	
	public void setCenter(Entity e) {
		centerTo = e;
	}
	
	private void resizeTileImage(int blockSize) {
		lastBlockSize = blockSize;
		
		final int gutter = (int) (5 * blockSize / 50.0f);
		tileImage = ImageFactory.instance.createEmpty(blockSize, blockSize);
		IGraphics g = tileImage.getGraphics();
		g.setColor(TsColor.darkGray);
		g.fillRect(0, 0, blockSize, blockSize);
		g.setColor(225, 225, 225);
		g.fillRect(gutter, gutter, blockSize - 2 * gutter, blockSize - 2 * gutter);
		g.dispose();
	}
	
	private void resizeBackground(double drawBoardWidth, double drawBoardHeight) {
		lastWidth = drawBoardWidth;
		lastHeight = drawBoardHeight;
	
		final int blockSize = tileImage.getWidth();
		final int normWidth = (int) (drawBoardWidth * blockSize);
		final int normHeight = (int) (drawBoardHeight * blockSize);
		
		int width = ((normWidth % blockSize == 0) ?
				normWidth / blockSize :
					normWidth / blockSize + 1) + 1;
		int height = ((normHeight % blockSize == 0) ?
				normHeight / blockSize :
					normHeight / blockSize + 1) + 1;
		
		backgroundImage = ImageFactory.instance.createEmpty(width * blockSize, height * blockSize);
		
		IGraphics g = backgroundImage.getGraphics();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				g.drawImage(tileImage, x * blockSize, y * blockSize);
			}
		}
		g.dispose();
	}
	
	public void positionBackground(IRectangleR rect) {
		//final int gameWidth = gameGrid.getGridWidth();
		//final int gameHeight = gameGrid.getGridHeight();
		
		double centerX = 0;
		double centerY = 0;
		if (centerTo != null) {
			centerX = centerTo.getCenterX();
			centerY = centerTo.getCenterY();
		}
		
		double pivotX = centerX - rect.getWidth()/2.0;
		double pivotY = centerY - rect.getHeight()/2.0;
		
//		if (centerX > gameWidth - rect.getWidth()/2.0)
//			pivotX = gameWidth - rect.getWidth();
//		if (centerX < rect.getWidth()/2.0)
//			pivotX = 0;
//		if (centerY > gameHeight - rect.getHeight()/2.0)
//			pivotY = gameHeight - rect.getHeight();
//		if (centerY < rect.getHeight()/2.0)
//			pivotY = 0;
//		if (rect.getWidth() > gameWidth)
//			pivotX = 0;
//		if (rect.getHeight() > gameHeight)
//			pivotY = 0;

		drawX = rect.getX() + (-1 * (pivotX - ((int) (pivotX))));
		drawY = rect.getY() + (-1 * (pivotY - ((int) (pivotY))));
	}
	
	private void checkToResize(IRectangleR view, int blockSize) {
		if (blockSize != lastBlockSize)
			resizeTileImage(blockSize);
		if (view.getWidth() != lastWidth || view.getHeight() != lastHeight)
			resizeBackground(view.getWidth(), view.getHeight());
	}

	public void draw(IGraphics g, int blockSize) {
		checkToResize(g.getView(), blockSize);
		positionBackground(g.getView());
		g.drawImage(backgroundImage, (float) drawX, (float) drawY);
	}
}
