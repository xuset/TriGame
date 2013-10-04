package tSquare.game.entity;

import java.util.HashMap;

import objectIO.connections.Connection;
import objectIO.markupMsg.MarkupMsg;
import objectIO.markupMsg.MsgAttribute;
import objectIO.netObject.NetFunction;
import objectIO.netObject.NetFunctionEvent;
import objectIO.netObject.ObjController;

public class EntityCreater {
	private NetFunction createFunc;
	private NetFunction removeFunc;
	private HashMap<String, Manager<?>> managers;
	
	public EntityCreater(HashMap<String, Manager<?>> managers, ObjController controller) {
		this.managers = managers;
		createFunc = new NetFunction(controller, "create");
		createFunc.function = createEvent;
		removeFunc = new NetFunction(controller, "remove");
		removeFunc.function = removeEvent;
	}
	
	public void createOnNetwork(Entity e) {
		MarkupMsg msg = new MarkupMsg();
		msg.addAttribute(new MsgAttribute("id").set(e.id));
		msg.addAttribute(new MsgAttribute("manager").set(e.manager.getHashMapKey()));
		msg.child.add(e.createToMsg());
		createFunc.sendCall(msg, Connection.BROADCAST_CONNECTION);
	}
	
	private NetFunctionEvent createEvent = new NetFunctionEvent() {
		public MarkupMsg calledFunc(MarkupMsg msg, Connection c) {
			long entityId = msg.getAttribute("id").getLong();
			Manager<?> m = managers.get(msg.getAttribute("manager").getString());
			if (m != null) {
				Entity e = m.createFromMsg(msg.child.get(0), entityId);
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
		msg.addAttribute(new MsgAttribute("manager").set(e.manager.getHashMapKey()));
		msg.addAttribute(new MsgAttribute("id").set(e.id));
		removeFunc.sendCall(msg, Connection.BROADCAST_CONNECTION);
	}
	
	private NetFunctionEvent removeEvent = new NetFunctionEvent() {
		public MarkupMsg calledFunc(MarkupMsg msg, Connection c) {
			long entityId = msg.getAttribute("id").getLong();
			Manager<?> m = managers.get(msg.getAttribute("manager").getString());
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
