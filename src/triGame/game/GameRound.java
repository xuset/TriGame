package triGame.game;

import java.awt.Graphics2D;

import objectIO.connections.Connection;
import objectIO.netObject.NetVar;
import objectIO.netObject.NetVar.OnChange;
import objectIO.netObject.ObjControllerI;
import tSquare.game.GameBoard.ViewRect;
import tSquare.game.GameIntegratable;
import tSquare.util.Observer;

public abstract class GameRound implements GameIntegratable{
	private static final long totalNumberDrawTime = 3000;
	
	protected final NetVar.nInt roundNumber;
	protected final NetVar.nBool roundOnGoing;
	private long timeNumberDrawStarted = 0;
	
	public final Observer<Integer> onNewRound = new Observer<Integer>();
	
	protected abstract int getZombiesPerRound();
	protected abstract int getZombieSpawnDelta();
	
	public int getRoundNumber() { return roundNumber.get(); }
	public boolean isRoundOnGoing() { return roundOnGoing.get(); }
	
	public GameRound(ObjControllerI objController) {
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
	public void performLogic(int frameDelta) {
		if (roundOnGoing.get())
			handleRoundOnGoing();
		else
			handleRoundNotOnGoing();
	}

	@Override
	public void draw(Graphics2D g, ViewRect rect) {
		Draw.drawRoundNumber(g, rect, roundNumber.get(), System.currentTimeMillis() - timeNumberDrawStarted, totalNumberDrawTime);
	}
	
	protected void onRoundStart() {
		
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
