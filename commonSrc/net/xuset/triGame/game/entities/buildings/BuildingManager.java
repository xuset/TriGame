package net.xuset.triGame.game.entities.buildings;

import java.util.ArrayList;

import net.xuset.tSquare.game.entity.Entity;
import net.xuset.tSquare.game.entity.Manager;
import net.xuset.tSquare.game.entity.ManagerController;
import net.xuset.tSquare.game.particles.ParticleController;
import net.xuset.tSquare.paths.ObjectGrid;
import net.xuset.triGame.game.GameGrid;
import net.xuset.triGame.game.GameMode;
import net.xuset.triGame.game.ManagerService;
import net.xuset.triGame.game.entities.buildings.Building.BuildingInfo;
import net.xuset.triGame.game.entities.buildings.types.HeadQuarters;
import net.xuset.triGame.game.shopping.ShopManager;
import net.xuset.triGame.game.ui.arsenal.ArsenalForm.ArsenalItemAdder;



public class BuildingManager extends Manager<Building> {
	public static final String HASH_MAP_KEY = "building";
	
	private final GameMode gameMode;
	private final CreationFuncs creators;
	private final BuildingGetter buildingGetter;
	public final ObjectGrid objectGrid;
	public final ArrayList<Building> interactives;
	
	public BuildingInfo[] getInfos() { return CreationFuncs.INFOS; }
	public BuildingCreator getCreator(BuildingInfo info) { return creators.getCreator(info); }
	public BuildingGetter getBuildingGetter() { return buildingGetter; }

	public BuildingManager(ManagerController mc, ManagerService managers, GameGrid gameGrid,
			GameMode gameMode, ShopManager shop, ParticleController particle,
			BuildingGetter buildingGetter) {
		
		super(mc, HASH_MAP_KEY, buildingGetter.getList());
		this.gameMode = gameMode;
		this.buildingGetter = buildingGetter;
		interactives = new ArrayList<Building>();
		objectGrid = new ObjectGrid(gameGrid.getGridWidth(), gameGrid.getGridHeight());
		creators = new CreationFuncs(this, mc, managers, gameMode, shop, particle);
	}
	
	@Override
	protected void onRemove(Entity e) {
		objectGrid.turnOffRectangle(e.getX(), e.getY(), e.getWidth(), e.getHeight());
		gameMode.getSafeBoard().removeVisibility(e);
		if (((Building) e).isInteractive())
			interactives.remove(e);
	}
	
	@Override
	protected void onAdd(Building b) {
		objectGrid.turnOnRectangle(b.getX(), b.getY(), b.getWidth(), b.getHeight());
		if (b.getVisibilityRadius() > 0)
			gameMode.getSafeBoard().addVisibilityForEntity(b, b.getVisibilityRadius());
		if (b.isInteractive())
			interactives.add(b);
	}
	
	public void addItemsToUI(ArsenalItemAdder itemAdder) {
		for (BuildingInfo info : CreationFuncs.INFOS) {
			if (info.isShopable()) {
				itemAdder.addBuilding(info, getCreator(info));
			}
		}
	}
	
	@Deprecated
	public Building getHQ() {
		if (list.isEmpty())
			return null;
		for (Building b : list) {
			if (b.info == HeadQuarters.INFO)
				return b;
		}
		return null;
	}
	
	@Deprecated
	public Building getByLocation(double x, double y) {
		for (Building b : list) {
			if (b.getX() == x && b.getY() == y)
				return b;
		}
		return null;
	}
}
