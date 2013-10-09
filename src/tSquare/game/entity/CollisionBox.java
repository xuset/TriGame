package tSquare.game.entity;

import java.awt.geom.Rectangle2D;

import objectIO.netObject.NetVar;

public final class CollisionBox extends Rectangle2D {
	private Entity entity;
	
	public enum Type { Hitbox, AttackBox }

	public NetVar.nDouble offsetX1;
	public NetVar.nDouble offsetY1;
	public NetVar.nDouble offsetX2;
	public NetVar.nDouble offsetY2;
	
	CollisionBox(Type type, Entity e) {
		this(type.name(), e);
	}
	
	private CollisionBox(String name, Entity e) {
		entity = e;
		offsetX1 = new NetVar.nDouble(0.0, name + "x1", e.objClass);
		offsetY1 = new NetVar.nDouble(0.0, name + "y1", e.objClass);
		offsetX2 = new NetVar.nDouble(0.0, name + "x2", e.objClass);
		offsetY2 = new NetVar.nDouble(0.0, name + "y2", e.objClass);
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
		return entity.getHeight() + offsetY1.get() + offsetY2.get();
	}

	@Override
	public final double getWidth() {
		return entity.getWidth() + offsetX1.get() + offsetX2.get();
	}

	@Override
	public final double getX() {
		return entity.getX() + offsetX1.get();
	}

	@Override
	public final double getY() {
		return entity.getY() + offsetY1.get();
	}

	@Override
	public final boolean isEmpty() {
		return false;
	}

	@Override
	public final Rectangle2D createIntersection(Rectangle2D arg0) {
		return null;
	}

	@Override
	public final Rectangle2D createUnion(Rectangle2D arg0) {
		return null;
	}

	@Override
	public final int outcode(double x, double y) {
		return -1;
	}

	@Override
	public final void setRect(double x, double y, double w, double h) {
		return;
	}
	
}
