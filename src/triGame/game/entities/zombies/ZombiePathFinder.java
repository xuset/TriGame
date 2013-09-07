package triGame.game.entities.zombies;

import java.util.Collection;
import java.util.Iterator;

import tSquare.paths.AStarDefiner;
import tSquare.paths.DynamicPathFinder;
import tSquare.paths.Node;
import tSquare.paths.PathFinder;
import triGame.game.TriGame;
import triGame.game.entities.Person;
import triGame.game.entities.building.Building;
import triGame.game.safeArea.SafeAreaBoard;

public class ZombiePathFinder extends DynamicPathFinder {
	private Collection<Building> buildings;
	private Collection<Person> persons;
	private SafeAreaBoard safeBoard;
	private Zombie zombie;
	
	public ZombiePathFinder(TriGame game) {
		super(game.wallManager.objectGrid);
		buildings = game.buildingManager.getList();
		persons = game.personManager.getList();
		this.safeBoard = game.safeBoard;
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
			int x = n.getX() - TriGame.BLOCK_WIDTH/2;
			int y = n.getY() - TriGame.BLOCK_WIDTH/2;
			final int width = TriGame.BLOCK_WIDTH;
			final int height = TriGame.BLOCK_HEIGHT;
			for (Person p : persons) {
				if (p.hitbox.contains(x, y, width, height) || p.hitbox.intersects(x, y, width, height)) {
					zombie.target = p;
					return true;
				}
			}
			for (Building b : buildings) {
				if (b.hitbox.contains(x, y, width, height) || b.hitbox.intersects(x, y, width, height)) {
					zombie.target = b;
					return true;
				}
			}
			return false;
		}
		
		public Collection<Node> getAdjacentNodes(Node n) { //can optimize by putting Node.getAdjacentNodes() inside this. would remove iterator
			Collection<Node> col = n.getAdjacentNodes();
			Iterator<Node> it = col.iterator();
			while (it.hasNext()) {
				Node next = it.next();
				if (safeBoard.insideDrawSquare(next.getX(), next.getY()) == false)
					it.remove();
			}
			return col;
		}
	}
}
