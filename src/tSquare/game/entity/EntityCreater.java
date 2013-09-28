package tSquare.game.entity;

import java.util.HashMap;

import objectIO.connections.Connection;
import objectIO.markupMsg.MarkupMsg;
import objectIO.markupMsg.MsgAttribute;
import objectIO.netObject.NetFunction;
import objectIO.netObject.NetFunctionEvent;
import objectIO.netObject.NetObjectController;
import tSquare.game.Manager;

public class EntityCreater {
	private NetFunction createFunc;
	private NetFunction removeFunc;
	private HashMap<String, Manager<?>> managers;
	
	public EntityCreater(HashMap<String, Manager<?>> managers, NetObjectController controller) {
		this.managers = managers;
		createFunc = new NetFunction(controller, "create");
		createFunc.function = createEvent;
		removeFunc = new NetFunction(controller, "remove");
		removeFunc.function = removeEvent;
	}
	
	public void createOnNetwork(Entity e) {
		MarkupMsg msg = new MarkupMsg();
		msg.addAttribute(new MsgAttribute("id", String.valueOf(e.getId())));
		msg.addAttribute(new MsgAttribute("manager", e.manager.getHashMapKey()));
		msg.addAttribute(new MsgAttribute("update", String.valueOf(e.allowUpdates)));
		msg.content = e.createToString();
		createFunc.sendCall(msg, Connection.BROADCAST_CONNECTION);
	}
	
	private NetFunctionEvent createEvent = new NetFunctionEvent() {
		public MarkupMsg calledFunc(MarkupMsg msg, Connection c) {
			long entityId = Long.parseLong(msg.getAttribute("id").value);
			boolean allowUpdates = Boolean.parseBoolean(msg.getAttribute("update").value);
			Manager<?> m = managers.get(msg.getAttribute("manager").value);
			if (m != null) {
				Entity e = m.createFromString(msg.content, entityId);
				if (allowUpdates)
					e.createUpdateVars();
				e.createdOnNetwork = true;
				e.id = entityId;
				e.owned = false;
			}
			return null;
		}
		
		public void returnedFunc(MarkupMsg msg, Connection c) {

		}
	};
	
	public void removeOnNetwork(Entity e) {
		MarkupMsg msg = new MarkupMsg();
		msg.addAttribute(new MsgAttribute("manager", e.manager.getHashMapKey()));
		msg.addAttribute(new MsgAttribute("id", String.valueOf(e.id)));
		removeFunc.sendCall(msg, Connection.BROADCAST_CONNECTION);
	}
	
	private NetFunctionEvent removeEvent = new NetFunctionEvent() {
		public MarkupMsg calledFunc(MarkupMsg msg, Connection c) {
			long entityId = Long.parseLong(msg.getAttribute("id").value);
			Manager<?> m = managers.get(msg.getAttribute("manager").value);
			if (m != null) {
				Entity e = m.getById(entityId);
				if (e != null)
					e.remove();
			}
			return null;
		}
		
		public void returnedFunc(MarkupMsg msg, Connection c) {
			
		}
	};
}
