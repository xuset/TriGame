package net.xuset.tSquare.paths;

import java.util.List;

public class DijkstraDefiner extends AbstractPathDefiner {

	@Override
	public void setNodeStats(Node child, Node parent, Node finish) {
		int g = calculateNewG(child, parent);
		
		if (g >= child.g && child.g != 0)
			return;
		
		child.g = g;
		child.parent = parent;
		child.fetched = true;
	}

	@Override
	public void addNode(Node n, List<Node> nodes) {
		nodes.add(n);
	}

}
