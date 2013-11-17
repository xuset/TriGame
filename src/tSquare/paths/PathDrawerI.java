package tSquare.paths;

import tSquare.game.GameIntegratable;

public interface PathDrawerI extends GameIntegratable{
	public void addToOpenNodes(Node n);
	public void addToClosedNodes(Node n);
	public void setPath(Path p);
	public void clearAll();
}
