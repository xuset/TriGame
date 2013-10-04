package triGame.game.entities.wall;

import objectIO.markupMsg.MarkupMsg;
import tSquare.game.GameBoard;
import tSquare.game.entity.Manager;
import tSquare.game.entity.ManagerController;
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

	@Override
	public AbstractWall createFromMsg(MarkupMsg msg, long entityId) {
		int x = (int) msg.getAttribute("x").getDouble();
		int y = (int) msg.getAttribute("y").getDouble();
		String type = msg.getAttribute("type").getString();
		
		AbstractWall w = null;
		if (type.equals(Barrier.IDENTIFIER))
			w = new Barrier(x, y, this, entityId);
		if (type.equals(TrapDoor.IDENTIFIER))
			w = new TrapDoor(x, y, this, entityId);
		
		if (w != null)
			add(w);
		return w;
	}

}
