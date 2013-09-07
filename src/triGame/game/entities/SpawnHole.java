package triGame.game.entities;

import tSquare.game.entity.Entity;

public class SpawnHole extends Entity{
	public static final String SPRITE_ID = "hole";
	
	public SpawnHole(int x, int y, SpawnHoleManager manager, long id) {
		super("hole", x, y, manager, id);
	}
	
	public static SpawnHole create(int x, int y, SpawnHoleManager manager) {
		SpawnHole s = new SpawnHole(x, y, manager, manager.getUniqueId());
		s.createOnNetwork(false);
		manager.add(s);
		return s;
	}
	
	public String createToString() {
		return (int) x + ":" + (int) y;
	}
}
