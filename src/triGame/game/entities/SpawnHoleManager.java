package triGame.game.entities;

import tSquare.game.entity.Entity;
import tSquare.game.entity.EntityKey;
import tSquare.game.entity.LocationCreator;
import tSquare.game.entity.Manager;
import tSquare.game.entity.ManagerController;
import tSquare.paths.ObjectGrid;
import triGame.game.Params;

public class SpawnHoleManager extends Manager<SpawnHole>{
	public static final String HASH_MAP_KEY = "spawnHole";
	
	public final ObjectGrid objectGrid;
	public SpawnHole[] initialSpawnHoles;
	public final LocationCreator<SpawnHole> creator;
	
	public SpawnHoleManager(ManagerController controller) {
		super(controller, HASH_MAP_KEY);
		objectGrid = new ObjectGrid(Params.GAME_WIDTH, Params.GAME_HEIGHT, Params.BLOCK_SIZE, Params.BLOCK_SIZE);
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
