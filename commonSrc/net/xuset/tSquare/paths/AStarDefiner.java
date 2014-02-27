package net.xuset.tSquare.paths;

import java.util.List;
import java.util.ListIterator;

public class AStarDefiner extends AbstractPathDefiner{

	@Override
	public void addNode(Node n, List<Node> nodes) {
		if (nodes.isEmpty()) {
			nodes.add(n);
			return;
		}
		
		for (ListIterator<Node> it = nodes.listIterator(); it.hasNext(); ) {
			Node next = it.next();
			if (n.f < next.f) {
				it.previous();
				it.add(n);
				return;
			}
		}
		nodes.add(n);
	}
}
