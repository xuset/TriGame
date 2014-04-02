package net.xuset.triGame;

public class Version {
	private final int major, minor, patch;
	
	public Version(String version) {
		int[] verNum = getVersionsFromString(version);
		major = verNum[0];
		minor = verNum[1];
		patch = verNum[2];
	}
	
	private int[] getVersionsFromString(String in) {
		int[] array = new int[] { 0, 0, 0 };
		String[] split = in.split("\\.");
		
		for (int i = 0; i < 3 && i < split.length; i++)
			array[i] = toInt(split[i]);
		
		return array;
	}
	
	private int toInt(String num) {
		try {
			return Integer.parseInt(num);
		} catch (NumberFormatException ex) {
			return 0;
		}
	}
	
	public Version(int major, int minor, int patch) {
		if (major < 0.0 || minor < 0.0 || patch < 0.0)
			throw new IllegalArgumentException("Versions must be equal" +
					" to or greater than zero");
		
		this.major = major;
		this.minor = minor;
		this.patch = patch;
	}
	
	public Version(int major, int minor) {
		this(major, minor, 0);
	}
	
	public int getMajor() { return major; }
	
	public int getMinor() { return minor; }
	
	public int getPatch() { return patch; }
	
	public boolean isGreaterThan(Version version) {
		return major > version.major || minor > version.minor || patch > version.patch;
	}
	
	public boolean equals(int majorV, int minorV, int patchV) {
		return major == majorV &&
				minor == minorV &&
				patch == patchV;
	}
	
	public boolean equals(Version version) {
		return major == version.major &&
				minor == version.minor &&
				patch == version.patch;
	}
	
	@Override
	public String toString() { return major + "." + minor + "." + patch; }
}
