package net.xuset.tSquare.game.entity;

import java.util.HashMap;

import net.xuset.objectIO.connections.Connection;
import net.xuset.objectIO.connections.ConnectionI;
import net.xuset.objectIO.markupMsg.MarkupMsg;
import net.xuset.objectIO.netObject.NetFunction;
import net.xuset.objectIO.netObject.NetFunctionEvent;
import net.xuset.objectIO.netObject.NetObjUpdater;
import net.xuset.tSquare.util.HashMapKeyCollision;



public final class CreationHandler {
	public final HashMap<String, Creator<?>> creators = new HashMap<String, Creator<?>>();
	private final ManagerController managerController;
	private final NetFunction createFunc;
	private final NetFunction removeFunc;
	
	final NetObjUpdater objController;
	
	public CreationHandler(NetObjUpdater objController, ManagerController managerController) {
		this.objController = objController;
		this.managerController = managerController;
		createFunc = new NetFunction(objController, "createEntity", createEvent);
		removeFunc = new NetFunction(objController, "removeEntity", removeEvent);
	}
	
	void activateCreator(Creator<?> c) {
		if (creators.containsKey(c.classId))
			throw new HashMapKeyCollision(c.classId + " has already been used");
		creators.put(c.classId, c);
	}
	
	void createOnNetwork(Entity e, Creator<?> c, Manager<?> manager) {
		assert(e != null && c != null && manager != null);
		String managerKey = manager.getHashMapKey();
		MarkupMsg msg = new MarkupMsg();
		msg.addAttribute("id", e.id);
		msg.addAttribute("creator", c.classId);
		msg.addAttribute("manager", managerKey);
		msg.addAttribute("allowUpdates", e.isUpdatesAllowed());
		
		MarkupMsg initialValues = e.createToMsg();
		if (initialValues != null)
			msg.addNested(initialValues);
		
		createFunc.sendCall(msg, Connection.BROADCAST_CONNECTION);
	}
	
	void removeOnNetwork(Entity e, Manager<?> m) {
		MarkupMsg msg = new MarkupMsg();
		msg.addAttribute("manager", m.getHashMapKey());
		msg.addAttribute("id", e.id);
		removeFunc.sendCall(msg, Connection.BROADCAST_CONNECTION);
	}
	
	private NetFunctionEvent createEvent = new NetFunctionEvent() {
		@Override
		public MarkupMsg calledFunc(MarkupMsg msg, ConnectionI c) {
			String managerKey = msg.getAttribute("manager").getString();
			String creatorKey = msg.getAttribute("creator").getString();
			Creator<?> creator = creators.get(creatorKey);

			boolean allowUpdates = msg.getAttribute("allowUpdates").getBool();
			long id = msg.getAttribute("id").getLong();
			MarkupMsg initialValue = msg.getNestedMsgs().get(0);
			EntityKey key = EntityKey.createFromExisting(id, allowUpdates, initialValue);
			
			creator.createFromMsg(key, managerController.getManager(managerKey));
			return null;
		}
		
		@Override
		public void returnedFunc(MarkupMsg msg, ConnectionI c) {

		}
	};
	
	private NetFunctionEvent removeEvent = new NetFunctionEvent() {
		@Override
		public MarkupMsg calledFunc(MarkupMsg msg, ConnectionI c) {
			long entityId = msg.getAttribute("id").getLong();
			String managerKey = msg.getAttribute("manager").getString();
			Manager<?> m = managerController.getManager(managerKey);
			Entity e = m.getById(entityId);
			if (e != null)
				e.remove();
			return null;
		}
		
		@Override
		public void returnedFunc(MarkupMsg msg, ConnectionI c) {
			
		}
	};
}
