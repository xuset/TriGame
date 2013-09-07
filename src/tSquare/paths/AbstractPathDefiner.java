package tSquare.paths;


import java.util.Collection;

import tSquare.paths.Node;

public abstract class AbstractPathDefiner implements PathDefiner {
	protected Node chosenNode;
	
	public abstract Node chooseNodeToVisit(Collection<Node> nodes, PathFinder pathFinder);
	
	public Collection<Node> getAdjacentNodes(Node node, PathFinder pathFinder) {
		return node.getAdjacentNodes();
	}

	public boolean isValidNode(Node node, PathFinder pathFinder) {
		if (node.isOpen() && isValidDiagonalMovement(chosenNode, node))
			return true;
		return false;
	}
	
	private boolean isValidDiagonalMovement(Node n1, Node n2) {
		if (n1.isCornerNode(n2)) {
			Node[] nodes = n1.getCornerConnectors(n2);
			for (int i = 0; i < nodes.length; i++) {
				if (nodes[i].isOpen() == false)
					return false;
			}
		}
		return true;
	}

	public boolean isFinishNode(Node node, PathFinder pathFinder) {
		if (node.isEqualTo(pathFinder.getFinishNode()))
			return true;
		return false;
	}

	public Node chooseNextNode(Collection<Node> nodes, PathFinder pathFinder) {
		chosenNode = chooseNodeToVisit(nodes, pathFinder);
		return chosenNode;
	}

	public void setNodeStats(Node node, PathFinder pathFinder) {
		Node child = node;
		Node parent = chosenNode;
		Node finish = pathFinder.getFinishNode();
		boolean isCorner = child.isCornerNode(parent);
		int distance = (isCorner) ? 14 : 10;
		child.parent = parent;
		child.g = parent.g + distance;
		int dx = Math.abs(finish.x - child.x);
		int dy = Math.abs(finish.y - child.y);
		int diagnols = Math.min(dx, dy);
		int straights = Math.abs(dx - dy);
		child.h = straights * 10 + diagnols * 14;
		child.f = child.g + child.h;
		child.fetched = true;
	}
}
