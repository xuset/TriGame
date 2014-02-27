package net.xuset.tSquare.system;

import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.math.rect.IRectangleR;
import net.xuset.tSquare.system.input.InputHolder;

public interface IDrawBoard {
	int getWidth();
	int getHeight();
	IGraphics getGraphics();
	void flushScreen();
	void clearScreen();
	InputHolder createInputListener();
	Object getBackend();
	IRectangleR getWindow();
}
