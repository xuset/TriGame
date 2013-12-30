package tSquare.game.entity;


public class EntityManager extends Manager<Entity>{
	public static final String HASHMAP_KEY = "entity";
	
	private final StandardCreator<Entity> creator;
	
	public EntityManager(ManagerController controller) {
		super(controller, HASHMAP_KEY);
		creator = new StandardCreator<Entity>("entity", controller.creator, new EntityCreate());
	}
	
	private class EntityCreate implements StandardCreator.StandardFunc<Entity> {
		@Override
		public Entity create(String spriteId, double x, double y) {
			return new Entity(spriteId, x, y);
		}
		
		@Override
		public Entity create(EntityKey key) {
			return new Entity(key);
		}
	}
	
	public Entity create(String spriteId, double x, double y) {
		return creator.create(spriteId, x, y, this);
	}
}
