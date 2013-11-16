package tSquare.game.entity;

import objectIO.markupMsg.MarkupMsg;
import objectIO.markupMsg.MsgAttribute;

public class LocationCreator<T extends Entity> extends Creator<T> {
	
	public static interface IFace<T extends Entity> { T create(double x, double y, EntityKey key); }
	
	private final IFace<? extends T> func;

	public LocationCreator(String classId, CreationHandler handler, IFace<? extends T> func) {
		super(classId, handler);
		this.func = func;
	}
	
	public T create(double x, double y, Manager<T> manager) {
		EntityKey key = getNewKey(manager);
		MarkupMsg msg = new MarkupMsg();
		msg.addAttribute(new MsgAttribute("x").set(x));
		msg.addAttribute(new MsgAttribute("y").set(y));
		networkCreate(key, msg);
		T e = func.create(x, y, key);
		localCreate(e, manager);
		return e;
	}
	
	public T create(double x, double y) {
		return create(x, y, null);
	}

	@Override
	protected T parseMsg(MarkupMsg msg, EntityKey key) {
		double x = msg.getAttribute("x").getDouble();
		double y = msg.getAttribute("y").getDouble();
		T e = func.create(x, y, key);
		return e;
	}

}
