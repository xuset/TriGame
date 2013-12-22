package triGame.game.entities;

import tSquare.game.entity.CreationHandler;
import tSquare.game.entity.Entity;
import tSquare.game.entity.LocationCreator;
import tSquare.game.entity.Manager;

public class  LocManCreator <T extends Entity> extends LocationCreator<T> {

	private final Manager<T> manager;
	public LocManCreator(String classId, CreationHandler handler,
			Manager<T> manager, LocationCreator.IFace<? extends T> func) {
		
		super(classId, handler, func);
		this.manager = manager;
	}
	
	@Override
	public T create(double x, double y) {
		return create(x, y, manager);
	}
	
	@Override
	public T create(double x, double y, Manager<T> manager) {
		if (isValidLocation(x, y))
			return super.create(x, y, manager);
		return null;
	}
	
	public T forceCreate(double x, double y, Manager<T> manager) {
		return super.create(x, y, manager);
	}

	public boolean isValidLocation(double x, double y) {
		return true;
	}
}
