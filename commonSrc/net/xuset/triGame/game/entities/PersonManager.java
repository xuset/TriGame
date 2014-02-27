package net.xuset.triGame.game.entities;

import net.xuset.tSquare.game.entity.EntityKey;
import net.xuset.tSquare.game.entity.LocationCreator;
import net.xuset.tSquare.game.entity.Manager;
import net.xuset.tSquare.game.entity.ManagerController;
import net.xuset.tSquare.game.entity.LocationCreator.LocationFunc;
import net.xuset.triGame.game.GameGrid;
import net.xuset.triGame.game.GameMode;
import net.xuset.triGame.game.ManagerService;
import net.xuset.triGame.game.ui.gameInput.IPlayerInput;


public class PersonManager extends Manager<Person>{
	public static final String HASH_MAP_KEY = "person";
	
	private final ManagerService managers;
	private final GameMode gameMode;
	private final IPlayerInput playerInput;
	private final long ownerId;
	private final boolean isServer;
	private final GameGrid gameGrid;
	private final TriangleSpriteCreator triangleCreator;
	
	public final LocationCreator<Person> creator;
	
	public PersonManager(ManagerController controller, ManagerService managers,
			GameMode gameMode, IPlayerInput playerInput,
			long ownerId, boolean isServer, GameGrid gameGrid,
			TriangleSpriteCreator triangleCreator) {
		
		super(controller, HASH_MAP_KEY);
		this.managers = managers;
		this.gameMode = gameMode;
		this.playerInput = playerInput;
		this.ownerId = ownerId;
		this.isServer = isServer;
		this.gameGrid = gameGrid;
		this.triangleCreator = triangleCreator;
		
		creator = new LocationCreator<Person>(HASH_MAP_KEY, controller.creator, new PersonCreate());
	}
	
	public Person getPlayer() {
		for (Person p : list) {
			if (p.owned())
				return p;
		}
		return null;
	}
	
	public Person create(double x, double y) {
		return creator.create(x, y, this);
	}
	
	public Person getByUserId(long userId) {
		for (Person p : list) {
			if (p.getOwnerId() == userId)
				return p;
		}
		return null;
	}
	
	private class PersonCreate implements LocationFunc<Person> {
		
		private Person create(double x, double y, EntityKey key) {
			return new Person(x, y, key, managers, gameMode.getSafeBoard(),
					playerInput, ownerId, isServer, gameGrid, triangleCreator);
		}
		
		@Override
		public Person create(double x, double y) {
			return create(x, y, null);
		}

		@Override
		public Person create(EntityKey key) {
			return create(0, 0, key);
		}
	}
}
