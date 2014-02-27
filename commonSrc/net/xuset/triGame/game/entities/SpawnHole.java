package net.xuset.triGame.game.entities;

import net.xuset.tSquare.game.entity.Entity;
import net.xuset.tSquare.game.entity.EntityKey;

public class SpawnHole extends Entity{
	public static final String SPRITE_ID = "spawnHole";
	
	public SpawnHole(double x, double y) {
		super(SPRITE_ID, x, y);
	}
	
	public SpawnHole(EntityKey key) {
		super(key);
	}
}
