package triGame.game.entities;

import tSquare.game.entity.Entity;
import tSquare.math.IdGenerator;

public class PointWell extends Entity {
	public static final String SPRITE_ID = "media/PointWell.png";
	
	private final PointParticle[] particles = new PointParticle[5];
	private final PointWellManager manager;

	protected PointWell(double startX, double startY, PointWellManager manager, long id) {
		super(SPRITE_ID, startX, startY, manager, id);
		this.manager = manager;
		manager.objectGrid.turnOnRectangle(startX, startY, getWidth(), getHeight());
		for (int i = 0; i < particles.length; i++) {
			particles[i] = new PointParticle.Hovering((int) getCenterX(), (int) getCenterY(), manager.gameBoard);
			manager.getGame().particleController.addParticle(particles[i]);
		}
	}
	
	public static PointWell create(int x, int y, PointWellManager manager) {
		PointWell p = new PointWell(x, y, manager, IdGenerator.getInstance().getId());
		p.createOnNetwork(true);
		manager.add(p);
		return p;
	}
	
	public void draw() {
		if (manager.getGame().buildingManager.objectGrid.isBlockOpen(getX(), getY()))
			super.draw();
	}

	@Override
	public void remove() {
		manager.objectGrid.turnOffRectangle(getX(), getY(), getWidth(), getHeight());
		super.remove();
	}
	

}
