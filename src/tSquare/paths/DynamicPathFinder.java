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
	
	public void setPathDefiner(PathDefiner pathDefiner) {
		setPathDefiner = pathDefiner;
		dynamicWrapper.reset(this, pathDefiner);
		super.setPathDefiner(pathDefiner);
	}
	
	public boolean findPath(Path path) {
		if (path != null) {
			wasModified = true;
			modifiedPath = path;
			super.setPathDefiner(dynamicWrapper);
			Node temp = startNode;
			startNode = finishNode;
			finishNode = temp; //swapping start and finish is needed for dynamic path finding
			boolean found = super.findPath();
			finishNode = startNode;
			startNode = temp;
			return found;
		} else
			return findPath();
	}
	
	public boolean findPath() {
		wasModified = false;
		super.setPathDefiner(setPathDefiner);
		return super.findPath();
	}
	
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
		for (Node current = finalNode; current != null && !finishNode.equals(current); current = current.parent)
  			modifiedPath.steps.add(current);
		modifiedPath.steps.add(finishNode);
		return modifiedPath;
	}
	
	public class DynamicWrapper implements PathDefiner { //modifies user defined PathDefiner only at method isFinishNode
		private PathDefiner pathDefiner;
		private DynamicPathFinder dynamicFinder;
		
		public void reset(DynamicPathFinder dynamicFinder, PathDefiner pathDefiner) {
			this.pathDefiner = pathDefiner;
			this.dynamicFinder = dynamicFinder;
		}
		
		public boolean isFinishNode(Node node, PathFinder pathFinder) {
			Iterator<Node> it = dynamicFinder.modifiedPath.steps.iterator();
			if (node.isEqualTo(dynamicFinder.finishNode)) {
				clearPath(it);
				return true;
			}
			while (it.hasNext()) {
				Node next = it.next();
				if (next.isEqualTo(node)) {
					it.remove();
					clearPath(it);
					return true;
				}
			}
			return false;
		}
		
		private void clearPath(Iterator<Node> it) {
			while (it.hasNext()) {
				it.next();
				it.remove();
			}
		}
		
		public final Node chooseNextNode(Collection<Node> nodes, PathFinder pathFinder) { return pathDefiner.chooseNextNode(nodes, pathFinder); }
		public final Collection<Node> getAdjacentNodes(Node node, PathFinder pathFinder) { return pathDefiner.getAdjacentNodes(node, pathFinder); }
		public final boolean isValidNode(Node node, PathFinder pathFinder) { return pathDefiner.isValidNode(node, pathFinder); }
		public final void setNodeStats(Node node, PathFinder pathFinder) { pathDefiner.setNodeStats(node, pathFinder); }
		
	}
}
