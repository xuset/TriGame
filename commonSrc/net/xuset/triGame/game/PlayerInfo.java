package net.xuset.triGame.game;

import net.xuset.objectIO.netObject.NetVar;
import net.xuset.objectIO.netObject.ObjControllerI;

public class PlayerInfo {
	private final long id;
	private final NetVar.nBool netRequestRound;
	
	public PlayerInfo(ObjControllerI objController, long id) {
		this.id = id;
		netRequestRound = new NetVar.nBool(false, "netRequestRound" + id, objController);
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
