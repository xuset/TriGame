package net.xuset.triGame.game.entities;

import net.xuset.tSquare.game.entity.Entity;
import net.xuset.tSquare.game.entity.EntityKey;
import net.xuset.tSquare.game.entity.LocationCreator;
import net.xuset.tSquare.game.entity.Manager;
import net.xuset.tSquare.game.entity.ManagerController;
import net.xuset.tSquare.game.entity.LocationCreator.LocationFunc;
import net.xuset.tSquare.game.particles.ParticleController;
import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.paths.ObjectGrid;
import net.xuset.triGame.game.GameGrid;


public class PointWellManager extends Manager<PointWell> {
	public static final String HASH_MAP_KEY = "pointWell";
	
	public final ObjectGrid objectGrid;
	
	private final LocationCreator<PointWell> creator;
	private final ParticleController particleContr;

	public PointWellManager(ManagerController controller, GameGrid gameGrid, ParticleController pc) {
		super(controller, HASH_MAP_KEY);
		this.particleContr = pc;
		objectGrid = new ObjectGrid(gameGrid.getGridWidth(), gameGrid.getGridHeight());
		creator = new LocationCreator<PointWell>(HASH_MAP_KEY, controller.creator, new PointWellCreate());
	}
	
	public PointWell create(int x, int y) {
		PointWell pw =  creator.create(x, y, this);
		return pw;
	}
	
	public PointWell getByPoint(double x, double y) {
		for (PointWell p : list) {
			if (p.getX() == x && p.getY() == y)
				return p;
		}
		return null;
	}
	
	@Override
	protected void onAdd(PointWell pw) {
		objectGrid.turnOnRectangle(pw.getX(), pw.getY(), pw.getWidth(), pw.getHeight());
	}
	
	@Override
	protected void onRemove(Entity e) {
		objectGrid.turnOffRectangle(e.getX(), e.getY(), e.getWidth(), e.getHeight());
	}
	
	@Override
	public void draw(IGraphics g) {
		super.draw(g);
	}
	
	private class PointWellCreate implements LocationFunc<PointWell> {
		@Override
		public PointWell create(double x, double y) {
			return new PointWell(x, y, particleContr, null);
		}

		@Override public PointWell create(EntityKey key) {
			return new PointWell(0, 0, particleContr, key);
		}
	}

}
