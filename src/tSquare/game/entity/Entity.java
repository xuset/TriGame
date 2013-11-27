package tSquare.game.entity;


import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;

import objectIO.connections.Connection;
import objectIO.netObject.NetClass;
import objectIO.netObject.NetVar;
import objectIO.netObject.ObjController;
import objectIO.netObject.OfflineClass;
import tSquare.game.GameIntegratable;
import tSquare.game.GameBoard.ViewRect;
import tSquare.imaging.Sprite;
import tSquare.math.IdGenerator;
import tSquare.math.Point;


public class Entity implements GameIntegratable{
	private boolean removed = false;
	private final boolean owned;
	
	final long id;
	
	protected Sprite sprite;
	protected NetClass objClass;
	protected NetVar.nDouble x;
	protected NetVar.nDouble y;
	protected NetVar.nDouble angle;
	protected NetVar.nDouble scaleX;
	protected NetVar.nDouble scaleY;
	protected NetVar.nString spriteId;
	protected NetVar.nDouble health;

	public final boolean allowUpdates;
	public CollisionBox hitbox;
	public CollisionBox attackbox;
	public boolean visible = true;
	public boolean collidable = true;
	
	private Entity(String stringSpriteId, double startX, double startY, long id,
			ObjController objCtr, boolean allowUpdates, boolean owned) {
		if (!allowUpdates || objCtr == null)
			objClass = new OfflineClass();
		else
			objClass = new NetClass(objCtr, "" + id, 17);
		assignVars(stringSpriteId, startX, startY, objClass);
		this.id = id;
		this.allowUpdates = allowUpdates;
		this.owned = owned;
	}
	
	protected Entity(String sSpriteId, double startX, double startY) {
		this(sSpriteId, startX, startY,
				IdGenerator.getNext(),
				null,
				false,
				true);
	}
	
	protected Entity(String sSpriteId, double startX, double startY, ObjController objCtr) {
		this(sSpriteId, startX, startY, IdGenerator.getNext(), objCtr, true, true);
	}
	
	public Entity(String sSpriteId, double startX, double startY, EntityKey key) {
		this(sSpriteId, startX, startY, key.id, key.objController, key.allowUpdates, key.owned);
	}
	
	private void assignVars(String sSpriteId, double startX, double startY, NetClass cls) {
		objClass = cls;
		sprite = Sprite.add(sSpriteId);
		x = new NetVar.nDouble(startX, "x", objClass);
		y = new NetVar.nDouble(startY, "y", objClass);
		angle = new NetVar.nDouble(90.0, "a", objClass);
		scaleX = new NetVar.nDouble(1.0, "scaleX", objClass);
		scaleY = new NetVar.nDouble(1.0, "scaleY", objClass);
		spriteId = new NetVar.nString(sSpriteId, "spriteId", objClass);
		health = new NetVar.nDouble(100.0, "hlth", objClass);
		hitbox = new CollisionBox(CollisionBox.Type.Hitbox, this);
		attackbox = new CollisionBox(CollisionBox.Type.AttackBox, this);
		spriteId.event = spriteIdEvent;
		
	}
	
	public static Entity createOffline(String spriteId, int x, int y) {
		return new Entity(spriteId, x, y, IdGenerator.getNext(), null, false, true);
	}
	
	public final String getSpriteId() { return spriteId.get(); }
	public final double getX() { return x.get(); }
	public final double getY() { return y.get(); }
	public final int getIntX() { return x.get().intValue(); }
	public final int getIntY() { return y.get().intValue(); }
	public final Point getLocation() { return new Point(x.get(), y.get()); }
	public final int getWidth() { return (int) (sprite.getWidth() * scaleX.get()); }
	public final int getHeight() { return (int) (sprite.getHeight() * scaleY.get()); }
	public final double getCenterX() { return x.get() + getWidth()/2; }
	public final double getCenterY() { return y.get() + getHeight()/2; }
	public final Point getCenter() { return new Point(getCenterX(), getCenterY()); }
	public final double getAngle() { return angle.get(); }
	public final double getScaleX() { return scaleX.get(); }
	public final double getScaleY() { return scaleY.get(); }
	public final double getHealth() { return health.get(); }
	public final long getId() { return id; }
	public final boolean removeRequested() { return removed; }
	public final boolean owned() { return owned; }
	
	public void setSprite(String spriteId) {
		sprite = Sprite.add(spriteId);
		this.spriteId.set(spriteId);
	}
	
	private NetVar.OnChange<String> spriteIdEvent = new NetVar.OnChange<String>() {
		@Override
		public void onChange(NetVar<String> var, Connection c) {
			sprite = Sprite.add(var.get());
		}
	};
	
	public void setAngle(double degrees) 	{ this.angle.set(degrees); }
	public void setX(double x) 				{ this.x.set(x);}
	public void setY(double y) 				{ this.y.set(y);}
	public void setLocation(Point p) { setLocation(p.x, p.y); }
	public void moveForward(double distance) {
		double newX = x.get() + (Math.cos(Math.toRadians(-angle.get())) * distance);
		double newY = y.get() +  (Math.sin(Math.toRadians(-angle.get())) * distance);
		setLocation(newX, newY);
	}
	public void setCenter(double x, double y) {
		setLocation(x - getWidth()/2, y - getHeight()/2);
	}
	public void setCenter(Point p) {
		setCenter(p.x, p.y);
	}
	public void setLocation(double x, double y) {
		this.x.set(x);
		this.y.set(y);
	}
	
	public void turn(double x, double y) {
		setAngle(Point.degrees(this.x.get() + getWidth()/2.0, this.y.get() + getHeight()/2.0, x, y));
	}
	public void turn(Point p) {
		turn(p.x, p.y);
	}
	
	public void setScaleX(double scale) { scaleX.set(scale); }
	public void setScaleY(double scale) { scaleY.set(scale); }
	public void setScale(double x, double y) {
		scaleX.set(x);
		scaleY.set(y);
	}
	
	public double modifyHealth(double delta) {
		health.set(health.get() + delta);
		return health.get();
	}
	
	@Override
	public void draw(Graphics2D g, ViewRect rect) {
		if (!visible) return;
		
		int x = (int) (this.x.get() - rect.getX());
		int y = (int) (this.y.get() - rect.getY());
		int w = (int) (sprite.getWidth() * scaleX.get());
		int h = (int) (sprite.getHeight() * scaleY.get());
	
		if (!rect.isInside(this.x.get(), this.y.get(), w, h))
			return;
		
		if ((angle.get().intValue() - 90) % 360 == 0) {
			sprite.draw(x, y, w, h, 0, 0, sprite.getWidth(), sprite.getHeight(), g);
		} else {
			if (scaleX.get() != 1 || scaleY.get() != 1)
				sprite.draw(x, y, angle.get(), scaleX.get(), scaleY.get(), g);
			else
				sprite.draw(x, y, angle.get(), g);
		}
	}
	
	public <T extends Entity> ArrayList<T> collidedWith(Collection<T> searchList) {
		return collidedWith(searchList, searchList.size());
	}
	public <T extends Entity> ArrayList<T> collidedWith(Collection<T> searchList, int maxReturns) {
		ArrayList<T> hitlist = new ArrayList<T>();
		int hits = 0;
		for (T type : searchList) {
			if (hits >= maxReturns)
				break;
			if (!type.equals(this)) {
				if (type.collidable == true && type.hitbox.intersects(this.hitbox)) {
					hitlist.add(type);
					hits++;
				}
			}
		}
		return hitlist;
	}
	
	public int numberOfCollisions(Collection<? extends Entity> searchList) {
		return numberOfCollisions(searchList, searchList.size());
	}
	public int numberOfCollisions(Collection<? extends Entity> searchList, int maxReturns) {
		int hits = 0;
		for (Entity e : searchList) {
			if (hits >= maxReturns)
				break;
			if (!e.equals(this)) {
				if (e.collidable == true && e.hitbox.intersects(this.hitbox)) {
					hits++;
				}
			}
		}
		return hits;
	}
	
	public <T extends Entity> T collidedWithFirst(Collection<T> searchList) {
		for (T type : searchList) {
			if (!type.equals(this)) {
				if (type.collidable == true && type.hitbox.intersects(this.hitbox)) {
					return type;
				}
			}
		}
		return null;
	}
	
	public boolean collidedWith(Entity e) {
		return (e.collidable && !equals(e) && hitbox.intersects(e.hitbox));
	}
	
	public boolean collided(Collection<? extends Entity> searchList) {
		for (Entity e : searchList) {
			if (e.collidable && !equals(e) && e.hitbox.intersects(hitbox)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void performLogic(int frameDelta) { }
	
	public void onRemoved() { }
	
	public void remove() {
		removed = true;
	}
}
