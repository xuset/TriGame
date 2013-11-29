package triGame.game.entities.buildings;

import java.util.ArrayList;

import tSquare.game.entity.EntityKey;
import tSquare.game.entity.LocationCreator;
import tSquare.game.entity.LocationCreator.IFace;
import tSquare.game.entity.ManagerController;
import tSquare.util.PlaceHolder;
import triGame.game.ManagerService;
import triGame.game.RoundHandler;
import triGame.game.entities.buildings.Building.BuildingInfo;
import triGame.game.entities.buildings.types.Barrier;
import triGame.game.entities.buildings.types.FreezeTower;
import triGame.game.entities.buildings.types.HeadQuarters;
import triGame.game.entities.buildings.types.LightTower;
import triGame.game.entities.buildings.types.PointCollector;
import triGame.game.entities.buildings.types.PointCollectorCreator;
import triGame.game.entities.buildings.types.SmallTower;
import triGame.game.entities.buildings.types.SteelBarrier;
import triGame.game.entities.buildings.types.Tower;
import triGame.game.entities.buildings.types.TrapDoor;
import triGame.game.shopping.ShopManager;

final class CreationFuncs {
	public static final BuildingInfo[] INFOS = new BuildingInfo[] {
		HeadQuarters.INFO,
		Barrier.INFO,
		SteelBarrier.INFO,
		TrapDoor.INFO,
		LightTower.INFO,
		SmallTower.INFO,
		Tower.INFO,
		PointCollector.INFO,
		FreezeTower.INFO
	};
	
	public final ArrayList<BuildingCreator> creators = new ArrayList<BuildingCreator>();
	private final ManagerService managers;
	private final PlaceHolder<RoundHandler> phRoundHandler;
	private final ShopManager shop;
	
	public BuildingCreator getCreator(BuildingInfo info) {
		for (BuildingCreator c : creators) {
			if (c.equals(info))
				return c;
		}
		return null;
	}
	
	public CreationFuncs(BuildingManager bm, ManagerController mc, ManagerService managers,
			PlaceHolder<RoundHandler> phRoundHandler,
			ShopManager shop) {
		
		this.managers = managers;
		this.phRoundHandler = phRoundHandler;
		this.shop = shop;
		
		creators.add(new BuildingCreator(HeadQuarters.INFO, bm, mc.creator, managers, headQuartersFunc));
		creators.add(new BuildingCreator(LightTower.INFO, bm, mc.creator, managers, lightTowerFunc));
		creators.add(new PointCollectorCreator(PointCollector.INFO, bm, mc.creator, managers, pointCollectorFunc));
		creators.add(new BuildingCreator(SmallTower.INFO, bm, mc.creator, managers, smallTowerFunc));
		creators.add(new BuildingCreator(Tower.INFO, bm, mc.creator, managers, towerFunc));
		creators.add(new BuildingCreator(Barrier.INFO, bm, mc.creator, managers, barrierFunc));
		creators.add(new BuildingCreator(SteelBarrier.INFO, bm, mc.creator, managers, steelFunc));
		creators.add(new BuildingCreator(TrapDoor.INFO, bm, mc.creator, managers, trapDoorFunc));
		creators.add(new BuildingCreator(FreezeTower.INFO, bm, mc.creator, managers, freezeFunc));
	}
	
	
	

	
	private final LocationCreator.IFace<HeadQuarters>headQuartersFunc = new IFace<HeadQuarters>() {

		@Override
		public HeadQuarters create(double x, double y, EntityKey key) {
			return new HeadQuarters(x, y, key);
		}
		
	};
	
	private final LocationCreator.IFace<LightTower> lightTowerFunc = new IFace<LightTower>() {

		@Override
		public LightTower create(double x, double y, EntityKey key) {
			return new LightTower(x, y, key);
		}
		
	};
	
	private final LocationCreator.IFace<PointCollector> pointCollectorFunc = new IFace<PointCollector>() {

		@Override
		public PointCollector create(double x, double y, EntityKey key) {
			return new PointCollector(x, y, shop, phRoundHandler, key);
		}
		
	};
	
	private final LocationCreator.IFace<SmallTower> smallTowerFunc = new IFace<SmallTower>() {

		@Override
		public SmallTower create(double x, double y, EntityKey key) {
			return new SmallTower(x, y, managers, key);
		}
		
	};
	
	private final LocationCreator.IFace<Tower> towerFunc = new IFace<Tower>() {

		@Override
		public Tower create(double x, double y, EntityKey key) {
			return new Tower(x, y, managers, key);
		}
		
	};
	
	private final LocationCreator.IFace<Barrier> barrierFunc = new IFace<Barrier>() {
		@Override
		public Barrier create(double x, double y, EntityKey key) {
			return new Barrier(x, y, key);
		}
	};
	
	private final LocationCreator.IFace<SteelBarrier> steelFunc = new IFace<SteelBarrier>() {
		@Override
		public SteelBarrier create(double x, double y, EntityKey key) {
			return new SteelBarrier(x, y, key);
		}
	};
	
	private final LocationCreator.IFace<TrapDoor> trapDoorFunc = new IFace<TrapDoor>() {
		@Override
		public TrapDoor create(double x, double y, EntityKey key) {
			return new TrapDoor(x, y, managers, key);
		}
	};
	
	private final LocationCreator.IFace<FreezeTower> freezeFunc = new IFace<FreezeTower>() {
		@Override
		public FreezeTower create(double x, double y, EntityKey key) {
			return new FreezeTower(x, y, managers, key);
		}
	};
}
