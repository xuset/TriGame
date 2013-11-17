package tSquare.paths;

import java.util.LinkedList;


public class Path{
	LinkedList<Node.Point> steps = new LinkedList<Node.Point>();
	
	public int getLength() { return steps.size(); }
	public boolean isEmpty() { return steps.isEmpty(); }
	
	public Path() {
	}
	
	Path(Node start, Node finish) {
		buildPath(start, finish);
	}
	
	void buildPath(Node start, Node finish) {
		for (Node current = finish; current != null; current = current.parent) {
  			steps.addFirst(current.relative);
  			if (start.equals(current))
  				break;
		}
	
	}

	public Node.Point pollNextStep() {
		return steps.poll();
	}
	
	public Node.Point peekNextStep() {
		return steps.peek();
	}
	
	public void clear() {
		steps.clear();
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (Node.Point n : steps) {
			builder.append(n.toString()).append(" , ");
		}
		return builder.toString();
	}
}
