package tSquare.paths;

public interface PathFinder {
	public void setStart(double x, double y);
	public void setFinish(double x, double y);
	public void setPathDefiner(PathDefiner pathDefiner);
	public boolean findPath();
	public Path buildPath();
	public Node getStartNode();
	public Node getFinishNode();
}
