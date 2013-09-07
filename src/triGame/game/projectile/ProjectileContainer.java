package triGame.game.projectile;


import tSquare.game.GameBoard;
import tSquare.game.GameIntegratable;
import tSquare.util.SafeContainer;
import triGame.game.TriGame;
import triGame.game.entities.building.BuildingManager;
import triGame.game.entities.wall.WallManager;
import triGame.game.entities.zombies.ZombieManager;
import triGame.game.shopping.ShopManager;

public class ProjectileContainer extends SafeContainer<Projectile> implements GameIntegratable{
	private TriGame game;
	private ProjectileCreator creator;
	
	public GameBoard gameBoard;
	public BuildingManager buildingManager;
	public ZombieManager zombieManager;
	public WallManager wallManager;
	
	public ShopManager getShop() { return game.shop; }
	public ProjectileCreator getCreator() { return creator; }
	public int getDelta() { return game.getDelta(); }
	public long getUserId() { return game.getUserId(); }
	public long getUniqueId() { return game.getIdGenerator().getId(); }
	
	public ProjectileContainer(TriGame game, GameBoard gameBoard) {
		this.game = game;
		this.gameBoard = gameBoard;
		this.buildingManager = game.buildingManager;
		this.wallManager = game.wallManager;
		this.zombieManager = game.zombieManager;
		creator = new ProjectileCreator(this, game.getNetwork().getObjController());
	}
	
	public void performLogic() {
		for (Projectile p : list) {
			p.performLogic();
		}
	}
	
	public void draw() {
		for (Projectile p : list) {
			p.draw();
		}
	}
}
