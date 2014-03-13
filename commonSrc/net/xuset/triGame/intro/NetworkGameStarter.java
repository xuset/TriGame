package net.xuset.triGame.intro;

import net.xuset.objectIO.connections.Connection;
import net.xuset.objectIO.markupMsg.MarkupMsg;
import net.xuset.objectIO.netObject.NetFunction;
import net.xuset.objectIO.netObject.NetFunctionEvent;
import net.xuset.objectIO.netObject.ObjController;
import net.xuset.triGame.game.GameMode.GameType;

public class NetworkGameStarter {
	private final NetFunction startFunc;
	private final ObjController objController;
	private GameType gameType = null;
	
	public NetworkGameStarter(ObjController objController) {
		this.objController = objController;
		startFunc = new NetFunction(objController, "NetworkGameStarter",
				new StartFuncEvent());
	}
	
	public boolean hasGameStarted() {
		objController.distributeRecievedUpdates();
		return gameType != null;
	}
	
	public GameType getGameType() { return gameType; }
	
	public void startGame(GameType gameType) {
		this.gameType = gameType;
		MarkupMsg msg = new MarkupMsg();
		msg.content = gameType.name();
		startFunc.sendCall(msg, Connection.BROADCAST_CONNECTION);
	}

	
	private class StartFuncEvent implements NetFunctionEvent {
		@Override
		public MarkupMsg calledFunc(MarkupMsg args, Connection c) {
			gameType = GameType.valueOf(args.content);
			objController.stopRunning();
			return null;
		}

		@Override
		public void returnedFunc(MarkupMsg args, Connection c) {
			//Do nothing
		}
	}
}
