package net.xuset.tSquare.imaging;

import java.awt.image.BufferedImage;

public class AwtImage implements IImage {
	public final BufferedImage bufferedImg;
	
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

}
