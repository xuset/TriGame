package triGame.game.entities.buildings;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import tSquare.game.entity.Entity;
import tSquare.game.entity.Manager;
import tSquare.game.entity.ManagerController;
import tSquare.imaging.Sprite;
import tSquare.paths.ObjectGrid;
import tSquare.util.PlaceHolder;
import triGame.game.ManagerService;
import triGame.game.Params;
import triGame.game.RoundHandler;
import triGame.game.entities.buildings.Building.BuildingInfo;
import triGame.game.entities.buildings.types.HeadQuarters;
import triGame.game.safeArea.SafeAreaBoard;
import triGame.game.shopping.ShopManager;
import triGame.game.ui.UserInterface;
import triGame.game.ui.arsenal.ArsenalItem;

public class BuildingManager extends Manager<Building> {
	public static final String HASH_MAP_KEY = "building";
	
	private final SafeAreaBoard safeBoard;
	private final CreationFuncs creators;
	public final ObjectGrid objectGrid;
	public final ArrayList<Building> interactives;
	
	public BuildingCreator getCreator(BuildingInfo info) { return creators.getCreator(info); }

	public BuildingManager(ManagerController mc, ManagerService managers,
			SafeAreaBoard safeBoard, PlaceHolder<RoundHandler> phRoundHandler,
			ShopManager shop) {
		
		super(mc, HASH_MAP_KEY);
		this.safeBoard = safeBoard;
		interactives = new ArrayList<Building>();
		objectGrid = new ObjectGrid(Params.GAME_WIDTH, Params.GAME_HEIGHT,
				Params.BLOCK_SIZE, Params.BLOCK_SIZE);
		creators = new CreationFuncs(this, mc, managers, phRoundHandler, shop);
	}
	
	@Override
	protected void onRemove(Entity e) {
		objectGrid.turnOffRectangle(e.getX(), e.getY(), e.getWidth(), e.getHeight());
		safeBoard.removeVisibility(e);
		if (((Building) e).isInteractive())
			interactives.remove(e);
	}
	
	@Override
	protected void onAdd(Building b) {
		objectGrid.turnOnRectangle(b.getX(), b.getY(), b.getWidth(), b.getHeight());
		if (b.getVisibilityRadius() > 0)
			safeBoard.addVisibilityForEntity(b, b.getVisibilityRadius());
		if (b.isInteractive())
			interactives.add(b);
	}
	
	public void addItemsToUI(UserInterface ui) {
		for (BuildingInfo info : CreationFuncs.INFOS) {
			if (info.isShopable()) {
				BufferedImage image = Sprite.get(info.spriteId).getBuffered();
				ArsenalItem item = new ArsenalItem(info.item, image, info.visibilityRadius, getCreator(info));
				item.getInfo().description = info.description;
				ui.arsenal.panel.addToArsenal(ui.arsenal.towerGroup, item);
			}
		}
	}
	
	public Building getHQ() {
		if (list.isEmpty())
			return null;
		Building b = list.get(0);
		if (b.info == HeadQuarters.INFO)
			return b;
		return null;
	}
}
