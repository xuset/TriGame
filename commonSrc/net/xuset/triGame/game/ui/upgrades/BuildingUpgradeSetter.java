package net.xuset.triGame.game.ui.upgrades;

import net.xuset.tSquare.imaging.Sprite;
import net.xuset.tSquare.system.input.mouse.MouseAction;
import net.xuset.tSquare.system.input.mouse.TsMouseEvent;
import net.xuset.tSquare.util.Observer.Change;
import net.xuset.triGame.game.PointConverter;
import net.xuset.triGame.game.entities.buildings.Building;
import net.xuset.triGame.game.entities.buildings.BuildingGetter;
import net.xuset.triGame.game.ui.UiCollisionDetector;
import net.xuset.triGame.game.ui.UiFormSwitcher;
import net.xuset.triGame.game.ui.UiFormTypes;

public class BuildingUpgradeSetter implements Change<TsMouseEvent>{
	private final BuildingGetter buildingGetter;
	private final PointConverter pConv;
	private final UiFormSwitcher uiSwitcher;
	private final UiUpgrades uiUpgrades;
	private final UiCollisionDetector uiCollision;
	
	public BuildingUpgradeSetter(BuildingGetter buildingGetter, PointConverter pConv,
			UiFormSwitcher uiSwitcher, UiUpgrades uiUpgrades,
			UiCollisionDetector uiCollision) {
		
		this.buildingGetter = buildingGetter;
		this.pConv = pConv;
		this.uiSwitcher = uiSwitcher;
		this.uiUpgrades = uiUpgrades;
		this.uiCollision = uiCollision;
	}

	@Override
	public void observeChange(TsMouseEvent t) {
		if (t.action != MouseAction.RELEASE || uiCollision.isCollidingWith(t.x, t.y))
			return;
		double gameX = (int) pConv.screenToGameX(t.x);
		double gameY = (int) pConv.screenToGameY(t.y);
		
		//TODO this may cause a concurrentModificationException 
		Building b = buildingGetter.getByLocation(gameX, gameY);
		if (b != null && b.upgrades != null) {
			Sprite s = Sprite.get(b.getSpriteId());
			uiUpgrades.showUpgrades(s, b.upgrades);
			uiSwitcher.switchView(UiFormTypes.UPGRADES);
		} else {
			uiSwitcher.switchView(UiFormTypes.ARSENAL);
		}
	}
}
