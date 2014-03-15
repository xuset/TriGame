package net.xuset.triGame.game.ui;

import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.system.input.InputHolder;
import net.xuset.tSquare.system.input.mouse.MouseAction;
import net.xuset.tSquare.system.input.mouse.TsMouseEvent;
import net.xuset.tSquare.ui.UiController;
import net.xuset.tSquare.ui.UiForm;
import net.xuset.tSquare.ui.layout.UiBorderLayout;
import net.xuset.tSquare.util.Observer.Change;
import net.xuset.triGame.settings.Settings;
import net.xuset.triGame.game.PointConverter;
import net.xuset.triGame.game.entities.buildings.BuildingGetter;
import net.xuset.triGame.game.shopping.ShopManager;
import net.xuset.triGame.game.ui.arsenal.ArsenalForm;
import net.xuset.triGame.game.ui.arsenal.ArsenalForm.ArsenalItemAdder;
import net.xuset.triGame.game.ui.arsenal.BuildingAttacher;
import net.xuset.triGame.game.ui.gameInput.GameInput;
import net.xuset.triGame.game.ui.gameInput.IGameInput;
import net.xuset.triGame.game.ui.upgrades.BuildingUpgradeSetter;
import net.xuset.triGame.game.ui.upgrades.UiUpgrades;
import net.xuset.triGame.game.ui.upgrades.UpgradeForm;

public class UserInterface implements UiFormSwitcher, UiCollisionDetector{
	private final UiController controller;
	private final BaseForm baseForm;

	private final BuildingAttacher attacher;
	private final ArsenalForm arsenalForm;
	private final UpgradeForm upgradeForm;
	private final BuildingUpgradeSetter upgradeSetter;
	private final PauseHandler pauseHandler;
	private final ArsenalItemAdder arsenalItemAdder;
	private final GameInput gameInput;
	
	public IGameInput getGameInput() { return gameInput; }
	public ArsenalItemAdder getArsenalItemAdder() { return arsenalItemAdder; }
	public void clearAttachedBuildings() { attacher.clearAttached(); }

	public UserInterface(InputHolder input, PointConverter pointConverter,
			ShopManager shop, BuildingGetter buildingGetter, Settings settings) {

		controller = new UiController(input.getMouse());
		attacher = new BuildingAttacher(input.getMouse(),
				pointConverter, shop, (UiCollisionDetector) this);
		baseForm = new BaseForm();
		upgradeForm = new UpgradeForm(shop);
		UiUpgrades upgrades = new UiUpgrades(upgradeForm, (UiFormSwitcher) this);
		arsenalForm = new ArsenalForm(attacher, shop, upgrades);
		upgradeSetter = new BuildingUpgradeSetter(buildingGetter, pointConverter,
				(UiFormSwitcher) this, upgrades, (UiCollisionDetector) this);
		pauseHandler = new PauseHandler(controller.getPopupController(), settings);
		
		input.getMouse().watch(new MouseObserver());

		UiBorderLayout layout = new UiBorderLayout(controller.getForm());
		controller.getForm().setLayout(layout);
		layout.add(baseForm, UiBorderLayout.BorderPosition.SOUTH);
		gameInput = new GameInput(settings, input.getKeyboard(),
				arsenalForm.arsenalInput, layout);
		
		arsenalItemAdder = arsenalForm.itemAdder;
		
		switchView(UiFormTypes.ARSENAL);
	}
	
	@Override
	public void switchView(UiFormTypes f) {
		switch(f) {
		case ARSENAL:
			switchView(arsenalForm);
			return;
		case UPGRADES:
			switchView(upgradeForm);
			return;
		}
	}
	
	public void draw(IGraphics g, IGraphics transG, int blockSize) {
		pauseHandler.update();
		controller.setScale(blockSize / 50.0f);
		controller.draw(g);
		attacher.draw(transG);
		upgradeSetter.draw(transG);
	}
	
	@Override
	public boolean isCollidingWith(float x, float y) {
		x /= controller.getScale();
		y /= controller.getScale();
		boolean popupCollision = controller.getPopupController().contains(x, y);
		//TODO maybe you dont need gameInput?
		return gameInput.contains(x, y) || baseForm.contains(x, y) || popupCollision;
	}
	
	public void showPauseScreen(boolean showPause) {
		pauseHandler.setPause(showPause);
	}
	
	public boolean isPaused() {
		return pauseHandler.isPaused();
	}
	
	public void setGameOver(boolean isGameOver) {
		pauseHandler.setGameOver(isGameOver);
	}
	
	private void switchView(UiForm f) {
		baseForm.getLayout().clearComponents();
		baseForm.getLayout().add(f);
	}
	
	public boolean exitGameRequested() {
		return pauseHandler.exitGameRequested();
	}
	
	private class MouseObserver implements Change<TsMouseEvent> {

		@Override
		public void observeChange(TsMouseEvent t) {
			if (t.action == MouseAction.PRESS && !isCollidingWith(t.x, t.y))
				pauseHandler.setPause(false);
			upgradeSetter.observeChange(t);
		}
		
	}
	
}
