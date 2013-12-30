package triGame.game.entities.buildings;

import java.util.ArrayList;

import tSquare.game.entity.EntityKey;
import tSquare.game.entity.LocationCreator;
import tSquare.game.entity.LocationCreator.LocationFunc;
import tSquare.game.entity.ManagerController;
import tSquare.game.particles.ParticleController;
import triGame.game.GameMode;
import triGame.game.ManagerService;
import triGame.game.SafeBoard;
import triGame.game.entities.buildings.Building.BuildingInfo;
import triGame.game.entities.buildings.types.Barrier;
import triGame.game.entities.buildings.types.FreezeTower;
import triGame.game.entities.buildings.types.HeadQuarters;
import triGame.game.entities.buildings.types.HealthTower;
import triGame.game.entities.buildings.types.LightTower;
import triGame.game.entities.buildings.types.MortarTower;
import triGame.game.entities.buildings.types.PointCollector;
import triGame.game.entities.buildings.types.PointCollectorCreator;
import triGame.game.entities.buildings.types.SmallTower;
import triGame.game.entities.buildings.types.SteelBarrier;
import triGame.game.entities.buildings.types.StrongWall;
import triGame.game.entities.buildings.types.Tower;
import triGame.game.entities.zombies.ZombieTargeter;
import triGame.game.shopping.ShopManager;

final class CreationFuncs {
	public static final BuildingInfo[] INFOS = new BuildingInfo[] {
		StrongWall.INFO,
		HeadQuarters.INFO,
		Barrier.INFO,
		SteelBarrier.INFO,
		LightTower.INFO,
		FreezeTower.INFO,
		SmallTower.INFO,
		PointCollector.INFO,
		HealthTower.INFO,
		Tower.INFO,
		MortarTower.INFO
	};
	
	public final ArrayList<BuildingCreator> creators = new ArrayList<BuildingCreator>();
	private final ManagerService managers;
	private final GameMode gameMode;
	private final ShopManager shop;
	private final ParticleController particle;
	private final ZombieTargeter targeter;
	
	public BuildingCreator getCreator(BuildingInfo info) {
		for (BuildingCreator c : creators) {
			if (c.equals(info))
				return c;
		}
		return null;
	}
	
	public CreationFuncs(BuildingManager bm, ManagerController mc, ManagerService managers,
			GameMode gameMode, ShopManager shop, ParticleController particle) {
		
		this.managers = managers;
		this.gameMode = gameMode;
		this.shop = shop;
		this.particle = particle;
		targeter = gameMode.getZombieTargeter();
		
		SafeBoard sb = gameMode.getSafeBoard();
		
		creators.add(new BuildingCreator(StrongWall.INFO, bm, sb, mc.creator, managers, strongFunc));
		creators.add(new BuildingCreator(HeadQuarters.INFO, bm, sb, mc.creator, managers, headQuartersFunc));
		creators.add(new BuildingCreator(LightTower.INFO, bm, sb, mc.creator, managers, lightTowerFunc));
		creators.add(new PointCollectorCreator(PointCollector.INFO, bm, sb, mc.creator, managers, pointCollectorFunc));
		creators.add(new BuildingCreator(SmallTower.INFO, bm, sb, mc.creator, managers, smallTowerFunc));
		creators.add(new BuildingCreator(Tower.INFO, bm, sb, mc.creator, managers, towerFunc));
		creators.add(new BuildingCreator(Barrier.INFO, bm, sb, mc.creator, managers, barrierFunc));
		creators.add(new BuildingCreator(SteelBarrier.INFO, bm, sb, mc.creator, managers, steelFunc));
		creators.add(new BuildingCreator(FreezeTower.INFO, bm, sb, mc.creator, managers, freezeFunc));
		creators.add(new BuildingCreator(MortarTower.INFO, bm, sb, mc.creator, managers, mortarFunc));
		creators.add(new BuildingCreator(HealthTower.INFO, bm, sb, mc.creator, managers, hpTowerFunc));
	}
	
	
	

	
	private final LocationCreator.LocationFunc<HeadQuarters>headQuartersFunc = new LocationFunc<HeadQuarters>() {
		public HeadQuarters create(double x, double y, EntityKey key) {
			return new HeadQuarters(x, y, particle, key);
		}

		@Override
		public HeadQuarters create(EntityKey key) {
			return create(0, 0, key);
		}

		@Override
		public HeadQuarters create(double x, double y) {
			return create(x, y, null);
		}
		
	};
	
	private final LocationCreator.LocationFunc<LightTower> lightTowerFunc = new LocationFunc<LightTower>() {
		public LightTower create(double x, double y, EntityKey key) {
			return new LightTower(x, y, particle, key);
		}

		@Override
		public LightTower create(EntityKey key) {
			return create(0, 0, key);
		}

		@Override
		public LightTower create(double x, double y) {
			return create(x, y, null);
		}
		
	};
	
	private final LocationCreator.LocationFunc<PointCollector> pointCollectorFunc = new LocationFunc<PointCollector>() {
		public PointCollector create(double x, double y, EntityKey key) {
			return new PointCollector(x, y, managers, particle, shop, gameMode, key);
		}

		@Override
		public PointCollector create(EntityKey key) {
			return create(0, 0, key);
		}

		@Override
		public PointCollector create(double x, double y) {
			return create(x, y, null);
		}
		
	};
	
	private final LocationCreator.LocationFunc<SmallTower> smallTowerFunc = new LocationFunc<SmallTower>() {
		public SmallTower create(double x, double y, EntityKey key) {
			return new SmallTower(x, y, particle, targeter, managers.projectile, key);
		}

		@Override
		public SmallTower create(EntityKey key) {
			return create(0, 0, key);
		}

		@Override
		public SmallTower create(double x, double y) {
			return create(x, y, null);
		}
		
	};
	
	private final LocationCreator.LocationFunc<Tower> towerFunc = new LocationFunc<Tower>() {
		public Tower create(double x, double y, EntityKey key) {
			return new Tower(x, y, particle, targeter, managers.projectile, key);
		}

		@Override
		public Tower create(EntityKey key) {
			return create(0, 0, key);
		}

		@Override
		public Tower create(double x, double y) {
			return create(x, y, null);
		}
		
	};
	
	private final LocationCreator.LocationFunc<Barrier> barrierFunc = new LocationFunc<Barrier>() {
		@Override
		public Barrier create(EntityKey key) {
			return new Barrier(0, 0, key);
		}

		@Override
		public Barrier create(double x, double y) {
			return new Barrier(x, y, null);
		}
	};
	
	private final LocationCreator.LocationFunc<SteelBarrier> steelFunc = new LocationFunc<SteelBarrier>() {
		@Override
		public SteelBarrier create(EntityKey key) {
			return new SteelBarrier(0, 0, key);
		}

		@Override
		public SteelBarrier create(double x, double y) {
			return new SteelBarrier(x, y, null);
		}
	};
	
	private final LocationCreator.LocationFunc<FreezeTower> freezeFunc = new LocationFunc<FreezeTower>() {
		public FreezeTower create(double x, double y, EntityKey key) {
			return new FreezeTower(x, y, particle, managers, key);
		}

		@Override
		public FreezeTower create(EntityKey key) {
			return create(0, 0, key);
		}

		@Override
		public FreezeTower create(double x, double y) {
			return create(x, y, null);
		}
	};
	
	private final LocationCreator.LocationFunc<MortarTower> mortarFunc = new LocationFunc<MortarTower>() {
		public MortarTower create(double x, double y, EntityKey key) {
			return new MortarTower(x, y, particle, targeter, managers.projectile, key);
		}

		@Override
		public MortarTower create(EntityKey key) {
			return create(0, 0, key);
		}

		@Override
		public MortarTower create(double x, double y) {
			return create(x, y, null);
		}
	};
	
	private final LocationCreator.LocationFunc<HealthTower> hpTowerFunc = new LocationFunc<HealthTower>() {
		
		public HealthTower create(double x, double y, EntityKey key) {
			return new HealthTower(x, y, particle, managers, key);
		}
		
		@Override
		public HealthTower create(double x, double y) {
			return create(x, y, null);
		}
		
		@Override
		public HealthTower create(EntityKey key) {
			return create(0, 0, key);
		}
	};
	
	private final LocationCreator.LocationFunc<StrongWall> strongFunc = new LocationFunc<StrongWall>() {

		@Override
		public StrongWall create(EntityKey key) {
			return new StrongWall(0, 0, key);
		}

		@Override
		public StrongWall create(double x, double y) {
			return new StrongWall(x, y, null);
		}
	};
}
