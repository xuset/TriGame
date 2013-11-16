package tSquare.game.entity;


public class EntityManager extends Manager<Entity>{
	public static final String HASHMAP_KEY = "entity";
	
	public StandardCreator<Entity> creator;
	
	public EntityManager(ManagerController controller) {
		super(controller, HASHMAP_KEY);
		creator = new StandardCreator<Entity>("entity", controller.creator,
				new StandardCreator.IFace<Entity>() {
					@Override
					public Entity create(String spriteId, double x, double y, EntityKey key) {
						return new Entity(spriteId, x, y, key);
					}
				});
	}
	
	public Entity create(String spriteId, int x, int y) {
		return creator.create(spriteId, x, y, this);
	}
}
