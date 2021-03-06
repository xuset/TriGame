package net.xuset.triGame.game.ui.upgrades;

import net.xuset.tSquare.game.GameDrawable;
import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.Sprite;
import net.xuset.tSquare.imaging.TsColor;
import net.xuset.tSquare.math.point.IPointW;
import net.xuset.tSquare.math.point.Point;
import net.xuset.tSquare.system.input.mouse.MouseAction;
import net.xuset.tSquare.system.input.mouse.TsMouseEvent;
import net.xuset.tSquare.util.Observer.Change;
import net.xuset.triGame.game.PointConverter;
import net.xuset.triGame.game.entities.buildings.Building;
import net.xuset.triGame.game.entities.buildings.BuildingGetter;
import net.xuset.triGame.game.ui.UiCollisionDetector;
import net.xuset.triGame.game.ui.UiFormSwitcher;
import net.xuset.triGame.game.ui.UiFormTypes;

public class BuildingUpgradeSetter implements Change<TsMouseEvent>, GameDrawable{
	private final BuildingGetter buildingGetter;
	private final PointConverter pConv;
	private final UiFormSwitcher uiSwitcher;
	private final UiUpgrades uiUpgrades;
	private final UiCollisionDetector uiCollision;
	
	private IPointW lastMouseLoc = new Point();
	private boolean needsRechecking = false;
	private boolean drawBuildingView = false;
	private Building selected = null;
	
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
		if (t.action == MouseAction.RELEASE && !uiCollision.isCollidingWith(t.x, t.y)) {
			needsRechecking = true;
			lastMouseLoc.setTo(t.x, t.y);
		}
	}
	
	private void checkForSwitch() {
		needsRechecking = false;
		
		double gameX = (int) pConv.screenToGameX(lastMouseLoc.getX());
		double gameY = (int) pConv.screenToGameY(lastMouseLoc.getY());
		
		Building b = buildingGetter.getByLocation(gameX, gameY);
		if (b != null && b.upgrades != null && b.owned() && !b.upgrades.items.isEmpty()) {
			Sprite s = Sprite.get(b.getSpriteId());
			uiUpgrades.showUpgrades(s, b.upgrades);
			uiSwitcher.switchView(UiFormTypes.UPGRADES);
			drawBuildingView = true;
			selected = b;
		} else {
			uiSwitcher.switchView(UiFormTypes.ARSENAL);
			drawBuildingView = false;
			selected = null;
		}
	}

	@Override
	public void draw(IGraphics g) {
		if (needsRechecking)
			checkForSwitch();
		
		if (drawBuildingView) {
			float r = (float) selected.getVisibilityRadius();
			float x = (float) (selected.getX() + selected.getWidth() / 2 - r);
			float y = (float) (selected.getY()  + selected.getHeight() / 2 - r);
			
			g.setColor(TsColor.green);
			g.drawOval(x, y, 2 * r, 2 * r);
		}
	}
}
