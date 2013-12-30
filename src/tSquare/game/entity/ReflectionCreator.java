package tSquare.game.entity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ReflectionCreator<T extends Entity> extends Creator<T> {
	private Constructor<T> constructor;

	public ReflectionCreator(CreationHandler handler, Class<T> classObj) {
		super(classObj.getSimpleName(), handler, null);
		try {
			constructor = classObj.getConstructor(EntityKey.class);
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
	}
	
	public T create() {
		return create(null);
	}
	
	public T create(Manager<T> manager) {
		T t = createInstance(null);
		if (t != null) {
			networkCreate(t, manager);
			localCreate(t, manager);
		}
		return t;
	}
	
	private T createInstance(EntityKey key) {
		try {
			return constructor.newInstance(key);
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected T createFromMsg(EntityKey key, Manager<?> manager) {
		T t = createInstance(key);
		if (t != null)
			localCreate(t, (Manager<T>) manager);
		return t;
	}

}
