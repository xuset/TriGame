package tSquare.paths;


import java.util.Collection;

import tSquare.paths.Node;

public interface PathDefiner {
	public Node chooseNextNode(Collection<Node> nodes, PathFinder pathFinder);
	public Collection<Node> getAdjacentNodes(Node node, PathFinder pathFinder);
	public boolean isValidNode(Node node, PathFinder pathFinder);
	public boolean isFinishNode(Node node, PathFinder pathFinder);
	public void setNodeStats(Node node, PathFinder pathFinder);
}
