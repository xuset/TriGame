package tSquare.paths;

import java.util.Collection;
import java.util.Iterator;

public class DijkstraDefiner extends AbstractPathDefiner {

	@Override
	public Node chooseNextNode(Collection<Node> nodes) {
		Iterator<Node> iterator = nodes.iterator();
		if (iterator.hasNext())
			return iterator.next();
		return null;
	}

}
