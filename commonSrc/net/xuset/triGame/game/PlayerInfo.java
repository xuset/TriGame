package net.xuset.triGame.game;

import net.xuset.objectIO.netObj.NetClass;
import net.xuset.objectIO.netObj.NetVar;

public class PlayerInfo {
	private final long id;
	private final NetVar.nBool netRequestRound;
	
	public PlayerInfo(NetClass objController, long id) {
		this.id = id;
		netRequestRound = new NetVar.nBool("netRequestRound" + id, false);
		objController.addObj(netRequestRound);
	}
	
	public long getId() { return id; }
	
	public boolean isRequestingNewRound() {
		return netRequestRound.get();
	}
	
	public void resetNewRoundRequest() {
		netRequestRound.set(false);
	}
	
	public void requestNewRound() {
		netRequestRound.set(true);
	}

}
