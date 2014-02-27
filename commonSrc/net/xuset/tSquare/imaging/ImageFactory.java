package net.xuset.tSquare.imaging;

import java.io.InputStream;

import net.xuset.tSquare.files.FileFactory;
import net.xuset.tSquare.files.IFile;
import net.xuset.tSquare.imaging.IImage;
import net.xuset.tSquare.imaging.IImageFactory;
import net.xuset.tSquare.imaging.ImageFactoryBackend;
import net.xuset.tSquare.math.rect.IRectangleR;



public class ImageFactory implements IImageFactory {
	public static final ImageFactory instance = new ImageFactory();

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
	public IImage createCopy(IImage image, int dx, int dy, int dw, int dh,
			int sx, int sy, int sw, int sh) {
		IImage newImage = createEmpty(dw, dh);
		IGraphics g = newImage.getGraphics();
		g.drawImage(image, dx, dy, dw, dh, sx, sy, sw, sh);
		g.dispose();
		return newImage;
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
	public IImage createEmpty(int w, int h) {
		return ImageFactoryBackend.createEmpty(w, h);
	}

	@Override
	public IImage loadImage(String url) {
		return loadImage(FileFactory.instance.open(url));
	}

	@Override
	public IImage loadImage(IFile file) {
		InputStream is = file.getInputStream();
		if (is == null)
			return null;
		return ImageFactoryBackend.loadFromStream(is);
	}
	

}
