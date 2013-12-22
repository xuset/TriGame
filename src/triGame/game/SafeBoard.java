package triGame.game;

import tSquare.game.GameIntegratable;
import tSquare.game.entity.Entity;

public abstract class SafeBoard implements GameIntegratable{
	public abstract void addVisibilityForEntity(Entity e, int radius);	
	public abstract void removeVisibility(Entity e);
	public abstract boolean insideSafeArea(int x, int y);
	public abstract boolean insideSafeArea(int x, int y, int width, int height);
}
