package net.xuset.triGame.game.ui;

import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.IImage;
import net.xuset.tSquare.imaging.TsColor;
import net.xuset.tSquare.imaging.TsFont;
import net.xuset.tSquare.imaging.TsTypeFace;
import net.xuset.tSquare.math.rect.IRectangleR;
import net.xuset.tSquare.system.input.mouse.MouseAction;
import net.xuset.tSquare.system.input.mouse.TsMouseEvent;
import net.xuset.tSquare.ui.Alignment;
import net.xuset.tSquare.ui.Axis;
import net.xuset.tSquare.ui.UiButton;
import net.xuset.tSquare.ui.UiComponent;
import net.xuset.tSquare.ui.UiForm;
import net.xuset.tSquare.ui.UiLabel;
import net.xuset.tSquare.ui.UiPopupController;
import net.xuset.tSquare.ui.layout.UiQueueLayout;
import net.xuset.tSquare.util.Observer.Change;
import net.xuset.triGame.settings.Settings;
import net.xuset.triGame.settings.UiSettingsForm;
import net.xuset.tSquare.imaging.Sprite;

public class PauseHandler {
	public static final String SPRITE_ID = "media/Settings.png";
	
	private final UiPopupController popupController;
	private final Settings settings;
	private final PauseForm pauseForm;
	private final PauseButton pauseButton;
	private final ScoreSubmitter scoreSubmitter;
	
	private boolean requestExitGame = false;
	private boolean isGameOver = false;
	private PauseState switchState = null; //null for dont switch
	private int playerCount, roundsSurvived;
	
	public boolean exitGameRequested() { return requestExitGame; }
	public boolean isPaused() { return popupController.contains(pauseForm); }
	
	public PauseHandler(UiPopupController popupController, Settings settings,
			IBrowserOpener browserOpener, ScoreSubmitter scoreSubmitter) {
		
		this.scoreSubmitter = scoreSubmitter;
		this.popupController = popupController;
		this.settings = settings;
		pauseForm = new PauseForm(browserOpener);
		pauseButton = new PauseButton();
		
		popupController.addPopup(pauseButton);
	}
	
	public void update() {
		if (switchState != null) {
			restructure(switchState);
			switchState = null;
		}
	}
	
	public void setGameOver(int roundsSurvived, int playerCount) {
		this.playerCount = playerCount;
		this.roundsSurvived = roundsSurvived;
		pauseForm.lblRoundsSurvived.setText("Survived " + roundsSurvived + " rounds");
		isGameOver = true;
		setPause(true, false, true);
	}
	
	public void setPause(boolean isPaused) {
		setPause(isGameOver ? true : isPaused, isPaused, isGameOver);
	}
	
	public void setPause(boolean isPaused, boolean showSettings, boolean showGameOver) {
		switchState = new PauseState(isPaused, showSettings, showGameOver);
	}
	
	private void restructure(PauseState state) {
		if (state.showPause) {
			pauseForm.setOptions(state.showSettings, state.showGameOver);
			if (!popupController.contains(pauseForm))
				popupController.addPopup(pauseForm);
		} else {
			popupController.removePopup(pauseForm);
		}
	}
	
	private class PauseState {
		final boolean showPause, showSettings, showGameOver;
		
		PauseState(boolean showPause, boolean showSettings, boolean showGameOver) {
			this.showPause = showPause;
			this.showSettings = showSettings;
			this.showGameOver = showGameOver;
		}
	}
	
	private class PauseForm extends UiForm {
		private final UiForm innerForm = new UiForm();
		private final UiComponent settingsComponent;
		private final BtnExit btnExit = new BtnExit();
		private final UiLabel lblGameOver = new UiLabel("GAME  OVER!");
		private final UiLabel lblRoundsSurvived = new UiLabel("");
		private final UiButton btnScores = new UiButton("Post to leader board");

		public PauseForm(IBrowserOpener browserOpener) {
			settingsComponent = new UiSettingsForm(settings, false).getUiComponent();
			
			getLayout().setOrientation(Axis.Y_AXIS);
			getLayout().setAlignment(Axis.X_AXIS, Alignment.CENTER);
			
			innerForm.setOpaque(true);
			innerForm.setBackground(new TsColor(220, 220, 220));
			innerForm.setLayout(new UiQueueLayout(20, 20, innerForm));
			innerForm.getBorder().setVisibility(true);
			innerForm.getBorder().setColor(TsColor.gray);
			innerForm.getLayout().setOrientation(Axis.Y_AXIS);
			innerForm.getLayout().setAlignment(Axis.X_AXIS, Alignment.CENTER);
			
			btnScores.addMouseListener(new BtnScoreAction(browserOpener));
			
			lblGameOver.setFont(new TsFont("Arial", 70, TsTypeFace.BOLD));
			lblGameOver.setForeground(TsColor.red);
			lblGameOver.setDrawShadow(true);
			lblGameOver.setShadowColor(TsColor.black);
			
			lblRoundsSurvived.setFont(new TsFont("Arial", 30, TsTypeFace.BOLD));
			lblRoundsSurvived.setForeground(TsColor.red);
			lblRoundsSurvived.setDrawShadow(true);
			lblRoundsSurvived.setShadowColor(TsColor.black);
			
			setOptions(true, false);
		}
		
		public void setOptions(boolean showSettings, boolean showGameOver) {
			btnExit.reset();
			innerForm.getLayout().clearComponents();
			getLayout().clearComponents();
			
			if (showGameOver) {
				getLayout().add(lblGameOver);
				getLayout().add(lblRoundsSurvived);
			}
			
			getLayout().add(innerForm);
			
			if (showSettings)
				innerForm.getLayout().add(settingsComponent);
			if (showGameOver && playerCount != -1)
				innerForm.getLayout().add(btnScores);
			innerForm.getLayout().add(btnExit);
			
		}
		
		@Override
		public void draw(IGraphics g) {
			reposition(g.getView());
			super.draw(g);
		}
		
		private void reposition(IRectangleR view) {
			float x = (float) (view.getCenterX() - getWidth() / 2);
			float y = (float) (view.getCenterY() - getHeight() / 2);
			setLocation(x, y);
		}

		@Override
		protected void recieveMouseEvent(TsMouseEvent e, float x, float y) {
			super.recieveMouseEvent(e, x, y);
		}
	}
	
	private class PauseButton extends UiComponent {
		private long timeClicked = 0L;
		private IImage image;
		
		public PauseButton() {
			super(0, 0, 0, 0);
			this.image = Sprite.get(SPRITE_ID).image;
		}

		@Override
		public void draw(IGraphics g) {
			setSize(image.getWidth(g), image.getHeight(g));
			repostion(g.getView());
			getBorder().setVisibility(timeClicked + 50 > System.currentTimeMillis());
			super.draw(g);
			
			g.drawImage(image, getX(), getY());
		}

		@Override
		protected void recieveMouseEvent(TsMouseEvent e, float x, float y) {
			super.recieveMouseEvent(e, x, y);
			
			if (e.action == MouseAction.PRESS)
				timeClicked = System.currentTimeMillis();
			
			if (e.action == MouseAction.RELEASE)
				setPause(!isPaused());
		}
		
		private void repostion(IRectangleR view) {
			float x = (float) (view.getWidth() - getWidth() - 10);
			float y = 10.0f;
			setLocation(x, y);
		}
	}
	
	private class BtnExit extends UiButton {
		private static final String endGameText = "END GAME";
		private static final String areYouSure = "Are you sure you want to exit?";
		
		public BtnExit() {
			super(endGameText);
			setFont(new TsFont("Arial", 20, TsTypeFace.BOLD));
		}
		
		public void reset() {
			setText(endGameText);
			setForeground(TsColor.black);
		}

		@Override
		public void recieveMouseEvent(TsMouseEvent e, float x, float y) {
			super.recieveMouseEvent(e, x, y);
			if (e.action == MouseAction.RELEASE) {
				if (getText().equals(endGameText))
					setAckText();
				else
					requestExitGame = true;
			}
		}
		
		private void setAckText() {
			setText(areYouSure);
			setForeground(TsColor.red);
		}
		
		
	}
	
	private class BtnScoreAction implements Change<TsMouseEvent> {
		private final IBrowserOpener browserOpener;
		
		public BtnScoreAction(IBrowserOpener browserOpener) {
			this.browserOpener = browserOpener;
		}

		@Override
		public void observeChange(TsMouseEvent t) {
			if (t.action == MouseAction.RELEASE)
				browserOpener.openBrowser(scoreSubmitter.
						craftUrl(roundsSurvived, playerCount));
		}
		
	}
}
