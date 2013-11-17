package tSquare.paths;

public class NodeList {
	private final Node[][] nodes;
	public final ObjectGrid[] grids;
	public final int width;
	public final int height;
	
	public NodeList(ObjectGrid...grids) {
		this.grids = grids;
		ObjectGrid main = grids[0];
		width = main.gridWidth;
		height = main.gridHeight;
		nodes = new Node[width][height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				nodes[x][y] = new Node(x, y, x * main.blockWidth + main.blockWidth / 2, y * main.blockHeight + main.blockHeight / 2);
			}
		}
	}
	
	public Node getNode(int x, int y) {
		if (x >= 0 && y >= 0 && x < width && y < height)
			return nodes[x][y];
		return null;
	}
	
	public Node getNode_Safe(int x, int y) {
		if (x >= width)
			x = width - 1;
		if (y >= height)
			y = height - 1;
		if (x < 0)
			x = 0;
		if (y < 0)
			y = 0;
		return nodes[x][y];
	}
	
	public boolean isNodeOpen(int x, int y) {
		for (ObjectGrid g : grids) {
			if (g.isOpen(x, y) == false)
				return false;
		}
		return true;
	}
	
	public boolean isNodeOpen(Node n) {
		return isNodeOpen(n.x, n.y);
	}
	
	public void resetNodes() {
		for (Node[] nn : nodes) {
			for (Node n : nn) {
				n.resetStats();
			}
		}
	}
}
