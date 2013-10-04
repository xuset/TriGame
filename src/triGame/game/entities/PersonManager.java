package triGame.game.entities;

import objectIO.markupMsg.MarkupMsg;
import tSquare.game.GameBoard;
import tSquare.game.entity.Manager;
import tSquare.game.entity.ManagerController;
import tSquare.system.PeripheralInput;
import triGame.game.TriGame;
import triGame.game.entities.building.BuildingManager;
import triGame.game.entities.wall.WallManager;
import triGame.game.entities.zombies.ZombieManager;
import triGame.game.safeArea.SafeAreaBoard;
import triGame.game.shopping.ShopManager;

public class PersonManager extends Manager<Person>{
	public static final String HASH_MAP_KEY = "person";
	private TriGame game;
	
	public PeripheralInput.Keyboard getInput() { return game.getInput().keyboard; }
	public ShopManager getShop() { return game.shop; }
	public BuildingManager getBuildingManager() { return game.buildingManager; }
	public WallManager getWallManager() { return game.wallManager; }
	public ZombieManager getZombieManager() { return game.zombieManager; }
	public Person getPlayer() { return game.player; }
	public SafeAreaBoard getSafeAreaBoard() { return game.safeBoard; }
	
	public PersonManager(ManagerController controller, TriGame game, GameBoard gameBoard) {
		super(controller, gameBoard, HASH_MAP_KEY);
		this.game = game;
	}
	
	void setGameOver() {
		game.setGameOver();
	}
	
	public Person create(int x, int y) {
		Person p = Person.create(x, y, this);
		return p;
	}
	@Override
	public Person createFromMsg(MarkupMsg msg, long entityId) {
		int x = (int) msg.getAttribute("x").getDouble();
		int y = (int) msg.getAttribute("y").getDouble();
		Person p = new Person(x, y, this, entityId);
		add(p);
		return p;
	}
}
