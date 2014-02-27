package net.xuset.triGame.game.ui;

import net.xuset.tSquare.game.GameDrawable;
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
import net.xuset.triGame.game.settings.SettingsContainer;
import net.xuset.triGame.game.shopping.ShopManager;
import net.xuset.triGame.game.ui.arsenal.ArsenalForm;
import net.xuset.triGame.game.ui.arsenal.ArsenalForm.ArsenalItemAdder;
import net.xuset.triGame.game.ui.arsenal.BuildingAttacher;
import net.xuset.triGame.game.ui.gameInput.GameInput;
import net.xuset.triGame.game.ui.gameInput.IGameInput;
import net.xuset.triGame.game.ui.upgrades.UpgradeForm;

public class UserInterface implements GameDrawable {
	private final UiController controller;
	private final ArsenalItemAdder arsenalItemAdder;
	private final BaseForm baseForm;
	
	private final BuildingAttacher attacher;
	private final PointConverter pointConverter;
	private BuildingGetter buildingGetter;
	
	private final IGameInput gameInput;
	
	private final ArsenalForm arsenalForm;
	private final UpgradeForm upgradeForm;
	
	public IGameInput getGameInput() { return gameInput; }
	public ArsenalItemAdder getArsenalItemAdder() { return arsenalItemAdder; }

	public UserInterface(InputHolder input, PointConverter pointConverter,
			ShopManager shop, BuildingGetter buildingGetter, SettingsContainer settings) {
		
		this.pointConverter = pointConverter;
		this.buildingGetter = buildingGetter;
		
		attacher = new BuildingAttacher(input.getMouse(), pointConverter, shop);
		controller = new UiController(input.getMouse());
		baseForm = new BaseForm();
		arsenalForm = new ArsenalForm(attacher, shop);
		upgradeForm = new UpgradeForm(shop);

		input.getMouse().watch(new MouseObserver());

		UiBorderLayout layout = new UiBorderLayout(controller.getForm());
		controller.getForm().setLayout(layout);
		layout.add(baseForm, UiBorderLayout.BorderPosition.SOUTH);
		gameInput = new GameInput(settings, input, layout);
		
		arsenalItemAdder = arsenalForm.itemAdder;
		
		switchView(arsenalForm);
	}
	
	private void switchView(UiForm f) {
		baseForm.getLayout().clearComponents();
		baseForm.getLayout().add(f);
	}
	
	@Override
	public void draw(IGraphics g) {
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
	
}
