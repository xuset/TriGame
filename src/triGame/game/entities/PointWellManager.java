package triGame.game.entities;

import objectIO.markupMsg.MarkupMsg;
import tSquare.game.entity.Manager;
import tSquare.game.entity.ManagerController;
import tSquare.paths.ObjectGrid;
import triGame.game.TriGame;

public class PointWellManager extends Manager<PointWell> {
	public static final String HASH_MAP_KEY = "pointWell";
	
	private TriGame game;
	
	public ObjectGrid objectGrid;
	
	TriGame getGame() { return game; }

	public PointWellManager(ManagerController controller, TriGame game) {
		super(controller, game.getGameBoard(), HASH_MAP_KEY);
		objectGrid = new ObjectGrid(game.getGameBoard(), TriGame.BLOCK_WIDTH, TriGame.BLOCK_HEIGHT);
		this.game = game;
	}
	
	public PointWell create(int x, int y) {
		return PointWell.create(x, y, this);
	}
	
	@Override
	public void draw() {
		completeListModifications();
		super.draw();
	}

	@Override
	public PointWell createFromMsg(MarkupMsg msg, long entityId) {
		double x = msg.getAttribute("x").getDouble();
		double y = msg.getAttribute("y").getDouble();
		PointWell p = new PointWell(x, y, this, entityId);
		add(p);
		return p;
	}

}
