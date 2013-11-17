package tSquare.paths;

import java.util.Collection;
import java.util.Iterator;


public class DynamicPathFinder extends BasePathFinder {
	private boolean wasModified = false;
	
	protected DynamicWrapper dynamicWrapper = new DynamicWrapper();
	protected PathDefiner setPathDefiner;
	protected Path modifiedPath;
	
	public DynamicPathFinder(ObjectGrid...grids) {
		super(grids);
	}
	
	public DynamicPathFinder(NodeList nodeList) {
		super(nodeList);
	}

	@Override
	public void setPathDefiner(PathDefiner pathDefiner) {
		setPathDefiner = pathDefiner;
		dynamicWrapper.reset(this, pathDefiner);
		super.setPathDefiner(pathDefiner);
	}

	public boolean findPath(Path path, double x1, double y1, double x2, double y2) {
		if (path != null) {
			wasModified = true;
			modifiedPath = path;
			super.setPathDefiner(dynamicWrapper);
			//swapping start and finish is needed for dynamic path finding
			boolean found = super.findPath(x2, y2, x1, y1);
			return found;
		} else
			return findPath(x1, y1, x2, y2);
	}

	@Override
	public boolean findPath(double x1, double y1, double x2, double y2) {
		wasModified = false;
		super.setPathDefiner(setPathDefiner);
		return super.findPath(x1, y1, x2, y2);
	}

	@Override
	public Path buildPath() {
		if (wasModified) {
			Path p = buildModifiedPath();
			wasModified = false;
			modifiedPath = null;
			return p;
		}
			return super.buildPath();
	}
	
	protected Path buildModifiedPath() {
		for (Node current = finalNode; current != null ; current = current.parent) {
			modifiedPath.steps.add(current.relative);
			if (startNode.equals(current))
  				break;
  			
		}
		pathDrawer.setPath(modifiedPath);
		return modifiedPath;
	}
	
	public class DynamicWrapper implements PathDefiner { //modifies user defined PathDefiner only at method isFinishNode
		private PathDefiner pathDefiner;
		private DynamicPathFinder dynamicFinder;
		
		public void reset(DynamicPathFinder dynamicFinder, PathDefiner pathDefiner) {
			this.pathDefiner = pathDefiner;
			this.dynamicFinder = dynamicFinder;
		}

		@Override
		public boolean isFinishNode(Node node, Node finish) {
			Iterator<Node.Point> it = dynamicFinder.modifiedPath.steps.iterator();
			if (node.isEqualTo(dynamicFinder.finishNode)) {
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
