package net.xuset.tSquare.imaging;

import net.xuset.tSquare.files.FileFactory;
import net.xuset.tSquare.files.IFileFactory;
import net.xuset.tSquare.math.rect.IRectangleR;

public abstract class AbstractImageFactory implements IImageFactory {
	@Override
	public IImage createCropped(IImage image, int x, int y, int w, int h) {
		return createCopy(image, 0, 0, w, h, x, y, w, h);
	}

	@Override
	public IImage createCropped(IImage image, IRectangleR rect) {
		return createCropped(image, (int) rect.getX(), (int) rect.getY(),
				(int) rect.getWidth(), (int) rect.getHeight());
	}

	@Override
	public IImage createCopy(IImage image, IRectangleR dst, IRectangleR src) {
		return createCopy(image, (int) dst.getX(), (int) dst.getY(),
				(int) dst.getWidth(), (int) dst.getHeight(),
				(int) src.getX(), (int) src.getY(),
				(int) src.getWidth(), (int) src.getHeight());
	}

	@Override
	public IImage createCopy(IImage image) {
		return createCopy(image, 0, 0, image.getWidth(), image.getHeight(),
				0, 0, image.getWidth(), image.getHeight());
	}

	@Override
	public IImage createScaled(IImage image, double sx, double sy) {
		int dw = (int) (image.getWidth() * sx);
		int dh = (int) (image.getHeight() * sy);
		return createCopy(image, 0, 0, dw, dh, 0, 0, image.getWidth(), image.getHeight());
	}

	@Override
	public IImage loadImage(String url) {
		IFileFactory ff = new FileFactory();
		return loadImage(ff.open(url));
	}
}
