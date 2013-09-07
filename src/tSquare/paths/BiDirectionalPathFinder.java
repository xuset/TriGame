package tSquare.paths;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

public class BiDirectionalPathFinder implements PathFinder{
	protected Node startNode;
	protected Node finishNode;
	protected Node startFinalNode;
	protected Node finishFinalNode;
	protected NodeList nodeList;
	protected Collection<Node> finishOpenNodes = new LinkedList<Node>();
	protected Collection<Node> startOpenNodes = new LinkedList<Node>();
	protected PathDefiner pathDefiner;
	
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
		startOpenNodes.clear();
		finishOpenNodes.clear();
		nodeList.resetNodes();
		startFinalNode = null;
		finishFinalNode = null;
		startOpenNodes.add(startNode);
		finishOpenNodes.add(finishNode);
		boolean found = false;
		while (found == false && startOpenNodes.isEmpty() == false && finishOpenNodes.isEmpty() == false) {
			Node chosen = iterate(startOpenNodes, finishNode);
			iterate(finishOpenNodes, startNode);
			for (Node n : finishOpenNodes) {
				if (chosen.isEqualTo(n)) {
					finalNode = n;
					return true;
				}
			}
		}
		return false;
	}
	
	private Node iterate(Collection<Node> openNodes, Node finish) {
		Node chosen = pathDefiner.chooseNextNode(openNodes, this);
		openNodes.remove(chosen);
		Collection<Node> adjacents = pathDefiner.getAdjacentNodes(chosen, this);
		adjacents = checkValidity(adjacents, chosen, finish);
		openNodes.addAll(adjacents);
		return chosen;
	}
	
	protected Collection<Node> checkValidity(Collection<Node> nodes, Node chosen, Node finish) {
		Iterator<Node> iterator = nodes.iterator();
		while (iterator.hasNext()) {
			Node next = iterator.next();
			if (next.fetched || pathDefiner.isValidNode(next, this) == false) {
				iterator.remove();
			} else {
				setNodeStats(next, chosen, finish);
			}
		}
		return nodes;
	}
	
	private void setNodeStats(Node child, Node parent, Node finish) {
		boolean isCorner = child.isCornerNode(parent);
		int distance = (isCorner) ? 14 : 10;
		child.parent = parent;
		child.g = parent.g + distance;
		int dx = Math.abs(finish.x - child.x);
		int dy = Math.abs(finish.y - child.y);
		int diagnols = Math.min(dx, dy);
		int straights = Math.abs(dx - dy);
		child.h = straights * 10 + diagnols * 14;
		child.f = child.g + child.h;
		child.fetched = true;
	}

	public Path buildPath() {
		Path p = new Path();
		LinkedList<Node> steps = p.steps;
		for (Node current = finalNode; current != null && !start.equals(current); current = current.parent)
			steps.addFirst(n);
		for (Node n : finishOpenNodes)
			steps.addLast(n);
		return p;
	}

	public Node getStartNode() {
		return startNode;
	}

	public Node getFinishNode() {
		return finishNode;
	}

}
