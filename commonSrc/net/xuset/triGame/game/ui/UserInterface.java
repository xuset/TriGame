package net.xuset.triGame.game.ui;

import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.Sprite;
import net.xuset.tSquare.system.input.InputHolder;
import net.xuset.tSquare.system.input.mouse.MouseAction;
import net.xuset.tSquare.system.input.mouse.TsMouseEvent;
import net.xuset.tSquare.ui.UiController;
import net.xuset.tSquare.ui.UiForm;
import net.xuset.tSquare.ui.layout.UiBorderLayout;
import net.xuset.tSquare.util.Observer.Change;
import net.xuset.triGame.game.PointConverter;
import net.xuset.triGame.game.entities.buildings.Building;
import net.xuset.triGame.game.entities.buildings.BuildingGetter;
import net.xuset.triGame.game.settings.Settings;
import net.xuset.triGame.game.shopping.ShopManager;
import net.xuset.triGame.game.ui.arsenal.ArsenalForm;
import net.xuset.triGame.game.ui.arsenal.ArsenalForm.ArsenalItemAdder;
import net.xuset.triGame.game.ui.arsenal.BuildingAttacher;
import net.xuset.triGame.game.ui.gameInput.GameInput;
import net.xuset.triGame.game.ui.gameInput.IGameInput;
import net.xuset.triGame.game.ui.upgrades.UpgradeForm;

public class UserInterface {
	private final UiController controller;
	private final ArsenalItemAdder arsenalItemAdder;
	private final BaseForm baseForm;
	
	private final BuildingAttacher attacher;
	private final PointConverter pointConverter;
	private BuildingGetter buildingGetter;
	
	private final GameInput gameInput;
	
	private final ArsenalForm arsenalForm;
	private final UpgradeForm upgradeForm;
	
	public IGameInput getGameInput() { return gameInput; }
	public ArsenalItemAdder getArsenalItemAdder() { return arsenalItemAdder; }

	public UserInterface(InputHolder input, PointConverter pointConverter,
			ShopManager shop, BuildingGetter buildingGetter, Settings settings) {
		
		this.pointConverter = pointConverter;
		this.buildingGetter = buildingGetter;

		controller = new UiController(input.getMouse());
		attacher = new BuildingAttacher(input.getMouse(),
				pointConverter, shop, new CollidesWithUI());
		baseForm = new BaseForm();
		arsenalForm = new ArsenalForm(attacher, shop);
		upgradeForm = new UpgradeForm(shop);

		input.getMouse().watch(new MouseObserver());

		UiBorderLayout layout = new UiBorderLayout(controller.getForm());
		controller.getForm().setLayout(layout);
		layout.add(baseForm, UiBorderLayout.BorderPosition.SOUTH);
		gameInput = new GameInput(settings, input.getKeyboard(),
				arsenalForm.arsenalInput, layout);
		
		arsenalItemAdder = arsenalForm.itemAdder;
		
		switchView(arsenalForm);
	}
	
	private void switchView(UiForm f) {
		baseForm.getLayout().clearComponents();
		baseForm.getLayout().add(f);
	}
	
	public void draw(IGraphics g, int blockSize) {
		controller.setScale(blockSize / 50.0f);
		controller.draw(g);
		attacher.draw(g);
	}
	
	private class MouseObserver implements Change<TsMouseEvent> {

		@Override
		public void observeChange(TsMouseEvent t) {
			if (t.action != MouseAction.RELEASE)
				return;
			double gameX = (int) pointConverter.screenToGameX(t.x);
			double gameY = (int) pointConverter.screenToGameY(t.y);
			
			//TODO this may cause a concurrentModificationException 
			Building b = buildingGetter.getByLocation(gameX, gameY);
			if (b != null && b.upgrades != null) {
				Sprite s = Sprite.get(b.getSpriteId());
				upgradeForm.setUpgrade(s, b.upgrades);
				switchView(upgradeForm);
			} else {
				switchView(arsenalForm);
			}
		}
		
	}
	
	public class CollidesWithUI {
		public boolean isColliding(float x, float y) {
			x /= controller.getScale();
			y /= controller.getScale();
			return gameInput.contains(x, y) || baseForm.contains(x, y);
		}
	}
	
}
