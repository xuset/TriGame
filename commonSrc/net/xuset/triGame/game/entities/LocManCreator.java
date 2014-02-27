package net.xuset.triGame.game.entities;

import net.xuset.tSquare.game.entity.CreationHandler;
import net.xuset.tSquare.game.entity.Entity;
import net.xuset.tSquare.game.entity.LocationCreator;
import net.xuset.tSquare.game.entity.Manager;

public class  LocManCreator <T extends Entity> extends LocationCreator<T> {

	private final Manager<T> manager;
	public LocManCreator(String classId, CreationHandler handler,
			Manager<T> manager, LocationFunc<? extends T> func) {
		
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

	@SuppressWarnings("unused")
	public boolean isValidLocation(double x, double y) {
		return true;
	}
}
