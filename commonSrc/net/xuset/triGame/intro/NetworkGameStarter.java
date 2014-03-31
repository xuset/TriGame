package net.xuset.triGame.intro;

import net.xuset.objectIO.connections.Connection;
import net.xuset.objectIO.connections.ConnectionI;
import net.xuset.objectIO.markupMsg.MarkupMsg;
import net.xuset.objectIO.netObject.NetFunction;
import net.xuset.objectIO.netObject.NetFunctionEvent;
import net.xuset.objectIO.netObject.StandardObjUpdater;
import net.xuset.triGame.game.GameMode.GameType;

class NetworkGameStarter {
	private final NetFunction startFunc;
	private final StandardObjUpdater objController;
	private GameType gameType = null;
	
	public NetworkGameStarter(StandardObjUpdater objController) {
		this.objController = objController;
		startFunc = new NetFunction(objController, "NetworkGameStarter",
				new StartFuncEvent());
	}
	
	public boolean hasGameStarted() {
		objController.distributeAllUpdates();
		return gameType != null;
	}
	
	public GameType getGameType() { return gameType; }
	
	public void startGame(GameType gameType) {
		this.gameType = gameType;
		MarkupMsg msg = new MarkupMsg();
		msg.setContent(gameType.name());
		startFunc.sendCall(msg, Connection.BROADCAST_CONNECTION);
	}

	
	private class StartFuncEvent implements NetFunctionEvent {
		@Override
		public MarkupMsg calledFunc(MarkupMsg args, ConnectionI c) {
			gameType = GameType.valueOf(args.getContent());
			objController.setRunning(false);
			return null;
		}

		@Override
		public void returnedFunc(MarkupMsg args, ConnectionI c) {
			//Do nothing
		}
	}
}
