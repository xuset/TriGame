package net.xuset.tSquare.imaging;

public class ScaledImage implements IImage {
	private final float scale;
	private final IImage image;
	
	public ScaledImage(IImage image, float scale) {
		this.scale = scale;
		this.image = image;
	}

	@Override
	public int getWidth() {
		return image.getWidth();
	}

	@Override
	public int getHeight() {
		return image.getHeight();
	}

	@Override
	public IGraphics getGraphics() {
		return new ScaledGraphics(image.getGraphics(), scale);
	}

	@Override
	public float getWidth(IGraphics g) {
		return image.getWidth(g);
	}

	@Override
	public float getHeight(IGraphics g) {
		return image.getHeight(g);
	}
	
	@Override
	public Object getBackend() {
		return image.getBackend();
	}

}
