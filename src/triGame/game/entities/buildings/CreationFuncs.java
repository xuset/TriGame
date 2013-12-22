package triGame.game.entities.buildings;

import java.util.ArrayList;

import tSquare.game.entity.EntityKey;
import tSquare.game.entity.LocationCreator;
import tSquare.game.entity.LocationCreator.IFace;
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
	
	
	

	
	private final LocationCreator.IFace<HeadQuarters>headQuartersFunc = new IFace<HeadQuarters>() {

		@Override
		public HeadQuarters create(double x, double y, EntityKey key) {
			return new HeadQuarters(x, y, particle, key);
		}
		
	};
	
	private final LocationCreator.IFace<LightTower> lightTowerFunc = new IFace<LightTower>() {

		@Override
		public LightTower create(double x, double y, EntityKey key) {
			return new LightTower(x, y, particle, key);
		}
		
	};
	
	private final LocationCreator.IFace<PointCollector> pointCollectorFunc = new IFace<PointCollector>() {

		@Override
		public PointCollector create(double x, double y, EntityKey key) {
			return new PointCollector(x, y, managers, particle, shop, gameMode, key);
		}
		
	};
	
	private final LocationCreator.IFace<SmallTower> smallTowerFunc = new IFace<SmallTower>() {

		@Override
		public SmallTower create(double x, double y, EntityKey key) {
			return new SmallTower(x, y, particle, targeter, managers.projectile, key);
		}
		
	};
	
	private final LocationCreator.IFace<Tower> towerFunc = new IFace<Tower>() {

		@Override
		public Tower create(double x, double y, EntityKey key) {
			return new Tower(x, y, particle, targeter, managers.projectile, key);
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
	
	private final LocationCreator.IFace<FreezeTower> freezeFunc = new IFace<FreezeTower>() {
		@Override
		public FreezeTower create(double x, double y, EntityKey key) {
			return new FreezeTower(x, y, particle, managers, key);
		}
	};
	
	private final LocationCreator.IFace<MortarTower> mortarFunc = new IFace<MortarTower>() {
		@Override
		public MortarTower create(double x, double y, EntityKey key) {
			return new MortarTower(x, y, particle, targeter, managers.projectile, key);
		}
	};
	
	private final LocationCreator.IFace<HealthTower> hpTowerFunc = new IFace<HealthTower>() {
		@Override
		public HealthTower create(double x, double y, EntityKey key) {
			return new HealthTower(x, y, particle, managers, key);
		}
	};
	
	private final LocationCreator.IFace<StrongWall> strongFunc = new IFace<StrongWall>() {
		@Override
		public StrongWall create(double x, double y, EntityKey key) {
			return new StrongWall(x, y, key);
		}
	};
}
