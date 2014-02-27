package net.xuset.triGame.game.entities.buildings.types;

import net.xuset.tSquare.game.entity.CreationHandler;
import net.xuset.tSquare.game.entity.Entity;
import net.xuset.tSquare.imaging.Sprite;
import net.xuset.triGame.game.ManagerService;
import net.xuset.triGame.game.SafeBoard;
import net.xuset.triGame.game.entities.buildings.Building;
import net.xuset.triGame.game.entities.buildings.BuildingCreator;
import net.xuset.triGame.game.entities.buildings.BuildingManager;
import net.xuset.triGame.game.entities.buildings.Building.BuildingInfo;


public class PointCollectorCreator extends BuildingCreator {
	private final ManagerService managers;
	private final SafeBoard safeBoard;
	private final BuildingInfo info;

	public PointCollectorCreator(BuildingInfo info, BuildingManager manager,
			SafeBoard safeBoard, CreationHandler ch, ManagerService managers,
			LocationFunc<? extends Building> iFace) {
		
		super(info, manager, safeBoard, ch, managers, iFace);
		this.managers = managers;
		this.safeBoard = safeBoard;
		this.info = info;
	}

	@Override
	public boolean isValidLocation(double x, double y) {
		Sprite s = Sprite.get(info.spriteId);
		float w = s.getWidth();
		float h = s.getHeight();
		
		if (!safeBoard.insideSafeArea(x, y, w, h))
			return false;
		
		for (Entity e : managers.person.list) {
			if (e.hitbox.isInside(x, y, w, h))
				return false;
		}
		
		return managers.building.objectGrid.isRectangleOpen(x, y, w, h) &&
				!managers.pointWell.objectGrid.isRectangleOpen(x, y, w, h);
	}

}
