package triGame.game.entities;

import tSquare.game.entity.EntityKey;
import tSquare.game.entity.LocationCreator;
import tSquare.game.entity.Manager;
import tSquare.game.entity.ManagerController;
import tSquare.system.PeripheralInput;
import triGame.game.ManagerService;
import triGame.game.safeArea.SafeAreaBoard;

public class PersonManager extends Manager<Person>{
	public static final String HASH_MAP_KEY = "person";
	
	private final ManagerService managers;
	private final SafeAreaBoard safeBoard;
	private final PeripheralInput.Keyboard keyboard;
	private final long ownerId;
	
	public final LocationCreator<Person> creator;
	
	public PersonManager(ManagerController controller, ManagerService managers,
			SafeAreaBoard safeBoard, PeripheralInput.Keyboard keyboard, long ownerId) {
		
		super(controller, HASH_MAP_KEY);
		this.managers = managers;
		this.safeBoard = safeBoard;
		this.keyboard = keyboard;
		this.ownerId = ownerId;
		
		creator = new LocationCreator<Person>(HASH_MAP_KEY, controller.creator, 
				new LocationCreator.IFace<Person>() {
					@Override
					public Person create(double x, double y, EntityKey key) {
						return new Person(x, y, key, PersonManager.this.managers,
								PersonManager.this.safeBoard, PersonManager.this.keyboard,
								PersonManager.this.ownerId);
					}
				});
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
}
