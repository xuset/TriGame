package triGame.game.projectile;

import objectIO.connections.Connection;
import objectIO.markupMsg.MarkupMsg;
import objectIO.netObject.NetFunction;
import objectIO.netObject.NetFunctionEvent;
import objectIO.netObject.ObjController;

public class ProjectileCreator {
	private NetFunction createFunc;
	private ProjectileContainer container;
	
	public ProjectileCreator(ProjectileContainer container, ObjController controller) {
		this.container = container;
		createFunc = new NetFunction(controller, "createPro");
		createFunc.function = createEvent;
	}
	
	public void createOnNetwork(Projectile p) {
		MarkupMsg msg = new MarkupMsg();
		msg.setAttribute("proj", p.getIdentifier());
		msg.content = p.createToString();
		createFunc.sendCall(msg, Connection.BROADCAST_CONNECTION);
	}
	
	private NetFunctionEvent createEvent = new NetFunctionEvent() {
		public MarkupMsg calledFunc(MarkupMsg msg, Connection c) {
			String projectile = msg.getAttribute("proj").value;
			if (projectile.equals(ProjectilePistol.IDENTIFIER))
				ProjectilePistol.createFromString(msg.content, container, c.getEndId());
			if (projectile.equals(ProjectileTower.IDENTIFIER))
				ProjectileTower.createFromString(msg.content, container, c.getEndId());
			if (projectile.equals(ProjectileShotgun.IDENTIFIER))
				ProjectileShotgun.createFromString(msg.content, container, c.getEndId());
			return null;
		}
		
		public void returnedFunc(MarkupMsg msg, Connection c) {

		}
	};
}
