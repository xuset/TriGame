package triGame.game.entities;

import objectIO.markupMsg.MarkupMsg;
import tSquare.game.GameBoard;
import tSquare.game.entity.Manager;
import tSquare.game.entity.ManagerController;
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

	@Override
	public boolean add(SpawnHole spawnHole) {
		objectGrid.turnOnBlock(spawnHole.getX(), spawnHole.getY());
		return super.add(spawnHole);
	}
	
	@Override
	public boolean remove(SpawnHole spawnHole) {
		objectGrid.turnOffBlock(spawnHole.getX(), spawnHole.getY());
		return super.remove(spawnHole);
	}

	@Override
	public SpawnHole createFromMsg(MarkupMsg msg, long entityId) {
		int x = (int) msg.getAttribute("x").getDouble();
		int y = (int) msg.getAttribute("y").getDouble();
		SpawnHole s= new SpawnHole(x, y, this, entityId);
		add(s);
		return s;
	}

}
