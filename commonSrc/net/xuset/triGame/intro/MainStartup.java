package net.xuset.triGame.intro;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.xuset.tSquare.files.IFileFactory;
import net.xuset.tSquare.system.IDrawBoard;
import net.xuset.tSquare.system.input.InputHolder;
import net.xuset.triGame.Params;
import net.xuset.triGame.game.TriGame;
import net.xuset.triGame.game.ui.IBrowserOpener;
import net.xuset.triGame.settings.Settings;

public class MainStartup {
	private final IDrawBoard drawBoard;
	private final IFileFactory fileFactory;
	private final InputHolder inputHolder;
	private final Settings settings;
	private final IpGetterIFace ipGetter;
	private final IBrowserOpener browserOpener;
	private final UpdateChecker updateChecker = new UpdateChecker();
	
	private volatile Thread thread;
	private volatile TriGame game = null;
	private volatile GameIntro intro = null;
	private volatile boolean exitGame = false;
	
	public MainStartup(IDrawBoard drawBoard, IFileFactory fileFactory,
			Settings settings, IpGetterIFace ipGetter, IBrowserOpener browserOpener) {
		
		this.drawBoard = drawBoard;
		this.fileFactory = fileFactory;
		this.settings = settings;
		this.ipGetter = ipGetter;
		this.browserOpener = browserOpener;
		this.inputHolder = drawBoard.createInputListener();
		
		setLogLevel(Params.LOG_LEVEL);
		thread = new Looper();
	}
	
	public boolean isGameOnGoing() {
		return game != null;
	}
	
	public boolean isAlive() {
		return thread.isAlive();
	}
	
	public synchronized void suspendGame() {
		exitGame = true;
		closeGame();
		closeIntro();
		joinThread();
	}
	
	public synchronized void resumeGame() {
		if (!thread.isAlive())
			thread = new Looper();
	}
	
	private void closeGame() {
		TriGame tGame = game;
		if (tGame != null) {
			if (tGame.isMultiplayer()) {
				tGame.shutdown();
				game = null;
			} else {
				game.stopGame();
			}
		}
	}
	
	private void closeIntro() {
		GameIntro gIntro = intro;
		if (gIntro != null) {
			gIntro.exitIntro();
			intro = null;
		}
	}
	
	private void joinThread() {
		try {
			thread.join(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
			if (thread.isAlive())
				thread.interrupt();
		}
	}
	
	private void determineAndLoop() {
		if (Params.DEBUG_MODE)
			runLoop();
		else
			catchAllAndLoop();
	}
	
	private void catchAllAndLoop() {
		try {
			runLoop();
		} catch (Exception ex) {
			System.err.println("Encountered error. Game has ended");
			ex.printStackTrace();
			resetInputHolder();
			new ExceptionViewer(drawBoard, inputHolder.getMouse(), ex);
		}
	}
	
	private void runLoop() {
		while (!exitGame) {
			if (game == null) {
				resetInputHolder();
				game = createGame();
				if (game == null)
					return;
			}
			
			game.startGame();
			if (!exitGame) {
				game.shutdown();
				game = null;
				resetInputHolder();
			}
		}
	}
	
	private void resetInputHolder() {
		inputHolder.getMouse().clearListeners();
		inputHolder.getKeyboard().clearListeners();
	}
	
	private TriGame createGame() {
		intro = new GameIntro(drawBoard, fileFactory,
				inputHolder, settings, ipGetter, updateChecker, browserOpener);
		
		TriGame game = intro.createGame();
		intro = null;//gc intro
		return game;
	}
	
	private void setLogLevel(Level level) {
		Logger topLogger = java.util.logging.Logger.getLogger("");
	    for (Handler handler : topLogger.getHandlers()) {
	        if (handler instanceof ConsoleHandler) {
	        	handler.setLevel(level);
	        }
	    }
	}
	
	private class Looper extends Thread{
		Looper() {
			exitGame = false;
			setName("GameLoop");
			start();
		}
		
		@Override
		public void run() {
			determineAndLoop();
		}
	}
}
