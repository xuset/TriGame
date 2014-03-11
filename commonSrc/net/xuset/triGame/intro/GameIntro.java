package net.xuset.triGame.intro;

import net.xuset.tSquare.files.IFileFactory;
import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.TsColor;
import net.xuset.tSquare.math.rect.IRectangleR;
import net.xuset.tSquare.system.IDrawBoard;
import net.xuset.tSquare.system.input.mouse.MouseAction;
import net.xuset.tSquare.system.input.mouse.TsMouseEvent;
import net.xuset.tSquare.ui.UiButton;
import net.xuset.tSquare.ui.UiController;
import net.xuset.tSquare.ui.UiForm;
import net.xuset.tSquare.ui.layout.UiBorderLayout;
import net.xuset.tSquare.ui.layout.UiQueueLayout;
import net.xuset.tSquare.util.Observer;
import net.xuset.triGame.game.GameInfo;
import net.xuset.triGame.game.TriGame;
import net.xuset.triGame.intro.host.IntroHost;
import net.xuset.triGame.intro.join.IntroJoin;
import net.xuset.triGame.intro.main.IntroMain;
import net.xuset.triGame.intro.settings.IntroSettings;
import net.xuset.triGame.intro.solo.IntroSolo;
import net.xuset.triGame.settings.Settings;

public class GameIntro implements IntroSwitcher{
	private final UiController ui;
	private final UiBorderLayout mainLayout;
	private final IFileFactory fileFactory;
	private final Settings settings;
	private final IDrawBoard drawBoard;
	private final UtilityForm utilityForm = new UtilityForm();
	private final IntroForm[] introForms = new IntroForm[GameIntroForms.values().length];
	private IntroForm selectedForm = null;
	private GameIntroForms newForm = null;
	
	private TriGame createdGame = null;
	
	public GameIntro(IDrawBoard drawBoard, IFileFactory fileFactory, Settings settings,
			IpGetterIFace ipGetter) {
		
		this.drawBoard = drawBoard;
		this.fileFactory = fileFactory;
		this.settings = settings;
		
		ui = new UiController(drawBoard.createInputListener().getMouse());
		UiForm mainForm = ui.getForm();
		mainLayout = new UiBorderLayout(mainForm);
		mainForm.setLayout(mainLayout);
		
		mainLayout.add(utilityForm, UiBorderLayout.BorderPosition.SOUTH);
		
		setupIntroForms(ipGetter);
		switchToForm(GameIntroForms.MAIN);
	}
	
	private void setupIntroForms(IpGetterIFace ipGetter) {
		IntroSwitcher iSwitcher = this;
		introForms[GameIntroForms.MAIN.ordinal()] = new IntroMain(iSwitcher);
		introForms[GameIntroForms.JOIN.ordinal()] = new IntroJoin(iSwitcher);
		introForms[GameIntroForms.SOLO.ordinal()] = new IntroSolo();
		introForms[GameIntroForms.HOST.ordinal()] = new IntroHost(ipGetter);
		introForms[GameIntroForms.SETTINGS.ordinal()] = new IntroSettings(settings);
	}
	
	public TriGame createGame() {
		loop();
		return createdGame;
	}
	
	private void tryToCreateGame() {
		GameInfo gInfo = selectedForm.getCreatedGameInfo();
		if (gInfo != null) {
			createdGame = new TriGame(gInfo, drawBoard, fileFactory, settings);
		}
	}
	
	private void loop() {
		final int targetFPS = 30;
		long timeStart = System.currentTimeMillis();
		while (createdGame == null) {
			IGraphics g = drawBoard.getGraphics();
			if (g == null)
				continue;
			
			checkAndSwitchForms();
			selectedForm.update();
			
			g.clear();
			g.setColor(TsColor.rgb(220, 220, 220));
			IRectangleR view = g.getView();
			g.fillRect(0, 0, (float) view.getWidth(), (float) view.getHeight());
			ui.setScale(settings.blockSize / 50.0f);
			ui.draw(g);
			drawBoard.flushScreen();
			tryToCreateGame();
			long wait = 1000 / targetFPS;
			long diff = wait - System.currentTimeMillis() + timeStart;
			if (diff > 0)
				try { Thread.sleep(diff); } catch(Exception ex) { }
			timeStart = System.currentTimeMillis();
			
		}
	}
	
	private void checkAndSwitchForms() {
		if (newForm != null) {
			if (selectedForm != null)
				selectedForm.onFocusLost();
			selectedForm = introForms[newForm.ordinal()];
			mainLayout.add(selectedForm.getForm(), UiBorderLayout.BorderPosition.CENTER);
			selectedForm.onFocusGained();
			utilityForm.setButtonOptions(newForm != GameIntroForms.MAIN,
					newForm != GameIntroForms.SETTINGS);
			newForm = null;
		}
	}

	@Override
	public void switchToForm(GameIntroForms newForm) {
		this.newForm = newForm;
	}
	
	private class UtilityForm extends UiForm {
		private final UiButton btnBack = new UiButton("<- back");
		private final UiButton btnSettings = new UiButton("Settings");
		
		public void setButtonOptions(boolean showBack, boolean showSettings) {
			getLayout().clearComponents();
			if (showBack)
				getLayout().add(btnBack);
			if (showSettings)
				getLayout().add(btnSettings);
		}
		
		UtilityForm() {
			UiQueueLayout layout = new UiQueueLayout(10, 5, this);
			setLayout(layout);
			
			btnBack.addMouseListener(new BtnBackAction());
			btnSettings.addMouseListener(new BtnSettingsAction());
			
			layout.add(btnBack);
			layout.add(btnSettings);
		}
		
		private class BtnBackAction implements Observer.Change<TsMouseEvent> {
			@Override
			public void observeChange(TsMouseEvent t) {
				if (t.action == MouseAction.PRESS)
					switchToForm(GameIntroForms.MAIN);
			}
		}
		
		private class BtnSettingsAction implements Observer.Change<TsMouseEvent> {
			@Override
			public void observeChange(TsMouseEvent t) {
				if (t.action == MouseAction.PRESS)
					switchToForm(GameIntroForms.SETTINGS);
			}
		}
	}

}