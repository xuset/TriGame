package net.xuset.triGame.game;

import net.xuset.tSquare.game.entity.ManagerController;
import net.xuset.tSquare.game.particles.ParticleController;
import net.xuset.triGame.game.entities.PersonManager;
import net.xuset.triGame.game.entities.PointWellManager;
import net.xuset.triGame.game.entities.SpawnHoleManager;
import net.xuset.triGame.game.entities.TriangleSpriteCreator;
import net.xuset.triGame.game.entities.buildings.BuildingGetter;
import net.xuset.triGame.game.entities.buildings.BuildingManager;
import net.xuset.triGame.game.entities.dropPacks.DropPackManager;
import net.xuset.triGame.game.entities.projectiles.ProjectileManager;
import net.xuset.triGame.game.entities.zombies.ZombieManager;
import net.xuset.triGame.game.shopping.ShopManager;
import net.xuset.triGame.game.ui.gameInput.IPlayerInput;


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
	
	ManagerService(ManagerController managerController,
			IPlayerInput playerInput, ShopManager shop, GameGrid gameGrid,
			ParticleController particleController, boolean isServer,
			long ownerId, GameMode gameMode, TriangleSpriteCreator triangleCreator,
			BuildingGetter buildingGetter) {

		spawnHole = new SpawnHoleManager(managerController, gameGrid);
		dropPack = new DropPackManager(managerController, shop);
		pointWell = new PointWellManager(managerController, gameGrid, particleController);
		projectile = new ProjectileManager(managerController, this, gameGrid);
		person = new PersonManager(managerController, this, gameMode, playerInput,
				ownerId, isServer, gameGrid, triangleCreator);
		building = new BuildingManager(managerController, this, gameGrid, gameMode,
				shop, particleController, buildingGetter);
		zombie = new ZombieManager(managerController, this, gameMode, building, isServer,
				shop, particleController);
	}
}
