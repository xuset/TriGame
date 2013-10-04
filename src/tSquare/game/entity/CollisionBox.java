package tSquare.game.entity;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import objectIO.netObject.NetVar;

public final class CollisionBox extends Rectangle2D {
	
	private Rectangle box = new Rectangle();
	private Entity entity;
	
	public enum Type { Hitbox, AttackBox }

	public NetVar.nDouble offsetX;
	public NetVar.nDouble offsetY;
	public NetVar.nInt width;
	public NetVar.nInt height;
	
	CollisionBox(Type type, Entity e) {
		this(type.name(), e);
	}
	
	private CollisionBox(String name, Entity e) {
		entity = e;
		offsetX = new NetVar.nDouble(0.0, name + "x", e.objClass);
		offsetY = new NetVar.nDouble(0.0, name + "y", e.objClass);
		width = new NetVar.nInt(e.getWidth(), name + "w", e.objClass);
		height = new NetVar.nInt(e.getHeight(), name + "h", e.objClass);
	}
	
	public void update() {
		offsetX.update();
		offsetY.update();
		width.update();
		height.update();
	}
	
	public final void setOffsetX(double x) {
		offsetX.set(x);
	}
	
	public final void setOffsetY(double y) {
		offsetY.set(y);
	}

	@Override
	public final Rectangle2D createIntersection(Rectangle2D arg0) {
		return box.createIntersection(arg0);
	}

	@Override
	public final Rectangle2D createUnion(Rectangle2D arg0) {
		return box.createUnion(arg0);
	}

	@Override
	public final int outcode(double x, double y) {
		return box.outcode(x, y);
	}

	@Override
	public final void setRect(double x, double y, double w, double h) {
		width.set((int) w);
		height.set((int) h);
	}

	@Override
	public final double getHeight() {
		return height.get();
	}

	@Override
	public final double getWidth() {
		return width.get();
	}

	@Override
	public final double getX() {
		return offsetX.get() + entity.getX();
	}

	@Override
	public final double getY() {
		return offsetY.get() + entity.getY();
	}

	@Override
	public final boolean isEmpty() {
		return false;
	}
	
}
