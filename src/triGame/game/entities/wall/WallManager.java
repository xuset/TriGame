package triGame.game.entities.wall;

import tSquare.game.GameBoard;
import tSquare.game.Manager;
import tSquare.game.ManagerController;
import tSquare.paths.ObjectGrid;
import triGame.game.TriGame;
import triGame.game.entities.Person;

public class WallManager extends Manager<AbstractWall>{
	public static final String HASH_MAP_KEY = "wall";
	
	private TriGame game;
	
	public ObjectGrid objectGrid;
	public Person getPlayer() { return game.player; }

	public WallManager(ManagerController controller, TriGame game, GameBoard gameBoard) {
		super(controller, gameBoard, HASH_MAP_KEY);
		objectGrid = new ObjectGrid(gameBoard, TriGame.BLOCK_WIDTH, TriGame.BLOCK_HEIGHT);
		this.game = game;
	}

	public Barrier purchaseBarrier(int x, int y) {
		if(game.shop.purchase(Barrier.NEW_BARRIER)) {
			return Barrier.create(x, y, this);
		}
		return null;
	}
	
	public TrapDoor purchaseTrapDoor(int x, int y) {
		if (game.shop.purchase(TrapDoor.NEW_TRAP_DOOR)) {
			return TrapDoor.create(x, y, this);
		}
		return null;
	}

	public AbstractWall createFromString(String parameters, long id) {
		String[] split = parameters.split(":", 2);
		AbstractWall w = null;
		if (split[0].equals(Barrier.IDENTIFIER))
			w = Barrier.createFromString(split[1], this, id);
		if (split[0].equals(TrapDoor.IDENTIFIER))
			w = TrapDoor.createFromString(split[1], this, id);
		if (w != null)
			add(w);
		return w;
	}
	
	public void draw() {
		super.draw();
	}

}
