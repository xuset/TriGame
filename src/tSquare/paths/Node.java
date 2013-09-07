package tSquare.paths;

import java.util.LinkedList;


public class Node {
	private NodeList nodeList;
	
	int x;
	int y;
	int relativeX;
	int relativeY;
	public int h = 0;
	public int g = 0;
	public int f = 0;
	boolean fetched = false;
	Node parent;
	
	public int getX() { return relativeX; }
	public int getY() { return relativeY; }
	
	Node(int x, int y, int relativeX, int relativeY, NodeList list) {
		this.x = x;
		this.y = y;
		this.relativeX = relativeX;
		this.relativeY = relativeY;
		this.nodeList = list;
	}
	
	public boolean isOpen() { return nodeList.isNodeOpen(x, y); }
	
	void resetStats() {
		this.f = 0;
		this.g = 0;
		this.h = 0;
		fetched = false;
		this.parent = null;
	}
	
	public boolean isCornerNode(Node relativeNode) {
		double difX = Math.abs(x - relativeNode.x);
		double difY = Math.abs(y - relativeNode.y);
		if (difX == 1 && difY == 1)
			return true;
		return false;
	}
	
	public Node[] getCornerConnectors(Node relativeNode) {
		int difX = x - relativeNode.x;
		int difY = y - relativeNode.y;
		if (Math.abs(difX) == 1 && Math.abs(difY) == 1)
			return new Node[] {nodeList.getNode(relativeNode.x + difX, relativeNode.y), nodeList.getNode(relativeNode.x, relativeNode.y + difY)};
		return null;
	}
	
	public LinkedList<Node> getAdjacentNodes() {
		LinkedList<Node> list = new LinkedList<Node>();
		for (int a = -1; a <= 1; a++) {
			for (int b = -1; b <= 1; b++) {
				if ((a == 0 && b == 0) == false) {
					Node n = nodeList.getNode(x + a, y + b);
					if (n != null)
						list.add(n);
				}
			}
		}
		return list;
	}
	
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
	
	public boolean isEqualTo(Node n) {
		if (x == n.x && y == n.y)
			return true;
		return false;
	}
}
