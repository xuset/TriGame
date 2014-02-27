package net.xuset.tSquare.paths;


import java.util.List;

import net.xuset.tSquare.paths.Node;



public interface PathDefiner {
	public Node chooseNextNode(List<Node> nodes);
	public boolean isValidNode(Node node, Node previous, NodeList nodeList);
	public void setNodeStats(Node node, Node parent, Node finish);
	public boolean isFinishNode(Node node, Node finish);
	public void addNode(Node n, List<Node> nodes);
}
