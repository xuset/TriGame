package net.xuset.tSquare.imaging;

import net.xuset.tSquare.files.IFile;
import net.xuset.tSquare.math.rect.IRectangleR;

public interface IImageFactory {
	IImage createCropped(IImage image, int x, int y, int w, int h);
	IImage createCropped(IImage image, IRectangleR rect);
	
	IImage createCopy(IImage image, int dx, int dy, int dw, int dh, int sx, int sy, int sw, int sh);
	IImage createCopy(IImage image, IRectangleR dst, IRectangleR src);
	IImage createCopy(IImage image);
	
	IImage createScaled(IImage image, double sx, double sy);

	IImage createEmpty(int w, int h);
	
	IImage loadImage(String url);
	IImage loadImage(IFile file);
}
