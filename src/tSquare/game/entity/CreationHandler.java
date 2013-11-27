package tSquare.game.entity;

import java.util.HashMap;

import objectIO.connections.Connection;
import objectIO.markupMsg.MarkupMsg;
import objectIO.markupMsg.MsgAttribute;
import objectIO.netObject.NetFunction;
import objectIO.netObject.NetFunctionEvent;
import objectIO.netObject.ObjController;
import tSquare.util.HashMapKeyCollision;

public final class CreationHandler {
	public final HashMap<String, Creator<?>> creators = new HashMap<String, Creator<?>>();
	private final ManagerController managerController;
	private final NetFunction createFunc;
	private final NetFunction removeFunc;
	
	final ObjController objController;
	
	public CreationHandler(ObjController objController, ManagerController managerController) {
		this.objController = objController;
		this.managerController = managerController;
		createFunc = new NetFunction(objController, "createEntity", createEvent);
		removeFunc = new NetFunction(objController, "removeEntity", removeEvent);
	}
	
	void activateCreator(String classId, Creator<?> c) {
		if (creators.containsKey(classId))
			throw new HashMapKeyCollision(classId + " has already been used");
		creators.put(classId, c);
	}
	
	void createOnNetwork(EntityKey key, MarkupMsg args, Creator<?> c) {
		String managerKey = (key.manager == null) ?	"null" : key.manager.getHashMapKey();
		MarkupMsg msg = new MarkupMsg();
		msg.addAttribute(new MsgAttribute("id").set(key.id));
		msg.addAttribute(new MsgAttribute("creator").set(c.classId));
		msg.addAttribute(new MsgAttribute("manager").set(managerKey));
		msg.addAttribute(new MsgAttribute("allowUpdates").set(key.allowUpdates));
		msg.child.add(args);
		createFunc.sendCall(msg, Connection.BROADCAST_CONNECTION);
	}
	
	void removeOnNetwork(Entity e, Manager<?> m) {
		MarkupMsg msg = new MarkupMsg();
		msg.addAttribute(new MsgAttribute("manager").set(m.getHashMapKey()));
		msg.addAttribute(new MsgAttribute("id").set(e.id));
		removeFunc.sendCall(msg, Connection.BROADCAST_CONNECTION);
	}
	
	private NetFunctionEvent createEvent = new NetFunctionEvent() {
		public MarkupMsg calledFunc(MarkupMsg msg, Connection c) {
			
			String creatorKey = msg.getAttribute("creator").getString();
			Creator<?> creator = creators.get(creatorKey);

			EntityKey key = new EntityKey();
			String managerKey = msg.getAttribute("manager").getString();
			
			key.id = msg.getAttribute("id").getLong();
			key.manager = managerController.getManager(managerKey);
			key.objController = objController;
			key.allowUpdates = msg.getAttribute("allowUpdates").getBool();
			key.owned = false;
			
			creator.createFromMsg(msg.child.get(0), key);
			return null;
		}
		
		public void returnedFunc(MarkupMsg msg, Connection c) {

		}
	};
	
	private NetFunctionEvent removeEvent = new NetFunctionEvent() {
		public MarkupMsg calledFunc(MarkupMsg msg, Connection c) {
			long entityId = msg.getAttribute("id").getLong();
			String managerKey = msg.getAttribute("manager").getString();
			Manager<?> m = managerController.getManager(managerKey);
			Entity e = m.getById(entityId);
			if (e != null)
				e.remove();
			return null;
		}
		
		public void returnedFunc(MarkupMsg msg, Connection c) {
			
		}
	};
}
