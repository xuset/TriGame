package net.xuset.tSquare.imaging;

import java.awt.image.BufferedImage;

public class AwtImage implements IImage {
	private final BufferedImage bufferedImg;
	
	AwtImage(BufferedImage image) {
		this.bufferedImg = image;
	}

	@Override
	public int getWidth() {
		return bufferedImg.getWidth();
	}

	@Override
	public int getHeight() {
		return bufferedImg.getHeight();
	}

	@Override
	public IGraphics getGraphics() {
		return new AwtGraphics(bufferedImg.getGraphics(), getWidth(), getHeight());
	}

	@Override
	public float getWidth(IGraphics g) {
		return g.getWidthUnits(this);
	}

	@Override
	public float getHeight(IGraphics g) {
		return g.getHeightUnits(this);
	}
	
	@Override
	public BufferedImage getBackend() {
		return bufferedImg;
	}

}
