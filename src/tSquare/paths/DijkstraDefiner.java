package tSquare.paths;

import java.util.Collection;
import java.util.Iterator;

public class DijkstraDefiner extends AbstractPathDefiner {

	public Node chooseNodeToVisit(Collection<Node> nodes, PathFinder pathFinder) {
		Iterator<Node> iterator = nodes.iterator();
		if (iterator.hasNext())
			return iterator.next();
		return null;
	}

}
