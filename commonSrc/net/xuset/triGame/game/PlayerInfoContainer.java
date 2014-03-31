package net.xuset.triGame.game;

import java.util.ArrayList;

import net.xuset.objectIO.connections.Connection;
import net.xuset.objectIO.connections.ConnectionI;
import net.xuset.objectIO.markupMsg.MarkupMsg;
import net.xuset.objectIO.markupMsg.MsgAttribute;
import net.xuset.objectIO.netObject.NetFunction;
import net.xuset.objectIO.netObject.NetFunctionEvent;
import net.xuset.objectIO.netObject.NetObjUpdater;

public class PlayerInfoContainer {
	private static final String ID_ATTRIBUTE = "id";
	private static final String ACTION_ATTRIBUTE = "action";
	private static final String ACTION_CREATE = "cre";
	private static final String ACTION_REMOVE = "rem";
	
	private final NetObjUpdater objController;
	private final ArrayList<PlayerInfo> playerInfos = new ArrayList<PlayerInfo>();
	private final NetFunction funcCreate;
	private final PlayerInfo myPlayer;
	
	public PlayerInfoContainer(NetObjUpdater objController, long playerId) {
		this.objController = objController;
		funcCreate = new NetFunction(objController, "playerInfoCreate",
				new PlayerCreate());
		myPlayer = createPlayerOnNetwork(playerId);
		playerInfos.add(myPlayer);
	}
	
	public int getPlayerCount() {
		return playerInfos.size();
	}
	
	public PlayerInfo getPlayer(int index) {
		return playerInfos.get(index);
	}
	
	public PlayerInfo getOwnedPlayer() {
		return myPlayer;
	}
	
	public PlayerInfo getById(long id) {
		for (PlayerInfo pi : playerInfos)
			if (pi.getId() == id) return pi;
		return null;
	}
	
	public boolean removeOnNetwork(long id) {
		PlayerInfo pi = getById(id);
		if (pi != null) {
			MarkupMsg msg = craftRemoveMsg(id);
			funcCreate.sendCall(msg, Connection.BROADCAST_CONNECTION);
			return playerInfos.remove(pi);
			
		}
		return false;
	}
	
	private PlayerInfo createPlayerOnNetwork(long id) {
		PlayerInfo pInfo = new PlayerInfo(objController, id);
		MarkupMsg msg = craftCreateMsg(id);
		funcCreate.sendCall(msg, Connection.BROADCAST_CONNECTION);
		return pInfo;
	}
	
	private PlayerInfo localCreateAndStore(long id) {
		PlayerInfo pInfo = new PlayerInfo(objController, id);
		playerInfos.add(pInfo);
		return pInfo;
	}
	
	private boolean localRemove(long id) {
		PlayerInfo pInfo = getById(id);
		return playerInfos.remove(pInfo);
	}
	
	private MarkupMsg craftCreateMsg(long id) {
		MarkupMsg msg = new MarkupMsg();
		msg.addAttribute(ID_ATTRIBUTE, id);
		msg.addAttribute(ACTION_ATTRIBUTE, ACTION_CREATE);
		return msg;
	}
	
	private MarkupMsg craftRemoveMsg(long id) {
		MarkupMsg msg = new MarkupMsg();
		msg.addAttribute(ID_ATTRIBUTE, id);
		msg.addAttribute(ACTION_ATTRIBUTE, ACTION_REMOVE);
		return msg;
	}
	
	private class PlayerCreate implements NetFunctionEvent {

		@Override
		public MarkupMsg calledFunc(MarkupMsg args, ConnectionI c) {
			MsgAttribute actionAttrib = args.getAttribute(ACTION_ATTRIBUTE);
			MsgAttribute idAttrib = args.getAttribute(ID_ATTRIBUTE);
			long id = idAttrib.getLong();
			
			if (actionAttrib == null || idAttrib == null)
				return null;
			
			if (actionAttrib.getValue().equals(ACTION_CREATE)) {
				localCreateAndStore(id);
			} else if (actionAttrib.getValue().equals(ACTION_REMOVE)){
				localRemove(id);
			}
			return null;
		}

		@Override
		public void returnedFunc(MarkupMsg args, ConnectionI c) {
			//Do nothing
		}
		
	}
}
