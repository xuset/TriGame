package triGame.game.entities.buildings;

import tSquare.game.entity.CreationHandler;
import tSquare.game.entity.Entity;
import tSquare.imaging.Sprite;
import triGame.game.ManagerService;
import triGame.game.SafeBoard;
import triGame.game.entities.LocManCreator;
import triGame.game.entities.buildings.Building.BuildingInfo;

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
		int w = s.getWidth();
		int h = s.getHeight();
		
		if (!safeBoard.insideSafeArea((int) x, (int) y, w, h))
			return false;
		
		for (Entity e : managers.person.list) {
			if (e.hitbox.intersects(x, y, w, h) ||
					e.hitbox.contains(x, y, w, h))
				return false;
		}
		return managers.building.objectGrid.isRectangleOpen(x, y, w, h) &&
				managers.spawnHole.objectGrid.isRectangleOpen(x, y, w, h) &&
				managers.pointWell.objectGrid.isRectangleOpen(x, y, w, h);
	}
}
