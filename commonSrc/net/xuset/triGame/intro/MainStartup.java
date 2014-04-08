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
	
	public MainStartup(IDrawBoard drawBoard, IFileFactory fileFactory,
			Settings settings, IpGetterIFace ipGetter, IBrowserOpener browserOpener) {
		
		this.drawBoard = drawBoard;
		this.fileFactory = fileFactory;
		this.settings = settings;
		this.ipGetter = ipGetter;
		this.browserOpener = browserOpener;
		this.inputHolder = drawBoard.createInputListener();
		
		setLogLevel(Params.LOG_LEVEL);
		
		try {
		
			while (true) {
				resetInputHolder();
				TriGame game = createGame();
				game.startGame();
				game.shutdown();
			}
		
		} catch (Exception ex) {
			System.err.println("Encountered error. Game has ended");
			ex.printStackTrace();
			resetInputHolder();
			new ExceptionViewer(drawBoard, inputHolder.getMouse(), ex);
		}
	}
	
	private void resetInputHolder() {
		inputHolder.getMouse().clearListeners();
		inputHolder.getKeyboard().clearListeners();
	}
	
	private TriGame createGame() {
		GameIntro intro = new GameIntro(drawBoard, fileFactory,
				inputHolder, settings, ipGetter, updateChecker, browserOpener);
		
		return intro.createGame();
	}
	
	private void setLogLevel(Level level) {
		Logger topLogger = java.util.logging.Logger.getLogger("");
	    for (Handler handler : topLogger.getHandlers()) {
	        if (handler instanceof ConsoleHandler) {
	        	handler.setLevel(level);
	        }
	    }
	}
}
