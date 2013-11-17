package tSquare.paths;


import java.util.Collection;

import tSquare.paths.Node;

public interface PathDefiner {
	public Node chooseNextNode(Collection<Node> nodes);
	public boolean isValidNode(Node node, Node previous, NodeList nodeList);
	public void setNodeStats(Node node, Node parent, Node finish);
	public boolean isFinishNode(Node node, Node finish);
}
