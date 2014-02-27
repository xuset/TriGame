package net.xuset.tSquare.paths;

public class BiDirectionalPathFinder implements PathFinder{
	private final BasePathFinder leftFinder;
	private final BasePathFinder rightFinder;
	private Node finalNode = null;
	private PathDrawerI pathDrawer;
	
	@Override
	public PathDrawerI getDrawer() { return pathDrawer; }
	
	public BiDirectionalPathFinder(ObjectGrid...grids) {
		leftFinder = new BasePathFinder(grids);
		rightFinder = new BasePathFinder(grids);
		setDrawer(new EmptyDrawer());
	}

	@Override
	public boolean findPath(double x1, double y1, double x2, double y2) {
		leftFinder.reset(x1, y1, x2, y2);
		rightFinder.reset(x2, y2, x1, y1);
		
		while (!leftFinder.openNodes.isEmpty() &&
				!rightFinder.openNodes.isEmpty()) {
			
			Node chosen = leftFinder.iterate();
			chosen.isClosed = true;
			if (rightFinder.nodeList.getNode(chosen.x, chosen.y).isClosed) {
				finalNode = chosen;
				return true;
			}
			chosen = rightFinder.iterate();
			chosen.isClosed = true;
			if (leftFinder.nodeList.getNode(chosen.x, chosen.y).isClosed) {
				finalNode = chosen;
				return true;
			}
		}
		return false;
	}

	@Override
	public Path buildPath() {
		return buildPath(new Path());
	}
	
	public Path buildPath(Path p) {
		Node n1 = leftFinder.nodeList.getNode(finalNode.x, finalNode.y);
		Node n2 = rightFinder.nodeList.getNode(finalNode.x, finalNode.y);
		p.clear();
		for (Node n = n1; n != null; n = n.parent)
			p.steps.addFirst(n.relative);
		for (Node n = n2; n != null; n = n.parent)
			p.steps.addLast(n.relative);
		pathDrawer.setPath(p);
		return p;
	}

	@Override
	public void setPathDefiner(PathDefiner pathDefiner) {
		leftFinder.setPathDefiner(pathDefiner);
		rightFinder.setPathDefiner(pathDefiner);
	}
	
	@Override
	public void setDrawer(PathDrawerI pathDrawer) {
		this.pathDrawer = pathDrawer;
		leftFinder.setDrawer(pathDrawer);
		rightFinder.setDrawer(pathDrawer);
	}
}
