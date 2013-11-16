package tSquare.game.entity;

import objectIO.markupMsg.MarkupMsg;

public class MsgCreator<T extends Entity> extends Creator<T> {
	
	public static interface IFace<T> { T create(MarkupMsg msg, EntityKey key); }
	
	private final IFace<T> func;

	public MsgCreator(String classId, CreationHandler handler, IFace<T> func) {
		super(classId, handler);
		this.func = func;
	}

	@Override
	protected T parseMsg(MarkupMsg msg, EntityKey key) {
		return func.create(msg, key);
	}
	
	public T create(MarkupMsg msg, Manager<T> manager) {
		EntityKey key = getNewKey(manager);
		networkCreate(key, msg);
		T t = func.create(msg, key);
		localCreate(t, manager);
		return t;
	}
	
	public T create(MarkupMsg msg) {
		return create(msg, null);
	}

}
