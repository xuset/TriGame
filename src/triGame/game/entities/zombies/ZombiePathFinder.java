package triGame.game.entities.zombies;

import tSquare.paths.AStarDefiner;
import tSquare.paths.BiDirectionalPathFinder;
import tSquare.paths.Node;
import tSquare.paths.NodeList;
import tSquare.paths.Path;
import triGame.game.ManagerService;
import triGame.game.entities.buildings.BuildingManager;

public class ZombiePathFinder extends BiDirectionalPathFinder {
	private final ManagerService managers;
	
	public ZombiePathFinder(ManagerService managers, BuildingManager buildingManager) {
		super(buildingManager.objectGrid);
		this.managers = managers;
		setPathDefiner(new ZDefiner());
		//setDrawer(new PathDrawer()); //uncomment this to draw visual path feedback
	}
	
	public boolean findPath(Path p, Zombie z) {
		if (p != null && p.peekNextStep() != null) {
			Node.Point n = p.peekNextStep();
			return findPath(n.x, n.y, z.target.getCenterX(), z.target.getCenterY());
		} else {
			return findPath(z.getCenterX(), z.getCenterY(), z.target.getCenterX(), z.target.getCenterY());
		}
	}
	
	public class ZDefiner extends AStarDefiner {
		private static final int additionalBuildingG = 70;

		@Override
		public boolean isFinishNode(Node node, Node finish) {
			return super.isFinishNode(node, finish);
		}

		@Override
		public boolean isValidNode(Node node, Node previous, NodeList nodeList) {
			return super.isValidDiagonalMovement(node, previous, nodeList);
		}

		@Override
		public void setNodeStats(Node child, Node parent, Node finish) {
			int g = calculateNewG(child, parent);
			if (!managers.building.objectGrid.isOpen(child.x, child.y))
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
