package net.xuset.triGame.game.entities.buildings;

import net.xuset.tSquare.game.entity.CreationHandler;
import net.xuset.tSquare.game.entity.Entity;
import net.xuset.tSquare.imaging.Sprite;
import net.xuset.triGame.game.ManagerService;
import net.xuset.triGame.game.SafeBoard;
import net.xuset.triGame.game.entities.LocManCreator;
import net.xuset.triGame.game.entities.buildings.Building.BuildingInfo;


public class BuildingCreator extends LocManCreator<Building> {
	private final ManagerService managers;
	private final BuildingInfo info;
	private final SafeBoard safeBoard;

	public BuildingCreator(BuildingInfo info, BuildingManager manager, SafeBoard safeBoard,
			CreationHandler ch, ManagerService managers, LocationFunc<? extends Building> iFace) {
		
		super(info.identifier, ch, manager, iFace);
		this.managers = managers;
		this.safeBoard = safeBoard;
		this.info = info;
	}
	
	public boolean equals(BuildingInfo info) {
		return this.info == info;
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
				managers.spawnHole.objectGrid.isRectangleOpen(x, y, w, h) &&
				managers.pointWell.objectGrid.isRectangleOpen(x, y, w, h);
	}
}
