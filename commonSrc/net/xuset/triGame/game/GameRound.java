package net.xuset.triGame.game;

import net.xuset.objectIO.netObj.NetClass;
import net.xuset.objectIO.netObj.NetVar;
import net.xuset.objectIO.netObj.NetVarListener;
import net.xuset.tSquare.game.GameIntegratable;
import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.util.Observer;


public abstract class GameRound implements GameIntegratable{
	private static final long totalNumberDrawTime = 3000;
	
	protected final PlayerInfoContainer playerContainer;
	private final NetVar.nInt roundNumber;
	private final NetVar.nBool roundOnGoing;
	private long timeNumberDrawStarted = 0;
	
	public final Observer<Integer> onNewRound = new Observer<Integer>();
	
	protected abstract int getZombiesPerRound();
	protected abstract int getZombieSpawnDelta();
	
	public int getRoundNumber() { return roundNumber.get(); }
	public boolean isRoundOnGoing() { return roundOnGoing.get(); }
	
	public GameRound(NetClass objController, PlayerInfoContainer playerContainer) {
		this.playerContainer = playerContainer;
		roundNumber = new NetVar.nInt("roundNumber", 0);
		roundOnGoing = new NetVar.nBool("roundOngoing", false);
		objController.addObj(roundNumber);
		objController.addObj(roundOnGoing);
		roundNumber.setListener(new RoundChangeEvent());
	}
	
	protected void handleRoundNotOnGoing() {
		
	}
	
	protected void handleRoundOnGoing() {
		
	}
	
	public final void setRound(int round) {
		roundNumber.set(round);
		roundOnGoing.set(true);
		onRoundStart();
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
	
	protected void setRoundEnded() {
		roundOnGoing.set(false);
	}
	
	protected void onRoundStart() {
		timeNumberDrawStarted = System.currentTimeMillis();
		onNewRound.notifyWatchers(roundNumber.get());
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
	
	private class RoundChangeEvent implements NetVarListener<Integer> {
		@Override
		public void onVarChange(Integer newValue) {
			onRoundStart();
			
		}
	}
}
