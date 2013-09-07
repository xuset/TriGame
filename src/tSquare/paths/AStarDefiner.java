package tSquare.paths;

import java.util.Collection;

public class AStarDefiner extends AbstractPathDefiner{

	public Node chooseNodeToVisit(Collection<Node> nodes, PathFinder pathFinder) {
		Node bestFit = nodes.iterator().next();
		for (Node n : nodes) {
			if (n.f < bestFit.f)
				bestFit = n;
		}
		return bestFit;
	}
}
