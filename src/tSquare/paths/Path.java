package tSquare.paths;

import java.util.LinkedList;


public class Path{
	LinkedList<Node> steps = new LinkedList<Node>();
	
	public int getLength() { return steps.size(); }
	public boolean isEmpty() { return steps.isEmpty(); }
	
	public Path() {
	}
	
	Path(Node start, Node finish) {
		buildPath(start, finish);
	}
	
	void buildPath(Node start, Node finish) {
		for (Node current = finish; current != null && !start.equals(current); current = current.parent)
  			steps.addFirst(current);
	}

	public Node pollNextStep() {
		return steps.poll();
	}
	
	public Node peekNextStep() {
		return steps.peek();
	}
	
	public void clear() {
		steps.clear();
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (Node n : steps) {
			builder.append(n.toString()).append(" , ");
		}
		return builder.toString();
	}
}
