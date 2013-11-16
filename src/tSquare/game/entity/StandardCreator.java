package tSquare.game.entity;

import objectIO.markupMsg.MarkupMsg;
import objectIO.markupMsg.MsgAttribute;


public class StandardCreator<T extends Entity> extends Creator<T>{

	
	public static interface IFace<T extends Entity> {
		public T create(String spriteId, double x, double y, EntityKey key);
	}
	
	private final IFace<T> func;
	
	public StandardCreator(String classId, CreationHandler handler, IFace<T> func) {
		super(classId, handler);
		this.func = func;
	}
	
	@Override
	protected T parseMsg(MarkupMsg msg, EntityKey key) {
		String spriteId = msg.getAttribute("spriteId").getString();
		double x = msg.getAttribute("x").getDouble();
		double y = msg.getAttribute("y").getDouble();
		return func.create(spriteId, x, y, key);
	}
	
	public T create (String spriteId, double x, double y, Manager<T> manager) {
		EntityKey key = getNewKey(manager);
		MarkupMsg args = new MarkupMsg();
		args.addAttribute(new MsgAttribute("spriteId").set(spriteId));
		args.addAttribute(new MsgAttribute("x").set(x));
		args.addAttribute(new MsgAttribute("y").set(y));
		networkCreate(key, args);
		T e = func.create(spriteId, x, y, key);
		localCreate(e, manager);
		return e;
	}
	
	public T create(String spriteId, double x, double y) {
		return create(spriteId, x, y, null);
	}
}
