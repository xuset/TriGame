package triGame.game.entities.buildings;

import java.util.ArrayList;

import tSquare.game.entity.Entity;
import tSquare.game.entity.Manager;
import tSquare.game.entity.ManagerController;
import tSquare.game.particles.ParticleController;
import tSquare.paths.ObjectGrid;
import triGame.game.GameMode;
import triGame.game.ManagerService;
import triGame.game.Params;
import triGame.game.entities.buildings.Building.BuildingInfo;
import triGame.game.entities.buildings.types.HeadQuarters;
import triGame.game.shopping.ShopManager;
import triGame.game.ui.UserInterface;

public class BuildingManager extends Manager<Building> {
	public static final String HASH_MAP_KEY = "building";
	
	private final GameMode gameMode;
	private final CreationFuncs creators;
	public final ObjectGrid objectGrid;
	public final ArrayList<Building> interactives;
	
	public BuildingCreator getCreator(BuildingInfo info) { return creators.getCreator(info); }

	public BuildingManager(ManagerController mc, ManagerService managers,
			GameMode gameMode, ShopManager shop, ParticleController particle) {
		
		super(mc, HASH_MAP_KEY);
		this.gameMode = gameMode;
		interactives = new ArrayList<Building>();
		objectGrid = new ObjectGrid(Params.GAME_WIDTH, Params.GAME_HEIGHT,
				Params.BLOCK_SIZE, Params.BLOCK_SIZE);
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
	
	public void addItemsToUI(UserInterface ui) {
		for (BuildingInfo info : CreationFuncs.INFOS) {
			if (info.isShopable()) {
				ui.arsenal.addTower(info, getCreator(info));
			}
		}
	}
	
	public Building getHQ() {
		if (list.isEmpty())
			return null;
		for (Building b : list) {
			if (b.info == HeadQuarters.INFO)
				return b;
		}
		return null;
	}
	
	public Building getByLocation(double x, double y) {
		for (Building b : list) {
			if (b.getX() == x && b.getY() == y)
				return b;
		}
		return null;
	}
}
