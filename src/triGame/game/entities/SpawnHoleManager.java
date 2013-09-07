package triGame.game.entities;

import tSquare.game.GameBoard;
import tSquare.game.Manager;
import tSquare.game.ManagerController;
import tSquare.paths.ObjectGrid;

public class SpawnHoleManager extends Manager<SpawnHole>{
	public static final String HASH_MAP_KEY = "spawnHole";
	
	public ObjectGrid objectGrid;
	public SpawnHole[] initialSpawnHoles;
	
	public SpawnHoleManager(ManagerController controller, GameBoard gameBoard) {
		super(controller, gameBoard, HASH_MAP_KEY);
		objectGrid = new ObjectGrid(gameBoard, 50, 50);
	}
	
	public SpawnHole create(int x, int y) {
		return SpawnHole.create(x, y, this);
	}
	
	public boolean add(SpawnHole spawnHole) {
		objectGrid.turnOnBlock(spawnHole.getX(), spawnHole.getY());
		return super.add(spawnHole);
	}
	
	public boolean remove(SpawnHole spawnHole) {
		objectGrid.turnOffBlock(spawnHole.getX(), spawnHole.getY());
		return super.remove(spawnHole);
	}

	public SpawnHole createFromString(String parameters, long id) {
		String[] parameter = parameters.split(":");
		int x = Integer.parseInt(parameter[0]);
		int y = Integer.parseInt(parameter[1]);
		SpawnHole s = new SpawnHole(x, y, this, id);
		add(s);
		return s;
	}

}
