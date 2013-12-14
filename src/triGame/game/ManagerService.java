package triGame.game;

import tSquare.game.entity.ManagerController;
import tSquare.game.particles.ParticleController;
import tSquare.system.PeripheralInput;
import tSquare.util.PlaceHolder;
import triGame.game.entities.PersonManager;
import triGame.game.entities.PointWellManager;
import triGame.game.entities.SpawnHoleManager;
import triGame.game.entities.buildings.BuildingManager;
import triGame.game.entities.dropPacks.DropPackManager;
import triGame.game.entities.projectiles.ProjectileManager;
import triGame.game.entities.zombies.ZombieManager;
import triGame.game.safeArea.SafeAreaBoard;
import triGame.game.shopping.ShopManager;

public final class ManagerService {
	public final BuildingManager building;
	public final PersonManager person;
	public final ZombieManager zombie;
	public final PointWellManager pointWell;
	public final SpawnHoleManager spawnHole;
	public final ProjectileManager projectile;
	public final DropPackManager dropPack;
	
	/*
	 * NOTES:
	 * 
	 * zombie (ZombieManager) must be created AFTER wall (WallManager).
	 * the path finder alg requires wall's objectGrid in order to initialize itself
	 */
	
	ManagerService(ManagerController managerController, SafeAreaBoard safeBoard,
			PeripheralInput.Keyboard keyboard, ShopManager shop,
			ParticleController particleController, boolean isServer,
			PlaceHolder<RoundHandler> phRoundHandler, long ownerId) {

		spawnHole = new SpawnHoleManager(managerController);
		dropPack = new DropPackManager(managerController, shop);
		pointWell = new PointWellManager(managerController, particleController);
		projectile = new ProjectileManager(managerController, this);
		person = new PersonManager(managerController, this, safeBoard, keyboard, ownerId, isServer);
		building = new BuildingManager(managerController, this, safeBoard, phRoundHandler, shop, particleController);
		zombie = new ZombieManager(managerController, this, building, isServer, shop, particleController);
	}
}
