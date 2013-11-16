package triGame.game.entities;

import tSquare.game.entity.Entity;
import tSquare.game.entity.EntityKey;

public class SpawnHole extends Entity{
	public static final String SPRITE_ID = "hole";
	
	public SpawnHole(double x, double y, SpawnHoleManager manager, EntityKey key) {
		super(SPRITE_ID, x, y, key);
	}
}
