package net.xuset.tSquare.game.entity;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import net.xuset.tSquare.game.entity.CreationHandler;
import net.xuset.tSquare.game.entity.Creator;
import net.xuset.tSquare.game.entity.Entity;
import net.xuset.tSquare.game.entity.EntityKey;
import net.xuset.tSquare.game.entity.Manager;


public class ReflectionCreator<T extends Entity> extends Creator<T> {
	private Constructor<T> constructor;

	public ReflectionCreator(CreationHandler handler, Class<T> classObj) {
		super(classObj.getSimpleName(), handler, null);
		try {
			constructor = classObj.getConstructor(EntityKey.class);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
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
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch(IllegalAccessException e) {
			e.printStackTrace();
		} catch(IllegalArgumentException e) {
			e.printStackTrace();
		} catch(InvocationTargetException e) {
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