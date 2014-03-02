package net.xuset.triGame.game;

import net.xuset.tSquare.game.GameDrawable;
import net.xuset.tSquare.game.entity.Entity;
import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.IImage;
import net.xuset.tSquare.imaging.IImageFactory;
import net.xuset.tSquare.imaging.TsColor;
import net.xuset.tSquare.math.rect.IRectangleR;


public class TiledBackground implements GameDrawable{
	private IImageFactory imgFactory;
	private double drawX = 0;
	private double drawY = 0;
	private IImage backgroundImage;
	private IImage tileImage;
	
	private double lastWidth = 0, lastHeight = 0; 
	private boolean needsResizing = false;
	
	private Entity centerTo;
	
	TiledBackground(IImageFactory imgFactory) {
		this.imgFactory = imgFactory;
		resizeTileImage();
		needsResizing = true;
	}
	
	void setImageFactory(IImageFactory imgFactory) {
		this.imgFactory = imgFactory;
		resizeTileImage();
	}
	
	public void setCenter(Entity e) {
		centerTo = e;
	}
	
	private void resizeTileImage() {
		final float gutter = (float) (5 / 50.0f);
		tileImage = imgFactory.createEmpty(1, 1);
		IGraphics g = tileImage.getGraphics();
		g.setColor(TsColor.darkGray);
		g.fillRect(0, 0, 1, 1);
		g.setColor(225, 225, 225);
		g.fillRect(gutter, gutter, 1 - 2 * gutter, 1 - 2 * gutter);
		g.dispose();
	}
	
	private void resizeBackground(double drawBoardWidth, double drawBoardHeight) {
		lastWidth = drawBoardWidth;
		lastHeight = drawBoardHeight;
		needsResizing = false;
	
		int normWidth = (int) drawBoardWidth;
		int normHeight = (int) drawBoardHeight;
		
		int width = ((drawBoardWidth - normWidth == 0) ?
				normWidth + 1 : normWidth + 2);
		int height = ((drawBoardHeight - normHeight == 0) ?
				normHeight + 1 : normHeight + 2);
		
		backgroundImage = imgFactory.createEmpty(width, height);
		
		IGraphics g = backgroundImage.getGraphics();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				g.drawImage(tileImage, x, y);
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
	
	private void checkToResize(IRectangleR view) {
		if (needsResizing ||
				view.getWidth() != lastWidth || view.getHeight() != lastHeight) {
			resizeBackground(view.getWidth(), view.getHeight());
		}
	}

	@Override
	public void draw(IGraphics g) {
		checkToResize(g.getView());
		positionBackground(g.getView());
		g.drawImage(backgroundImage, (float) drawX, (float) drawY);
	}
}
