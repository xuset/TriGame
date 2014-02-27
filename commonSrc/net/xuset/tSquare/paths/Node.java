package net.xuset.tSquare.paths;


public class Node {
	
	public class Point {
		public final int x;
		public final int y;
		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public boolean equals(Point p) {
			return (p.x == x && p.y == y);
		}
		
		@Override
		public String toString() {
			return "(" + x + ", " + y + ")";
		}
	}
	
	public final int x;
	public final int y;
	public final Point relative;
	
	public int h = 0; //estimated distance to finish
	public int g = 0; //actual distance from start node
	public int f = 0; // g + h
	public boolean fetched = false;
	public Node parent;
	
	boolean isClosed = false; //is used exclusively by BiDirectionalPathFinder
	
	Node(int x, int y) {
		this.x = x;
		this.y = y;
		relative = new Point(x, y);
	}
	
	void resetStats() {
		f = 0;
		g = 0;
		h = 0;
		fetched = false;
		parent = null;
		isClosed = false;
	}
	
	public boolean isCornerNode(Node relativeNode) {
		double difX = Math.abs(x - relativeNode.x);
		double difY = Math.abs(y - relativeNode.y);
		if (difX == 1 && difY == 1)
			return true;
		return false;
	}
	
	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
	
	public boolean isEqualTo(Node n) {
		if (x == n.x && y == n.y)
			return true;
		return false;
	}
}
