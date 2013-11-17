package tSquare.paths;

import java.util.ArrayList;

public class BiDirectionalPathFinder implements PathFinder{
	private final Finder leftFinder;
	private final Finder rightFinder;
	private final int blockWidth;
	private final int blockHeight;
	private Node finalNode = null;
	private PathDefiner pathDefiner;
	
	public PathDrawerI pathDrawer = new EmptyDrawer();
	
	public BiDirectionalPathFinder(ObjectGrid...grids) {
		leftFinder = new Finder(true, grids);
		rightFinder = new Finder(false, grids);
		blockWidth = grids[0].blockWidth;
		blockHeight = grids[0].blockHeight;
	}
	
	private int normalize(double x, int block) {
		return ((int) x) / block;
	}

	@Override
	public boolean findPath(double x1, double y1, double x2, double y2) {
		pathDrawer.clearAll();
		int sx = normalize(x1, blockWidth), sy = normalize(y1, blockHeight);
		int fx = normalize(x2, blockWidth), fy = normalize(y2, blockHeight);
		leftFinder.setEndpoints(sx, sy, fx, fy);
		rightFinder.setEndpoints(fx, fy, sx, sy);
		
		while (!leftFinder.openNodes.isEmpty() &&
				!rightFinder.openNodes.isEmpty()) {
			
			finalNode = leftFinder.iterate();
			if (finalNode != null)
				return true;
			finalNode = rightFinder.iterate();
			if (finalNode != null)
				return true;
		}
		return false;
	}

	@Override
	public Path buildPath() {
		return buildPath(new Path());
	}
	
	public Path buildPath(Path p) {
		Node n1 = getFinder(true).nodeList.getNode(finalNode.x, finalNode.y);
		Node n2 = getFinder(false).nodeList.getNode(finalNode.x, finalNode.y);
		p.clear();
		for (Node n = n1; n != null; n = n.parent)
			p.steps.addFirst(n.relative);
		for (Node n = n2; n != null; n = n.parent)
			p.steps.addLast(n.relative);
		pathDrawer.setPath(p);
		return p;
	}

	@Override
	public void setPathDefiner(PathDefiner pathDefiner) {
		this.pathDefiner = pathDefiner;
	}
	
	private Finder getFinder(boolean left) {
		if (left)
			return leftFinder;
		else
			return rightFinder;
	}
	
	private class Finder {
		public final ArrayList<Node> openNodes = new ArrayList<Node>();
		public final NodeList nodeList;
		public final boolean left;
		private Node finish;
		
		public Finder(boolean left, ObjectGrid...grids) {
			this.left = left;
			nodeList = new NodeList(grids);
		}
		
		public void setEndpoints(int sx, int sy, int fx, int fy) {
			nodeList.resetNodes();
			openNodes.clear();
			this.finish = nodeList.getNode_Safe(fx, fy);
			Node start = nodeList.getNode_Safe(sx, sy);
			start.fetched = true;
			openNodes.add(start);
		}
		
		public Node iterate() {
			Node chosen = pathDefiner.chooseNextNode(openNodes);
			openNodes.remove(chosen);
			pathDrawer.addToClosedNodes(chosen);
			if (checkForIntersect(chosen))
				return chosen;
			chosen.isClosed = true;
			addAdjacents(chosen);
			return null;
		}
		
		private void addAdjacents(Node parent) {
			for (int a = -1; a <= 1; a++) {
				for (int b = -1; b <= 1; b++) {
					if (!(a == 0 && b == 0)) {
						Node child = nodeList.getNode(parent.x + a, parent.y + b);
						checkNode(child, parent);
					}
				}
			}
		}
				
		private void checkNode(Node child, Node parent) {
			if (child == null)
				return;
			
			if (pathDefiner.isValidNode(child, parent, nodeList)) {
				boolean alreadyFetched = child.fetched;
				pathDefiner.setNodeStats(child, parent, finish);
				if (!alreadyFetched) {
					pathDrawer.addToOpenNodes(child);
					openNodes.add(child);
				}
			}
		}
		
		private boolean checkForIntersect(Node child) {
			Finder f = getFinder(!left);
			Node other = f.nodeList.getNode(child.x, child.y);
			return other.isClosed;
		}
	}
}
