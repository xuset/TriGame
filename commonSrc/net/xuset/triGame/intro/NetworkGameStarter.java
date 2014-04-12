package net.xuset.triGame.intro;

import net.xuset.objectIO.connections.Connection;
import net.xuset.objectIO.connections.Hub;
import net.xuset.objectIO.connections.sockets.InetCon;
import net.xuset.objectIO.markupMsg.MarkupMsg;
import net.xuset.objectIO.netObj.NetClass;
import net.xuset.objectIO.netObj.NetFunc;
import net.xuset.objectIO.netObj.NetFuncListener;
import net.xuset.triGame.game.GameMode.GameType;

class NetworkGameStarter {
	private final Hub<? extends InetCon> hub;
	private final NetClass objController;
	private final NetFunc startFunc;
	
	private GameType gameType = null;
	private boolean distributeUpdates = true;
	
	public NetworkGameStarter(Hub<? extends InetCon> hub, NetClass objController) {
		this.hub = hub;
		this.objController = objController;
		startFunc = new NetFunc("NetworkGameStarter", new StartFuncEvent());
		objController.addObj(startFunc);
		
	}
	
	public boolean hasGameStarted() {
		sendUpdates();
		distributeReceivedUpdates();
		if (gameType != null)
			objController.removeObj(startFunc);
		return gameType != null;
	}
	
	public GameType getGameType() { return gameType; }
	
	public void startGame(GameType gameType) {
		this.gameType = gameType;
		MarkupMsg msg = new MarkupMsg();
		msg.setContent(gameType.name());
		startFunc.sendCall(msg);
		sendUpdates();
		removeAddedNetObjs();
	}
	
	private void removeAddedNetObjs() {
		objController.removeObj(startFunc);
	}
	
	private class StartFuncEvent implements NetFuncListener {
		@Override
		public MarkupMsg funcCalled(MarkupMsg args) {
			gameType = GameType.valueOf(args.getContent());
			distributeUpdates = false;
			return null;
		}

		@Override
		public void funcReturned(MarkupMsg args) {
			//Do nothing
		}
	}
	
	private void sendUpdates() {
		if (objController.hasUpdates()) {
			MarkupMsg msg = objController.serializeUpdates();
			hub.broadcastMsg(msg);
			for (int i = 0; i < hub.getConnectionCount(); i++)
				hub.getConnectionByIndex(i).flush();
		}
	}
	
	private void distributeReceivedUpdates() {
		for (int i = 0; distributeUpdates && i < hub.getConnectionCount(); i++) {
			Connection con = hub.getConnectionByIndex(i);
			while (distributeUpdates && con.isMsgAvailable()) {
				MarkupMsg msg = con.pollNextMsg();
				if (objController.getId().equals(msg.getName()))
					objController.deserializeMsg(msg);
			}
		}
	}
}
