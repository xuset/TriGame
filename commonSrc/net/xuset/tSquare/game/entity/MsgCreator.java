package net.xuset.tSquare.game.entity;

import net.xuset.objectIO.markupMsg.MarkupMsg;

public class MsgCreator<T extends Entity> extends Creator<T> {
	
	public static interface MsgFunc<T extends Entity> extends CreateFunc<T> {
		T create(MarkupMsg msg);
	}
	
	private final MsgFunc<? extends T> func;

	public MsgCreator(String classId, CreationHandler handler, MsgFunc<? extends T> func) {
		super(classId, handler, func);
		this.func = func;
	}
	
	public T create(MarkupMsg msg, Manager<T> manager) {
		T t = func.create(msg);
		networkCreate(t, manager);
		localCreate(t, manager);
		return t;
	}
	
	public T create(MarkupMsg msg) {
		return create(msg, null);
	}

}
