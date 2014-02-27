package net.xuset.tSquare.paths;

public interface PathFinder {
	public void setPathDefiner(PathDefiner pathDefiner);
	public boolean findPath(double x1, double y1, double x2, double y2);
	public Path buildPath();
	public void setDrawer(PathDrawerI pathDrawer);
	public PathDrawerI getDrawer();
}
