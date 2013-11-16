package triGame.game.entities.zombies;

import tSquare.paths.AStarDefiner;
import tSquare.paths.DynamicPathFinder;
import tSquare.paths.Node;
import tSquare.paths.PathFinder;
import triGame.game.ManagerService;
import triGame.game.Params;
import triGame.game.entities.Person;
import triGame.game.entities.buildings.Building;
import triGame.game.entities.buildings.BuildingManager;

public class ZombiePathFinder extends DynamicPathFinder {
	private final ManagerService managers;
	private Zombie zombie;
	
	public ZombiePathFinder(ManagerService managers, BuildingManager buildingManager) {
		super(buildingManager.objectGrid);
		this.managers = managers;
		setPathDefiner(new ZombieWrapper());
	}
	
	public void setZombie(Zombie z) {
		super.setStart((int) z.getCenterX(), (int) z.getCenterY());
		super.setFinish((int) z.target.getCenterX(), (int) z.target.getCenterY());
		zombie = z;
	}
	
	/*public Path buildShortestPath(Path p) {
		Node shortest = nodeList.getNode(0, 0);
		for (int x = 0; x < nodeList.getWidth(); x++) {
			for (int y = 0; y < nodeList.getHeight(); y++) {
				Node temp = nodeList.getNode(x, y);
				if (temp.h < shortest.h)
					shortest = temp;
			}
		}
		if (shortest.h != 0) {
			p.clear();
			
		}
		return null;
	}*/
	
	public class ZombieWrapper extends AStarDefiner {
		public boolean isFinishNode(Node n, PathFinder pathFinder) {
			int x = n.getX() - Params.BLOCK_SIZE/2;
			int y = n.getY() - Params.BLOCK_SIZE/2;
			final int width = Params.BLOCK_SIZE;
			final int height = Params.BLOCK_SIZE;
			for (Person p : managers.person.list) {
				if (p.hitbox.contains(x, y, width, height) || p.hitbox.intersects(x, y, width, height)) {
					zombie.target = p;
					return true;
				}
			}
			for (Building b : managers.building.list) {
				if (b.hitbox.contains(x, y, width, height) || b.hitbox.intersects(x, y, width, height)) {
					zombie.target = b;
					return true;
				}
			}
			return false;
		}
		
		/*public Collection<Node> getAdjacentNodes(Node n) { //can optimize by putting Node.getAdjacentNodes() inside this. would remove iterator
			Collection<Node> col = n.getAdjacentNodes();
			Iterator<Node> it = col.iterator();
			while (it.hasNext()) {
				Node next = it.next();
				if (safeBoard.insideDrawSquare(next.getX(), next.getY()) == false)
					it.remove();
			}
			return col;
		}*/
	}
}
