package net.xuset.triGame.game.entities.zombies;

import net.xuset.tSquare.paths.AStarDefiner;
import net.xuset.tSquare.paths.BiDirectionalPathFinder;
import net.xuset.tSquare.paths.Node;
import net.xuset.tSquare.paths.NodeList;
import net.xuset.tSquare.paths.Path;
import net.xuset.triGame.game.ManagerService;
import net.xuset.triGame.game.entities.buildings.BuildingManager;


class ZombiePathFinder extends BiDirectionalPathFinder {
	private final ManagerService managers;
	private int additionalBuildingG = 0;
	
	ZombiePathFinder(ManagerService managers, BuildingManager buildingManager) {
		super(buildingManager.objectGrid);
		this.managers = managers;
		setPathDefiner(new ZDefiner());
		//setDrawer(new PathDrawer()); //uncomment this to draw visual path feedback
	}
	
	boolean findPath(Path p, Zombie z) {
		additionalBuildingG = z.additionalBuildingG;
		double targetX =  z.getTarget().getCenterX();
		double targetY = z.getTarget().getCenterY();
		
		if (p != null && p.peekNextStep() != null) {
			Node.Point n = p.peekNextStep();
			return findPath(n.x, n.y, targetX, targetY);
		} else {
			return findPath(z.getCenterX(), z.getCenterY(), targetX, targetY);
		}
	}
	
	private class ZDefiner extends AStarDefiner {

		@Override
		public boolean isValidNode(Node node, Node previous, NodeList nodeList) {
			return super.isValidDiagonalMovement(node, previous, nodeList);
		}

		@Override
		public void setNodeStats(Node child, Node parent, Node finish) {
			int g = calculateNewG(child, parent);
			
			boolean blockOpen = managers.building.objectGrid.isOpen(child.x, child.y);
			if (!blockOpen)
				g += additionalBuildingG;
			
			if (g >= child.g && child.g != 0)
				return;
			
			child.g = g;
			child.h = estimateH(child, finish);
			child.f = child.g + child.h;
			child.parent = parent;
			child.fetched = true;
		}
		
	}
}
