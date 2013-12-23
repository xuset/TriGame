package triGame.game;

import java.awt.Graphics2D;

import objectIO.netObject.ObjControllerI;
import tSquare.game.GameBoard.ViewRect;
import tSquare.game.GameIntegratable;
import tSquare.system.PeripheralInput;
import triGame.game.entities.Person;
import triGame.game.entities.zombies.ZombieHandler;
import triGame.game.entities.zombies.ZombieTargeter;
import triGame.game.shopping.ShopManager;
import triGame.game.survival.SurvivalGameMode;
import triGame.game.ui.UserInterface;
import triGame.game.versus.VersusGameMode;

public abstract class GameMode implements GameIntegratable {
	public enum GameType { SURVIVAL, VERSUS }
	
	protected final IsGameOver isGameOver = new IsGameOver();
	
	public int getRoundNumber() { return getGameRound().roundNumber.get(); }
	public boolean isRoundGoing() { return getGameRound().roundOnGoing.get(); }
	public boolean isGameOver() { return isGameOver.value; }
	
	
	public static GameMode factoryCreator(GameType gameType, ShopManager shop,
			ObjControllerI objc, boolean isServer, PeripheralInput.Keyboard keyboard) {
		
		switch (gameType) {
		case SURVIVAL:
			return new SurvivalGameMode(shop, isServer, objc, keyboard);
		case VERSUS:
			return new VersusGameMode(shop, isServer, objc, keyboard);
		}
		return null;
	}
	
	@Override
	public void performLogic(int frameDelta) {
		getSafeBoard().performLogic(frameDelta);
		getZombieHandler().performLogic(frameDelta);
		getGameRound().performLogic(frameDelta);
	}
	
	@Override
	public void draw(Graphics2D g, ViewRect rect) {
		getSafeBoard().draw(g, rect);
		getZombieHandler().draw(g, rect);
		getGameRound().draw(g, rect);
	}
	
	protected void onGameStart() {
		
	}
	
	public abstract SafeBoard getSafeBoard();
	public abstract ZombieHandler getZombieHandler();
	public abstract ZombieTargeter getZombieTargeter();
	protected abstract void createMap();
	protected abstract Person spawnInPlayer();
	protected abstract void setDependencies(ManagerService managers, UserInterface ui);
	protected abstract GameRound getGameRound();

	public class IsGameOver {
		public boolean value = false;
	}
	
}
