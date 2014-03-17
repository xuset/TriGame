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
	
	private boolean requestExitGame = false;
	private boolean isGameOver = false;
	private PauseState switchState = null; //null for dont switch
	
	public boolean exitGameRequested() { return requestExitGame; }
	public boolean isPaused() { return popupController.contains(pauseForm); }
	
	public PauseHandler(UiPopupController popupController, Settings settings) {
		this.popupController = popupController;
		this.settings = settings;
		pauseForm = new PauseForm();
		pauseButton = new PauseButton();
		
		popupController.addPopup(pauseButton);
	}
	
	public void update() {
		if (switchState != null) {
			restructure(switchState);
			switchState = null;
		}
	}
	
	public void setGameOver(int roundsSurvived) {
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
		private final UiButton btnExit = new UiButton("END GAME");
		private final UiLabel lblGameOver = new UiLabel("GAME  OVER!");
		private final UiLabel lblRoundsSurvived = new UiLabel("");

		public PauseForm() {
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
			
			btnExit.setFont(new TsFont("Arial", 20, TsTypeFace.BOLD));
			btnExit.addMouseListener(new BtnExitAction());
			
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
			innerForm.getLayout().clearComponents();
			getLayout().clearComponents();
			
			if (showGameOver) {
				getLayout().add(lblGameOver);
				getLayout().add(lblRoundsSurvived);
			}
			
			getLayout().add(innerForm);
			
			if (showSettings)
				innerForm.getLayout().add(settingsComponent);
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
	
	private class BtnExitAction implements Change<TsMouseEvent> {
		@Override
		public void observeChange(TsMouseEvent t) {
			if (t.action == MouseAction.RELEASE)
				requestExitGame = true;
		}
	}
}
