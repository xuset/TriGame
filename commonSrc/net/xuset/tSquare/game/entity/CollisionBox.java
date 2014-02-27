package net.xuset.tSquare.game.entity;

import net.xuset.objectIO.netObject.NetClass;
import net.xuset.objectIO.netObject.NetVar;
import net.xuset.tSquare.math.rect.RectangleR;



public final class CollisionBox extends RectangleR{
	private Entity entity;
	
	public enum Type { Hitbox, AttackBox }

	public NetVar.nDouble offsetX1;
	public NetVar.nDouble offsetY1;
	public NetVar.nDouble offsetX2;
	public NetVar.nDouble offsetY2;
	
	CollisionBox(Type type, Entity e, NetClass objClass) {
		this(type.name(), e, objClass);
	}
	
	private CollisionBox(String name, Entity e, NetClass objClass) {
		entity = e;
		offsetX1 = new NetVar.nDouble(0.0, name + "x1", objClass);
		offsetY1 = new NetVar.nDouble(0.0, name + "y1", objClass);
		offsetX2 = new NetVar.nDouble(0.0, name + "x2", objClass);
		offsetY2 = new NetVar.nDouble(0.0, name + "y2", objClass);
	}
	
	public final void setOffsetCorners(double x1, double y1, double x2, double y2) {
		setOffsetTopLeft(x1, y1);
		setOffsetBottomRight(x2, y2);
	}
	
	public final void setOffsetTopLeft(double x, double y) {
		offsetX1.set(x);
		offsetY1.set(y);
	}
	
	public final void setOffsetBottomRight(double x, double y) {
		offsetX2.set(x);
		offsetY2.set(y);
	}

	@Override
	public final double getHeight() {
		return entity.getHeight() + offsetY2.get() - offsetY1.get();
	}

	@Override
	public final double getWidth() {
		return entity.getWidth() + offsetX2.get() - offsetX1.get();
	}

	@Override
	public final double getX() {
		return entity.getX() + offsetX1.get();
	}

	@Override
	public final double getY() {
		return entity.getY() + offsetY1.get();
	}
	
}
