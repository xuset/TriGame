package tSquare.paths;

import tSquare.paths.Node;

public abstract class AbstractPathDefiner implements PathDefiner {
	
	@Override
	public boolean isFinishNode(Node node, Node finish) {
		return node.isEqualTo(finish);
	}

	@Override
	public boolean isValidNode(Node node, Node previous, NodeList nodeList) {
		if (nodeList.isNodeOpen(node) && isValidDiagonalMovement(previous, node, nodeList))
			return true;
		return false;
	}
	
	protected boolean isValidDiagonalMovement(Node n1, Node n2, NodeList nodeList) {
		int difX = n1.x - n2.x;
		int difY = n1.y - n2.y;
		if (Math.abs(difX) == 1 && Math.abs(difY) == 1) {
			Node corners[] = {
				nodeList.getNode(n2.x + difX, n2.y),
				nodeList.getNode(n2.x, n2.y + difY)
			};
			for (Node n : corners) {
				if (!nodeList.isNodeOpen(n))
					return false;
			}
		}
		return true;
	}

	@Override
	public void setNodeStats(Node child, Node parent, Node finish) {
		boolean isCorner = (parent == null) ? false : child.isCornerNode(parent);
		int distance = (isCorner) ? 14 : 10;
		int g = (parent == null) ? 0 : parent.g + distance;
		
		if (g >= child.g && child.fetched)
			return;
		
		child.g = g;
		child.parent = parent;
		
		int dx = Math.abs(finish.x - child.x);
		int dy = Math.abs(finish.y - child.y);
		int diagnols = Math.min(dx, dy);
		int straights = Math.abs(dx - dy);
		
		child.h = straights * 10 + diagnols * 14;
		child.f = child.g + child.h;
	}
}
