package tSquare.paths;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

public class BasePathFinder implements PathFinder{
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
	
	public void setStart(double x, double y) {
		int sx = ((int) x) / nodeList.getGrids()[0].getBlockWidth();
		int sy =  ((int) y) / nodeList.getGrids()[0].getBlockHeight();
		startNode = nodeList.getNode_Safe(sx, sy);
	}

	public void setFinish(double x, double y) {
		int fx = ((int) x) / nodeList.getGrids()[0].getBlockWidth();
		int fy =  ((int) y) / nodeList.getGrids()[0].getBlockHeight();
		finishNode = nodeList.getNode_Safe(fx, fy);
	}

	public void setPathDefiner(PathDefiner pathDefiner) {
		this.pathDefiner = pathDefiner;
	}

	public boolean findPath() {
		openNodes.clear();
		nodeList.resetNodes();
		finalNode = null;
		openNodes.add(startNode);
		while (openNodes.isEmpty() == false) {
			Node chosen = pathDefiner.chooseNextNode(openNodes, this);
			if (pathDefiner.isFinishNode(chosen, this)) {
				finalNode = chosen;
				return true;
			} else {
				openNodes.remove(chosen);
				Collection<Node> adjacents = pathDefiner.getAdjacentNodes(chosen, this);
				adjacents = checkValidity(adjacents);
				openNodes.addAll(adjacents);
			}
		}
		return false;
	}
	
	protected Collection<Node> checkValidity(Collection<Node> nodes) {
		Iterator<Node> iterator = nodes.iterator();
		while (iterator.hasNext()) {
			Node next = iterator.next();
			if (next.fetched || pathDefiner.isValidNode(next, this) == false) {
				iterator.remove();
			} else {
				pathDefiner.setNodeStats(next, this);
			}
		}
		return nodes;
	}

	public Path buildPath() {
		Path p = new Path(startNode, finalNode);
		return p;
	}

	public Node getStartNode() { return startNode; }

	public Node getFinishNode() { return finishNode; }

}
