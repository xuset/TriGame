package triGame.game.entities.building;

import objectIO.markupMsg.MarkupMsg;
import tSquare.game.GameBoard;
import tSquare.game.entity.Manager;
import tSquare.game.entity.ManagerController;
import tSquare.paths.ObjectGrid;
import triGame.game.TriGame;
import triGame.game.entities.PersonManager;
import triGame.game.entities.zombies.ZombieManager;
import triGame.game.projectile.ProjectileContainer;
import triGame.game.safeArea.SafeAreaBoard;
import triGame.game.shopping.ShopManager;

public class BuildingManager extends Manager<Building> {
	public static final String HASH_MAP_KEY = "building";
	
	private TriGame game;
	
	public ObjectGrid objectGrid;
	public ShopManager getShopManager() { return game.shop; }
	public ProjectileContainer getProjectils() { return game.gunManager.getProjectiles(); }
	public PersonManager getPersonManager() { return game.personManager; }
	public SafeAreaBoard getSafeBoard() { return game.safeBoard; };
	
	public TriGame getGameInstance() { return game; }
	
	public void setGameOver() { game.setGameOver(); }

	public BuildingManager(ManagerController controller, TriGame game, GameBoard gameBoard, ZombieManager zombieManager) {
		super(controller, gameBoard, HASH_MAP_KEY);
		this.game = game;
		objectGrid = new ObjectGrid(gameBoard, TriGame.BLOCK_WIDTH, TriGame.BLOCK_HEIGHT);
	}
	
	public BuildingManager(String hashMapKey, BuildingManager manager) {
		super(hashMapKey, manager);
	}
	
	public Tower purchaseTower(int x, int y) {
		if(game.shop.purchase(Tower.NEW_TOWER)) {
			return Tower.create(x, y, this);
		}
		return null;
	}
	
	public SmallTower purchaseSmallTower(int x, int y) {
		if (game.shop.purchase(SmallTower.NEW_TOWER)) {
			return SmallTower.create(x, y, this);
		}
		return null;
	}
	
	@Override
	public Building createFromMsg(MarkupMsg msg, long entityId) {
		int x = (int) msg.getAttribute("x").getDouble();
		int y = (int) msg.getAttribute("y").getDouble();
		long ownerId = msg.getAttribute("owner").getLong();
		String type = msg.getAttribute("type").getString();
		
		Building b =  null;
		if (type.equals(Tower.IDENTIFIER))
			b = new Tower(x, y, this, ownerId, entityId);
		if (type.equals(HeadQuarters.IDENTIFIER))
			b = new HeadQuarters(x, y, this, ownerId, entityId);
		if (type.equals(SmallTower.IDENTIFIER))
			b = new SmallTower(x, y, this, ownerId, entityId);
		
		if (b != null)
			add(b);
		return b;
	}

}
