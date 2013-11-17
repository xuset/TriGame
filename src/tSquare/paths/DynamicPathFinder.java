package tSquare.paths;

import java.util.Collection;
import java.util.Iterator;


public class DynamicPathFinder extends BasePathFinder {
	protected DynamicWrapper dynamicWrapper = new DynamicWrapper();
	protected Path modifiedPath;
	
	public DynamicPathFinder(ObjectGrid...grids) {
		super(grids);
	}
	
	public DynamicPathFinder(NodeList nodeList) {
		super(nodeList);
	}

	@Override
	public void setPathDefiner(PathDefiner pathDefiner) {
		dynamicWrapper.pathDefiner = pathDefiner;
		super.setPathDefiner(pathDefiner);
	}

	public boolean findPath(Path path, double x1, double y1, double x2, double y2) {
		if (path == null)
			return findPath(x1, y1, x2, y2);
		
		modifiedPath = path;
		super.setPathDefiner(dynamicWrapper);
		//swapping start and finish is needed for dynamic path finding
		boolean found = super.findPath(x2, y2, x1, y1);
		return found;
	}

	@Override
	public boolean findPath(double x1, double y1, double x2, double y2) {
		modifiedPath = null;
		super.setPathDefiner(dynamicWrapper.pathDefiner);
		return super.findPath(x1, y1, x2, y2);
	}

	@Override
	public Path buildPath() {
		if (modifiedPath == null)
			return super.buildPath();
		
		Path p = buildModifiedPath();
		modifiedPath = null;
		return p;
	}
	
	protected Path buildModifiedPath() {
		for (Node current = finalNode; current != null ; current = current.parent) {
			modifiedPath.steps.add(current.relative);
			if (startNode.equals(current))
  				break;
  			
		}
		getDrawer().setPath(modifiedPath);
		return modifiedPath;
	}
	
	private class DynamicWrapper implements PathDefiner { //modifies user defined PathDefiner only at method isFinishNode
		public PathDefiner pathDefiner;

		@Override
		public boolean isFinishNode(Node node, Node finish) {
			Iterator<Node.Point> it = DynamicPathFinder.this.modifiedPath.steps.iterator();
			if (node.isEqualTo(DynamicPathFinder.this.finishNode)) {
				clearPath(it);
				return true;
			}
			while (it.hasNext()) {
				Node.Point next = it.next();
				if (next.equals(node.relative)) {
					it.remove();
					clearPath(it);
					return true;
				}
			}
			return false;
		}
		
		private void clearPath(Iterator<Node.Point> it) {
			while (it.hasNext()) {
				it.next();
				it.remove();
			}
		}

		@Override public final Node chooseNextNode(Collection<Node> nodes)
			{ return pathDefiner.chooseNextNode(nodes); }
		@Override public final boolean isValidNode(Node node, Node previous, NodeList nodeList)
			{ return pathDefiner.isValidNode(node, previous, nodeList); }
		@Override public final void setNodeStats(Node child, Node parent, Node finish)
			{ pathDefiner.setNodeStats(child, parent, finish); }
		
	}
}
