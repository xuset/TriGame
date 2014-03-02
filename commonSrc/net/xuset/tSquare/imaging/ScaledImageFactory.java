package net.xuset.tSquare.imaging;

import net.xuset.tSquare.files.IFile;

public class ScaledImageFactory extends AbstractImageFactory{
	private final float scale;
	
	public ScaledImageFactory(float scale) {
		this.scale = scale;
	}

	@Override
	public IImage createCopy(IImage image, int dx, int dy, int dw, int dh,
			int sx, int sy, int sw, int sh) {
		
		IImage newImage = createEmpty(dw, dh);
		IGraphics g = newImage.getGraphics();
		g.drawImage(image,
				dx * scale, dy * scale,
				dw * scale, dh * scale,
				sx, sy, sw, sh);
		g.dispose();
		return newImage;
	}

	@Override
	public IImage createEmpty(int w, int h) {
		IImage img = ImageFactoryBackend.createEmpty((int) (w * scale), (int) (h * scale));
		return new ScaledImage(img, scale);
	}

	@Override
	public IImage loadImage(IFile file) {
		IImage img = ImageFactoryBackend.loadFromStream(file.getInputStream());
		return new ScaledImage(img, scale);
	}

}
