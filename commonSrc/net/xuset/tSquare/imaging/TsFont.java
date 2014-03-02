package net.xuset.tSquare.imaging;

public class TsFont implements IFont{
	private final String name;
	private final float size;
	private final TsTypeFace typeFace;
	
	public TsFont(String name, float size, TsTypeFace typeFace) {
		this.name = name;
		this.size = size;
		this.typeFace = typeFace;
	}
	
	public TsFont(String name, int size) {
		this(name, size, TsTypeFace.PLAIN);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public TsTypeFace getTypeFace() {
		return typeFace;
	}

	@Override
	public float getSize() {
		return size;
	}
}
