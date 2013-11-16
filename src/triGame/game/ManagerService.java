package triGame.game;

import tSquare.game.entity.ManagerController;
import tSquare.game.particles.ParticleController;
import tSquare.system.PeripheralInput;
import tSquare.util.PlaceHolder;
import triGame.game.entities.PersonManager;
import triGame.game.entities.PointWellManager;
import triGame.game.entities.SpawnHoleManager;
import triGame.game.entities.buildings.BuildingManager;
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
	
	/*
	 * NOTES:
	 * 
	 * zombie (ZombieManager) must be created AFTER wall (WallManager).
	 * the path finder alg requires wall's objectGrid in order to initialize itself
	 */
	
	ManagerService(ManagerController managerController, SafeAreaBoard safeBoard,
			PeripheralInput.Keyboard keyboard, ShopManager shop,
			ParticleController particleController, boolean isServer,
			PlaceHolder<RoundHandler> phRoundHandler) {

		spawnHole = new SpawnHoleManager(managerController);
		pointWell = new PointWellManager(managerController, particleController);
		projectile = new ProjectileManager(managerController, shop, this);
		person = new PersonManager(managerController, this, safeBoard, keyboard);
		building = new BuildingManager(managerController, this, safeBoard, phRoundHandler, shop);
		zombie = new ZombieManager(managerController, this, building, isServer, particleController);
	}
}
