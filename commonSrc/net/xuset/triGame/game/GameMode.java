package net.xuset.triGame.game;

import net.xuset.objectIO.netObj.NetClass;
import net.xuset.tSquare.game.GameIntegratable;
import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.triGame.game.entities.Person;
import net.xuset.triGame.game.entities.zombies.ZombieHandler;
import net.xuset.triGame.game.entities.zombies.ZombieTargeter;
import net.xuset.triGame.game.shopping.ShopManager;
import net.xuset.triGame.game.survival.SurvivalGameMode;
import net.xuset.triGame.game.ui.UserInterface;
import net.xuset.triGame.game.ui.gameInput.IRoundInput;
import net.xuset.triGame.game.versus.VersusGameMode;


public abstract class GameMode implements GameIntegratable {
	public enum GameType { SURVIVAL, VERSUS }
	
	protected final IsGameOver isGameOver = new IsGameOver();
	
	public int getRoundNumber() { return getGameRound().getRoundNumber(); }
	public boolean isRoundGoing() { return getGameRound().isRoundOnGoing(); }
	public boolean isGameOver() { return isGameOver.value; }
	
	
	public static GameMode factoryCreator(GameType gameType, ShopManager shop,
			GameGrid gameGrid, NetClass objc, boolean isServer,
			IRoundInput roundInput, PlayerInfoContainer playerContainer,
			UserInterface ui) {
		
		switch (gameType) {
		case SURVIVAL:
			return new SurvivalGameMode(shop, isServer, gameGrid, objc, roundInput,
					playerContainer);
		case VERSUS:
			return new VersusGameMode(shop, isServer, objc, roundInput, playerContainer,
					ui);
		}
		return null;
	}
	
	@Override
	public void update(int frameDelta) {
		getSafeBoard().update(frameDelta);
		getZombieHandler().update(frameDelta);
		getGameRound().update(frameDelta);
	}
	
	@Override
	public void draw(IGraphics g) {
		getSafeBoard().draw(g);
		getZombieHandler().draw(g);
		getGameRound().draw(g);
	}
	
	protected void onGameStart() {
		
	}
	
	public abstract SafeBoard getSafeBoard();
	public abstract ZombieHandler getZombieHandler();
	public abstract ZombieTargeter getZombieTargeter();
	protected abstract void createMap(double wallGenCoefficient);
	protected abstract Person spawnInPlayer();
	protected abstract void setDependencies(ManagerService managers);
	protected abstract GameRound getGameRound();

	public class IsGameOver {
		public boolean value = false;
	}
	
}
