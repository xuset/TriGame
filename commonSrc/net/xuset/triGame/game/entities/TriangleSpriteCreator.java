package net.xuset.triGame.game.entities;

import net.xuset.tSquare.imaging.IImage;
import net.xuset.tSquare.imaging.Sprite;
import net.xuset.tSquare.imaging.TsColor;
import net.xuset.triGame.game.Load;

public class TriangleSpriteCreator {
	private final int blockSize;
	
	public TriangleSpriteCreator(int blockSize) {
		this.blockSize = blockSize;
	}
	
	public Sprite createTriangle(int color) {
		IImage personImage = Load.triangleImage(new TsColor(color), blockSize);
		return new Sprite("", personImage, 1.0f / blockSize);
	}
}
