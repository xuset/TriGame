package net.xuset.triGame.game;

import net.xuset.objectIO.connections.Connection;
import net.xuset.objectIO.netObject.NetVar;
import net.xuset.objectIO.netObject.ObjControllerI;
import net.xuset.objectIO.netObject.NetVar.OnChange;
import net.xuset.tSquare.game.GameIntegratable;
import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.util.Observer;


public abstract class GameRound implements GameIntegratable{
	private static final long totalNumberDrawTime = 3000;
	
	protected final PlayerInfoContainer playerContainer;
	protected final NetVar.nInt roundNumber;
	protected final NetVar.nBool roundOnGoing;
	private long timeNumberDrawStarted = 0;
	
	public final Observer<Integer> onNewRound = new Observer<Integer>();
	
	protected abstract int getZombiesPerRound();
	protected abstract int getZombieSpawnDelta();
	
	public int getRoundNumber() { return roundNumber.get(); }
	public boolean isRoundOnGoing() { return roundOnGoing.get(); }
	
	public GameRound(ObjControllerI objController, PlayerInfoContainer playerContainer) {
		this.playerContainer = playerContainer;
		roundNumber = new NetVar.nInt(0, "roundNumber", objController);
		roundOnGoing = new NetVar.nBool(false, "roundOngoing", objController);
		roundNumber.setEvent(true, new RoundChangeEvent());
	}
	
	protected void handleRoundNotOnGoing() {
		
	}
	
	protected void handleRoundOnGoing() {
		
	}
	
	public final void setRound(int round) {
		roundNumber.set(round);
		roundOnGoing.set(true);
	}

	@Override
	public void update(int frameDelta) {
		if (roundOnGoing.get())
			handleRoundOnGoing();
		else
			handleRoundNotOnGoing();
	}

	@Override
	public void draw(IGraphics g) {
		Draw.drawRoundNumber(g, roundNumber.get(),
				System.currentTimeMillis() - timeNumberDrawStarted, totalNumberDrawTime);
	}
	
	protected void onRoundStart() {
		
	}
	
	protected void resetAllPlayersRoundRequest() {
		for (int i = 0; i < playerContainer.getPlayerCount(); i++) {
			PlayerInfo pInfo = playerContainer.getPlayer(i);
			pInfo.resetNewRoundRequest();
		}
	}
	
	protected boolean areAllPlayersReadyForNewRound() {
		for (int i = 0; i < playerContainer.getPlayerCount(); i++) {
			PlayerInfo pInfo = playerContainer.getPlayer(i);
			if (!pInfo.isRequestingNewRound())
				return false;
		}
		return true;
	}
	
	protected boolean isReadyForNextRound() {
		return playerContainer.getOwnedPlayer().isRequestingNewRound();
	}
	
	protected void setReadyForNextRound() {
		playerContainer.getOwnedPlayer().requestNewRound();
	}
	
	private class RoundChangeEvent implements OnChange<Integer> {
		@Override
		public void onChange(NetVar<Integer> var, Connection c) {
			timeNumberDrawStarted = System.currentTimeMillis();
			onNewRound.notifyWatchers(roundNumber.get());
			onRoundStart();
		}
	}
}
