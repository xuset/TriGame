package net.xuset.triGame.game;

import net.xuset.tSquare.game.GameIntegratable;
import net.xuset.tSquare.game.entity.Entity;

public abstract class SafeBoard implements GameIntegratable{
	public abstract void addVisibilityForEntity(Entity e, double radius);	
	public abstract void removeVisibility(Entity e);
	public abstract boolean insideSafeArea(double x, double y);
	public abstract boolean insideSafeArea(double x, double y, double width, double height);
}
