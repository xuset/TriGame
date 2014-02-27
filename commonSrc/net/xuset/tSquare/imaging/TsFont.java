package net.xuset.tSquare.imaging;

public class TsFont implements IFont{
	private final String name;
	private final int size;
	private final TsTypeFace typeFace;
	
	public TsFont(String name, int size, TsTypeFace typeFace) {
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
	public int getSize() {
		return size;
	}
}
