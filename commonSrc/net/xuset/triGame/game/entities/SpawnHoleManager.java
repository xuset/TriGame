package net.xuset.triGame.game.entities;

import net.xuset.tSquare.game.entity.Entity;
import net.xuset.tSquare.game.entity.EntityKey;
import net.xuset.tSquare.game.entity.LocationCreator;
import net.xuset.tSquare.game.entity.Manager;
import net.xuset.tSquare.game.entity.ManagerController;
import net.xuset.tSquare.paths.ObjectGrid;
import net.xuset.triGame.game.GameGrid;


public class SpawnHoleManager extends Manager<SpawnHole>{
	public static final String HASH_MAP_KEY = "spawnHole";
	
	public final ObjectGrid objectGrid;
	public SpawnHole[] initialSpawnHoles;
	public final LocationCreator<SpawnHole> creator;
	
	public SpawnHoleManager(ManagerController controller, GameGrid gameGrid) {
		super(controller, HASH_MAP_KEY);
		objectGrid = new ObjectGrid(gameGrid.getGridWidth(), gameGrid.getGridHeight());
		creator = new LocationCreator<SpawnHole>(HASH_MAP_KEY, controller.creator, new SpawnCreate());
	}
	
	public SpawnHole create(int x, int y) {
		SpawnHole sh = new SpawnHole(x, y);
		add(sh);
		creator.createOnNetwork(sh, this);
		return sh;
	}

	@Override
	protected void onAdd(SpawnHole spawnHole) {
		objectGrid.turnOnBlock(spawnHole.getX(), spawnHole.getY());
	}
	
	@Override
	protected void onRemove(Entity spawnHole) {
		objectGrid.turnOffBlock(spawnHole.getX(), spawnHole.getY());
	}
	
	private class SpawnCreate implements LocationCreator.LocationFunc<SpawnHole> {
		@Override
		public SpawnHole create(EntityKey key) {
			return new SpawnHole(key);
		}

		@Override
		public SpawnHole create(double x, double y) {
			return new SpawnHole(x, y);
		}
	}

}
