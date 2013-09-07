package triGame.game.entities.wall;

import tSquare.game.entity.Entity;

public abstract class AbstractWall extends Entity{
	String identifier;
	
	protected WallManager manager;

	public String getIdentifier() {
		return identifier;
	}
	
	public AbstractWall(String spriteId, int x, int y, WallManager manager, long entityId, String identifier) {
		super(spriteId, x, y, manager, entityId);
		this.manager = manager;
		manager.objectGrid.turnOnRectangle(x, y, getWidth(), getHeight());
		this.identifier = identifier;
	}
	
	public double modifyHealth(double delta) {
		super.modifyHealth(delta);
		if (health <= 0) {
			remove();
		}
		return health;
	}
	
	public void remove() {
		super.remove();
		manager.objectGrid.turnOffRectangle(x, y, getWidth(), getHeight());
	}
	
	public String createToString() {
		return identifier + ":" + spriteId + ":" + (int) x + ":" + (int) y;
	}
}
