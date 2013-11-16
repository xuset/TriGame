package triGame.game.entities;

import java.awt.Graphics2D;

import tSquare.game.GameBoard.ViewRect;
import tSquare.game.entity.Entity;
import tSquare.game.entity.EntityKey;
import tSquare.game.entity.LocationCreator;
import tSquare.game.entity.Manager;
import tSquare.game.entity.ManagerController;
import tSquare.game.particles.ParticleController;
import tSquare.paths.ObjectGrid;
import triGame.game.Params;

public class PointWellManager extends Manager<PointWell> {
	public static final String HASH_MAP_KEY = "pointWell";
	
	public final ObjectGrid objectGrid;
	
	private final LocationCreator<PointWell> creator;
	private final ParticleController particleContr;

	public PointWellManager(ManagerController controller, ParticleController pc) {
		super(controller, HASH_MAP_KEY);
		objectGrid = new ObjectGrid(Params.GAME_WIDTH, Params.GAME_HEIGHT, Params.BLOCK_SIZE, Params.BLOCK_SIZE);
		this.particleContr = pc;
		
		creator = new LocationCreator<PointWell>(HASH_MAP_KEY, controller.creator, 
				new LocationCreator.IFace<PointWell>() {
					@Override
					public PointWell create(double x, double y, EntityKey key) {
						return new PointWell(x, y, key, particleContr);
					}
				});
	}
	
	public PointWell create(int x, int y) {
		PointWell pw =  creator.create(x, y, this);
		return pw;
	}
	
	@Override
	protected void onAdd(PointWell pw) {
		objectGrid.turnOnRectangle(pw.getX(), pw.getY(), Params.BLOCK_SIZE, Params.BLOCK_SIZE);
	}
	
	@Override
	protected void onRemove(Entity e) {
		objectGrid.turnOffRectangle(e.getX(), e.getY(), Params.BLOCK_SIZE, Params.BLOCK_SIZE);
	}
	
	@Override
	public void draw(Graphics2D g, ViewRect rect) {
		super.draw(g, rect);
	}

}
