package tSquare.paths;

import java.util.Collection;

public class AStarDefiner extends AbstractPathDefiner{

	@Override
	public Node chooseNextNode(Collection<Node> nodes) {
		Node bestFit = nodes.iterator().next();
		for (Node n : nodes) {
			if (n.f < bestFit.f)
				bestFit = n;
		}
		return bestFit;
	}
}
