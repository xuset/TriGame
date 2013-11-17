package tSquare.paths;

import java.util.ArrayList;

public class BiDirectionalPathFinder implements PathFinder{
	public PathDrawerI pathDrawer = new EmptyDrawer();
	private Node finalLeft;
	private Node finalRight;
	protected PathDefiner pathDefiner;
	protected Node startNode;
	protected Node finishNode;
	protected NodeList nodeList;
	private AStarFinder leftFinder;
	private AStarFinder rightFinder;
	
	public BiDirectionalPathFinder(ObjectGrid...grids) {
		nodeList = new NodeList(grids);
		leftFinder = new AStarFinder(true);
		rightFinder = new AStarFinder(false);
	}
	
	protected Node normalizeXY(double x, double y) {
		int sx = ((int) x) / nodeList.grids[0].blockWidth;
		int sy =  ((int) y) / nodeList.grids[0].blockHeight;
		return  nodeList.getNode_Safe(sx, sy);
	}
	
	protected void setEndpoints(double x1, double y1, double x2, double y2) {
		startNode = normalizeXY(x1, y1);
		finishNode = normalizeXY(x2, y2);
	}

	@Override
	public boolean findPath(double x1, double y1, double x2, double y2) {
		pathDrawer.clearAll();
		nodeList.resetNodes();
		setEndpoints(x1, y1, x2, y2);
		leftFinder.setEndpoints(startNode, finishNode);
		rightFinder.setEndpoints(finishNode, startNode);
		while (!leftFinder.openNodes.isEmpty() &&
				!rightFinder.openNodes.isEmpty()) {
			
			if (leftFinder.iterate())
				return true;
			if (rightFinder.iterate())
				return true;
		}
		return false;
	}

	@Override
	public Path buildPath() {
		return buildPath(new Path());
	}
	
	public Path buildPath(Path p) {
		p.clear();
		for (Node n = finalLeft; n != null; n = n.parent)
			p.steps.addFirst(n.relative);
		for (Node n = finalRight; n != null; n = n.parent)
			p.steps.addLast(n.relative);
		pathDrawer.setPath(p);
		return p;
	}

	@Override
	public void setPathDefiner(PathDefiner pathDefiner) {
		this.pathDefiner = pathDefiner;
	}
	
	public class AStarFinder {
		private Node finish;
		public ArrayList<Node> openNodes = new ArrayList<Node>();
		public boolean left;
		
		public AStarFinder(boolean left) {
			this.left = left;
		}
		
		public void setEndpoints(Node start, Node finish) {
			this.finish = finish;
			openNodes.clear();
			start.fetched = true;
			openNodes.add(start);
		}
		
		public boolean iterate() {
			Node chosen = pathDefiner.chooseNextNode(openNodes);
			openNodes.remove(chosen);
			pathDrawer.addToClosedNodes(chosen);
			return addAdjacents(chosen);
		}
		
		private boolean addAdjacents(Node parent) {
			for (int a = -1; a <= 1; a++) {
				for (int b = -1; b <= 1; b++) {
					if (!(a == 0 && b == 0)) {
						Node child = nodeList.getNode(parent.x + a, parent.y + b);
						if (checkNode(child, parent))
							return true;
					}
				}
			}
			return false;
		}
				
		private boolean checkNode(Node child, Node parent) {
			if (child == null)
				return false;
			
			if (pathDefiner.isValidNode(child, parent, nodeList)) {
				if (checkForIntersect(child, parent))
					return true;
				pathDefiner.setNodeStats(child, parent, finish);
				if (!child.fetched) {
					pathDrawer.addToOpenNodes(child);
					openNodes.add(child);
					child.fetched = true;
				}
			}
			return false;
		}
		
		private boolean checkForIntersect(Node child, Node parent) {
			if ((child.left && !left) || (child.right && left)) {
				if (left) {
					finalLeft = parent;
					finalRight = child;
				} else {
					finalLeft = child;
					finalRight = parent;
				}
				return true;
			} else if (!child.left && !child.right) {
				child.left = left ? true : false;
				child.right = !left ? true : false;
			}
			return false;
		}
	}
}
