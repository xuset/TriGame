package net.xuset.triGame.intro;

import net.xuset.tSquare.files.IFileFactory;
import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.TsColor;
import net.xuset.tSquare.imaging.TsFont;
import net.xuset.tSquare.imaging.TsTypeFace;
import net.xuset.tSquare.math.rect.IRectangleR;
import net.xuset.tSquare.system.IDrawBoard;
import net.xuset.tSquare.system.input.InputHolder;
import net.xuset.tSquare.ui.UiComponent;
import net.xuset.tSquare.ui.UiController;
import net.xuset.tSquare.ui.UiForm;
import net.xuset.tSquare.ui.UiLabel;
import net.xuset.tSquare.ui.layout.UiBorderLayout;
import net.xuset.triGame.Params;
import net.xuset.triGame.game.GameInfo;
import net.xuset.triGame.game.TriGame;
import net.xuset.triGame.game.ui.IBrowserOpener;
import net.xuset.triGame.settings.Settings;

class GameIntro implements IntroSwitcher{
	private final IFileFactory fileFactory;
	private final Settings settings;
	private final IDrawBoard drawBoard;
	private final InputHolder inputHolder;
	private final IBrowserOpener browserOpener;
	
	private final UiController ui;
	private final UiBorderLayout mainLayout;
	private final UtilityForm utilityForm;
	private final IntroForm[] introForms = new IntroForm[GameIntroForms.values().length];
	private final UiForm containerForm = new UiForm();
	
	private final IntroAnimator animator;
	private IntroForm selectedForm = null;
	private GameIntroForms newForm = null;
	private volatile boolean exitIntro = false;
	private TriGame createdGame = null;
	
	public GameIntro(IDrawBoard drawBoard, IFileFactory fileFactory,
			InputHolder inputHolder, Settings settings, IpGetterIFace ipGetter,
			UpdateChecker updateChecker, IBrowserOpener browserOpener) {
		
		this.drawBoard = drawBoard;
		this.fileFactory = fileFactory;
		this.settings = settings;
		this.inputHolder = inputHolder;
		this.browserOpener = browserOpener;
		
		utilityForm = new UtilityForm((IntroSwitcher) this, updateChecker, browserOpener);
		animator = new IntroAnimator(settings.defaultBlockSize);
		ui = new UiController(inputHolder.getMouse());
		containerForm.setOpaque(true);
		containerForm.setBackground(new TsColor(235, 235, 235));
		containerForm.getBorder().setVisibility(true);
		containerForm.getBorder().setColor(TsColor.gray);
		
		UiLabel lblTitle = new UiLabel(Params.GAME_NAME);
		lblTitle.setFont(new TsFont("Arial", 70, TsTypeFace.BOLD));
		lblTitle.setForeground(TsColor.darkGray);
		lblTitle.setShadowColor(new TsColor(200, 20, 20));
		lblTitle.setDrawShadow(true);
		
		UiForm mainForm = ui.getForm();
		mainLayout = new UiBorderLayout(mainForm);
		mainForm.setLayout(mainLayout);
		mainLayout.add(lblTitle, UiBorderLayout.BorderPosition.NORTH);
		mainLayout.add(containerForm, UiBorderLayout.BorderPosition.CENTER);
		mainLayout.add(utilityForm, UiBorderLayout.BorderPosition.SOUTH);
		
		setupIntroForms(ipGetter);
		switchToForm(GameIntroForms.MAIN);
	}
	
	private void setupIntroForms(IpGetterIFace ipGetter) {
		IntroSwitcher iSwitcher = this;
		IntroConnected introCon = new IntroConnected();
		introForms[GameIntroForms.CONNECTED.ordinal()] = introCon;
		introForms[GameIntroForms.MAIN.ordinal()] = new IntroMain(iSwitcher);
		introForms[GameIntroForms.JOIN.ordinal()] = new IntroJoin(iSwitcher, introCon);
		introForms[GameIntroForms.SOLO.ordinal()] = new IntroSolo();
		introForms[GameIntroForms.HOST.ordinal()] = new IntroHost(ipGetter);
		introForms[GameIntroForms.SETTINGS.ordinal()] = new IntroSettings(settings);
	}
	
	public void exitIntro() {
		exitIntro = true;
	}
	
	public TriGame createGame() {
		loop();
		return createdGame;
	}
	
	private void tryToCreateGame() {
		GameInfo gInfo = selectedForm.getCreatedGameInfo();
		if (gInfo != null) {
			createdGame = new TriGame(gInfo, drawBoard, fileFactory, inputHolder,
					settings, browserOpener);
		}
	}
	
	private void loop() {
		final int targetFPS = 60;
		long timeStart = System.currentTimeMillis();
		while (!exitIntro && createdGame == null) {
			IGraphics g = drawBoard.getGraphics();
			if (g == null) {
				try { Thread.sleep(10); } catch (InterruptedException ex) { }
				continue;
			}
			
			checkAndSwitchForms();
			selectedForm.update();
			
			g.clear();
			g.setColor(TsColor.rgb(220, 220, 220));
			IRectangleR view = g.getView();
			g.fillRect(0, 0, (float) view.getWidth(), (float) view.getHeight());
			ui.setScale(settings.uiZoom);
			animator.draw(g);
			ui.draw(g);
			drawBoard.flushScreen();
			tryToCreateGame();
			long wait = 1000 / targetFPS;
			long diff = wait - System.currentTimeMillis() + timeStart;
			if (diff > 0)
				try { Thread.sleep(diff); } catch(Exception ex) { }
			timeStart = System.currentTimeMillis();
			
		}
		
		ui.unwatchMouseEvents();
		if (createdGame == null && selectedForm != null)
			selectedForm.onFocusLost();
	}
	
	private void checkAndSwitchForms() {
		if (newForm != null) {
			if (selectedForm != null)
				selectedForm.onFocusLost();
			selectedForm = introForms[newForm.ordinal()];
			containerForm.getLayout().clearComponents();
			UiComponent frm = selectedForm.getForm();
			if (frm != null)
				containerForm.getLayout().add(frm);
			selectedForm.onFocusGained();
			utilityForm.setButtonOptions(newForm != GameIntroForms.MAIN,
					newForm == GameIntroForms.MAIN);
			newForm = null;
		}
	}

	@Override
	public void switchToForm(GameIntroForms newForm) {
		this.newForm = newForm;
	}

}
