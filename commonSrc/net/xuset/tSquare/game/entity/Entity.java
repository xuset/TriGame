package net.xuset.tSquare.game.entity;


import java.util.ArrayList;
import java.util.List;

import net.xuset.objectIO.markupMsg.MarkupMsg;
import net.xuset.objectIO.netObj.ArrayNetClass;
import net.xuset.objectIO.netObj.NetClass;
import net.xuset.objectIO.netObj.NetVar;
import net.xuset.objectIO.netObj.NetVarListener;
import net.xuset.tSquare.game.GameIntegratable;
import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.Sprite;
import net.xuset.tSquare.math.IdGenerator;
import net.xuset.tSquare.math.point.IPointR;
import net.xuset.tSquare.math.point.Point;
import net.xuset.tSquare.math.point.PointR;
import net.xuset.tSquare.math.rect.IRectangleR;




public class Entity implements GameIntegratable{
	private boolean updatesAllowed = false;
	private boolean removed = false;
	private boolean createdOnNetwork = false;
	private final boolean owned;
	private final NetClass objClass;
	
	protected Sprite sprite;
	protected NetVar.nDouble x;
	protected NetVar.nDouble y;
	protected NetVar.nDouble angle;
	protected NetVar.nDouble scaleX;
	protected NetVar.nDouble scaleY;
	protected NetVar.nString spriteId;
	protected NetVar.nDouble health;

	public final long id;
	public CollisionBox hitbox;
	public boolean visible = true;
	public boolean collidable = true;
	
	private Entity(String sSpriteId, double startX, double startY, long id,
			boolean owned, MarkupMsg initialValues) {
		
		this.id = id;
		this.owned = owned;
		createdOnNetwork = !owned;
		
		objClass = new ArrayNetClass("" + id);
		
		sprite = Sprite.get(sSpriteId);
		x = new NetVar.nDouble("x", startX);
		y = new NetVar.nDouble("y", startY);
		angle = new NetVar.nDouble("a", Math.PI/2);
		scaleX = new NetVar.nDouble("scaleX", 1.0);
		scaleY = new NetVar.nDouble("scaleY", 1.0);
		spriteId = new NetVar.nString("spriteId", sSpriteId);
		health = new NetVar.nDouble("hlth", 100.0);
		objClass.addObj(x);
		objClass.addObj(y);
		objClass.addObj(angle);
		objClass.addObj(scaleX);
		objClass.addObj(scaleY);
		objClass.addObj(spriteId);
		objClass.addObj(health);
		
		hitbox = new CollisionBox(this);
		spriteId.setListener(new OnSpriteIdChange());
		
		setNetObjects(objClass);
		if (initialValues != null)
			objClass.deserializeMsg(initialValues);
	}
	
	public Entity(String sSpriteId, double startX, double startY, EntityKey key) {
		this(sSpriteId, startX, startY,
				key == null ? IdGenerator.getNext() : key.id,
				key == null ? true : key.owned,
				key == null ? null : key.initialValues);
	}
	
	public Entity(String sSpriteId, double startX, double startY) {
		this(sSpriteId, startX, startY, null);
	}
	
	public Entity(EntityKey key) {
		this("", 0, 0, key);
	}
	
	@SuppressWarnings("unused")
	protected void setNetObjects(NetClass objClass) {
		
	}
	
	public final boolean isUpdatesAllowed() { return updatesAllowed; }
	public final boolean isCreatedOnNetwork() { return createdOnNetwork; }
	public final String getSpriteId() { return spriteId.get(); }
	public final double getX() { return x.get(); }
	public final double getY() { return y.get(); }
	public final int getIntX() { return x.get().intValue(); }
	public final int getIntY() { return y.get().intValue(); }
	public final IPointR getLocation() { return new Point(x.get(), y.get()); }
	public final float getWidth() { return (float) (sprite.getWidth() * scaleX.get()); }
	public final float getHeight() { return (float) (sprite.getHeight() * scaleY.get()); }
	public final double getCenterX() { return x.get() + getWidth()/2; }
	public final double getCenterY() { return y.get() + getHeight()/2; }
	public final IPointR getCenter() { return new Point(getCenterX(), getCenterY()); }
	public final double getAngle() { return angle.get(); }
	public final double getScaleX() { return scaleX.get(); }
	public final double getScaleY() { return scaleY.get(); }
	public final double getHealth() { return health.get(); }
	public final long getId() { return id; }
	public final boolean removeRequested() { return removed; }
	public final boolean owned() { return owned; }
	
	public void setSprite(String spriteId) {
		this.spriteId.set(spriteId);
		sprite = Sprite.add(spriteId);
	}
	
	private class OnSpriteIdChange implements NetVarListener<String> {
		@Override
		public void onVarChange(String newValue) {
			sprite = Sprite.add(newValue);
			
		}
	}
	
	public void setAngle(double degrees) 	{ this.angle.set(degrees); }
	public void setX(double x) 				{ this.x.set(x);}
	public void setY(double y) 				{ this.y.set(y);}
	public void setLocation(IPointR p) { setLocation(p.getX(), p.getY()); }
	public void moveForward(double distance) {
		double newX = x.get() + Math.cos(angle.get()) * distance;
		double newY = y.get() - Math.sin(angle.get()) * distance;
		setLocation(newX, newY);
	}
	public void setCenter(double x, double y) {
		setLocation(x - getWidth()/2, y - getHeight()/2);
	}
	public void setCenter(IPointR p) {
		setCenter(p.getX(), p.getY());
	}
	public void setLocation(double x, double y) {
		this.x.set(x);
		this.y.set(y);
	}
	
	public void turn(double x, double y) {
		setAngle(PointR.angle(this.x.get() + getWidth()/2.0, this.y.get() + getHeight()/2.0, x, y));
	}
	public void turn(IPointR p) {
		turn(p.getX(), p.getY());
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
	
	public void setHealth(double newHealth) {
		health.set(newHealth);
	}
	
	public boolean isOnScreen(IRectangleR rect) {
		return rect.isInside(getX(), getY(), getWidth(), getHeight());
	}
	
	@Override
	public void draw(IGraphics g) {
		if (!visible || !isOnScreen(g.getView()))
			return;
		
		float x = (float) ((double) this.x.get());
		float y = (float) ((double) this.y.get());
		float w = getWidth();
		float h = getHeight();
		
		if (angle.get() - Math.PI/2 % 360 == 0) {
			if (scaleX.get() != 1 || scaleY.get() != 1)
				sprite.draw(x, y, w, h, 0, 0, sprite.getWidth(), sprite.getHeight(), g);
			else
				sprite.draw(x, y, g);
		} else {
			if (scaleX.get() != 1 || scaleY.get() != 1) {
				//sprite.draw(x, y, angle.get(), scaleX.get(), scaleY.get(), g);
				System.err.println("Rotating while scaling images is not supported yet");
				//TODO implement rotating while scaling images.
			} else {
				sprite.draw(x, y, angle.get(), g);
			}
		}
	}
	
	public <T extends Entity> ArrayList<T> collidedWith(List<T> searchList) {
		return collidedWith(searchList, searchList.size());
	}
	public <T extends Entity> ArrayList<T> collidedWith(List<T> searchList, int maxReturns) {
		ArrayList<T> hitlist = new ArrayList<T>(maxReturns < 10 ? maxReturns : 10);
		int hits = 0;
		for (int i = 0; i < searchList.size(); i++) {
			T type = searchList.get(i);
			if (hits >= maxReturns)
				break;
			if (!type.equals(this)) {
				if (type.collidable == true && type.hitbox.isInside(this.hitbox)) {
					hitlist.add(type);
					hits++;
				}
			}
		}
		return hitlist;
	}
	
	public int numberOfCollisions(List<? extends Entity> searchList) {
		return numberOfCollisions(searchList, searchList.size());
	}
	public int numberOfCollisions(List<? extends Entity> searchList, int maxReturns) {
		int hits = 0;
		for (int i = 0; i < searchList.size(); i++) {
			Entity e = searchList.get(i);
			if (hits >= maxReturns)
				break;
			if (!e.equals(this)) {
				if (e.collidable == true && e.hitbox.isInside(this.hitbox)) {
					hits++;
				}
			}
		}
		return hits;
	}
	
	public <T extends Entity> T collidedWithFirst(List<T> searchList) {
		for (int i = 0; i < searchList.size(); i++) {
			T type = searchList.get(i);
			if (!type.equals(this)) {
				if (type.collidable == true && type.hitbox.isInside(this.hitbox)) {
					return type;
				}
			}
		}
		return null;
	}
	
	public boolean collidedWith(Entity e) {
		return (e.collidable && !equals(e) && hitbox.isInside(e.hitbox));
	}
	
	public boolean collided(List<? extends Entity> searchList) {
		for (int i = 0; i < searchList.size(); i++) {
			Entity e  = searchList.get(i);
			if (e.collidable && !equals(e) && e.hitbox.isInside(hitbox)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void update(int frameDelta) { }
	
	final MarkupMsg serializeToMsg() {
		return objClass.serializeToMsg();
	}
	
	protected void onRemoved() { }
	
	public void remove() {
		removed = true;
	}

	final void handleRemove(NetClass objController) {
		objController.removeObj(objClass);
		onRemoved();
	}
	
	final void addToObjController(NetClass objController) {
		updatesAllowed = true;
		objController.addObj(objClass);
	}
}
