package triGame.game.entities.building;

import tSquare.game.GameBoard;
import tSquare.game.Manager;
import tSquare.game.ManagerController;
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
	
	public Building createFromString(String parameters, long id) { //TODO use the given ID
		String[] parameter = parameters.split(":", 4);
		long ownerId = Long.parseLong(parameter[1]);
		long entityId = Long.parseLong(parameter[2]);
		Building b = null;
		if (parameter[0].equals(Tower.IDENTIFIER))
			b = Tower.createFromString(parameter[3], this, ownerId, entityId);
		if (parameter[0].equals(HeadQuarters.IDENTIFIER))
			b = HeadQuarters.createFromString(parameter[3], this, ownerId, entityId);
		if (parameter[0].equals(SmallTower.IDENTIFIER))
			b = SmallTower.createFromString(parameter[3], this, ownerId, entityId);
		if (b != null)
			add(b);
		return b;
	}

}
