package tSquare.paths;

import java.util.Collection;
import java.util.LinkedList;

public class BasePathFinder implements PathFinder{
	public PathDrawerI pathDrawer = new EmptyDrawer();
	protected Node startNode;
	protected Node finishNode;
	protected Node finalNode;
	protected NodeList nodeList;
	protected Collection<Node> openNodes = new LinkedList<Node>();
	protected PathDefiner pathDefiner;
	
	public BasePathFinder(ObjectGrid...grids) {
		this.nodeList = new NodeList(grids);
	}
	
	public BasePathFinder(NodeList nodeList) {
		this.nodeList = nodeList;
	}
	
	protected void setStartFinishNodes(double x1, double y1, double x2, double y2) {
		startNode = normalizeXY(x1, y1);
		finishNode = normalizeXY(x2, y2);
	}
	
	protected Node normalizeXY(double x, double y) {
		int sx = ((int) x) / nodeList.grids[0].blockWidth;
		int sy =  ((int) y) / nodeList.grids[0].blockHeight;
		return  nodeList.getNode_Safe(sx, sy);
	}

	@Override
	public void setPathDefiner(PathDefiner pathDefiner) {
		this.pathDefiner = pathDefiner;
	}

	@Override
	public boolean findPath(double x1, double y1, double x2, double y2) {
		pathDrawer.clearAll();
		setStartFinishNodes(x1, y1, x2, y2);
		openNodes.clear();
		nodeList.resetNodes();
		finalNode = null;
		openNodes.add(startNode);
		while (openNodes.isEmpty() == false) {
			Node chosen = pathDefiner.chooseNextNode(openNodes);
			pathDrawer.addToClosedNodes(chosen);
			if (pathDefiner.isFinishNode(chosen, finishNode)) {
				finalNode = chosen;
				return true;
			} else {
				openNodes.remove(chosen);
				addAdjacents(chosen);
			}
		}
		return false;
	}
	
	protected void addAdjacents(Node parent) {
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
			pathDefiner.setNodeStats(child, parent, finishNode);
			if (!alreadyFetched) {
				pathDrawer.addToOpenNodes(child);
				openNodes.add(child);
			}
		}
	}

	@Override
	public Path buildPath() {
		Path p = new Path(startNode, finalNode);
		pathDrawer.setPath(p);
		return p;
	}
}
