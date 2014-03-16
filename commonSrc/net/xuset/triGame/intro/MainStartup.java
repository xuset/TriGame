package net.xuset.triGame.intro;

import net.xuset.tSquare.files.IFileFactory;
import net.xuset.tSquare.system.IDrawBoard;
import net.xuset.tSquare.system.input.InputHolder;
import net.xuset.triGame.game.TriGame;
import net.xuset.triGame.settings.Settings;

public class MainStartup {
	private final IDrawBoard drawBoard;
	private final IFileFactory fileFactory;
	private final InputHolder inputHolder;
	private final Settings settings;
	private final IpGetterIFace ipGetter;
	private final IUpdateChecker updateChecker;
	
	public MainStartup(IDrawBoard drawBoard, IFileFactory fileFactory,
			Settings settings, IpGetterIFace ipGetter, IUpdateChecker updateChecker) {
		
		this.drawBoard = drawBoard;
		this.fileFactory = fileFactory;
		this.settings = settings;
		this.ipGetter = ipGetter;
		this.inputHolder = drawBoard.createInputListener();
		this.updateChecker = updateChecker;
		
		while (true) {
			resetInputHolder();
			TriGame game = createGame();
			game.startGame();
			game.shutdown();
		}
	}
	
	private void resetInputHolder() {
		inputHolder.getMouse().clearListeners();
		inputHolder.getKeyboard().clearListeners();
		inputHolder.clearAllEvents();
	}
	
	private TriGame createGame() {
		GameIntro intro = new GameIntro(drawBoard, fileFactory,
				inputHolder, settings, ipGetter, updateChecker);
		
		return intro.createGame();
	}
}
