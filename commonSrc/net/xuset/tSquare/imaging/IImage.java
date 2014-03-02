package net.xuset.tSquare.imaging;

public interface IImage {
	int getWidth();
	int getHeight();
	IGraphics getGraphics();
	float getWidth(IGraphics g);
	float getHeight(IGraphics g);
	Object getBackend();
}
