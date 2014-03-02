package net.xuset.tSquare.imaging;

import java.io.InputStream;

import net.xuset.tSquare.files.IFile;
import net.xuset.tSquare.imaging.IImage;
import net.xuset.tSquare.imaging.ImageFactoryBackend;



public class ImageFactory extends AbstractImageFactory {
	//public static final ImageFactory instance = new ImageFactory();

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
	public IImage createEmpty(int w, int h) {
		return ImageFactoryBackend.createEmpty(w, h);
	}

	@Override
	public IImage loadImage(IFile file) {
		InputStream is = file.getInputStream();
		if (is == null)
			return null;
		return ImageFactoryBackend.loadFromStream(is);
	}
	

}
