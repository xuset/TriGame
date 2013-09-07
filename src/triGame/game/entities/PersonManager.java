package triGame.game.entities;

import tSquare.game.GameBoard;
import tSquare.game.Manager;
import tSquare.game.ManagerController;
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

	public Person createFromString(String parameters, long id) {
		String[] parameter = parameters.split(":");
		int x = Integer.parseInt(parameter[0]);
		int y = Integer.parseInt(parameter[1]);
		Person p = new Person(x, y, this, id);
		add(p);
		return p;
	}
	
	public Person create(int x, int y) {
		Person p = Person.create(x, y, this);
		return p;
	}
}
