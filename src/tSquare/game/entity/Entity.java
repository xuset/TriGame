package tSquare.game.entity;


import java.util.ArrayList;
import java.util.Collection;

import objectIO.markupMsg.MarkupMsg;
import objectIO.netObject.NetClass;
import objectIO.netObject.NetVar;
import objectIO.netObject.OfflineClass;
import tSquare.game.GameBoard;
import tSquare.game.GameIntegratable;
import tSquare.imaging.Sprite;
import tSquare.math.IdGenerator;
import tSquare.math.Point;


public class Entity implements GameIntegratable{
	private boolean removed = false;
	
	boolean createdOnNetwork = false;
	long id;
	boolean owned = true;
	
	protected Manager<? extends Entity> manager;
	protected Sprite sprite;
	protected NetClass objClass;
	protected NetVar.nDouble x;
	protected NetVar.nDouble y;
	protected NetVar.nDouble angle;
	protected NetVar.nDouble scaleX;
	protected NetVar.nDouble scaleY;
	protected NetVar.nString spriteId;
	protected NetVar.nDouble health;
	
	public CollisionBox hitbox;
	public CollisionBox attackbox;
	public boolean visible = true;
	public boolean collidable = true;
	
	protected Entity(String sSpriteId, double startX, double startY, Manager<?> manager, long id) {
		sprite = Sprite.add(sSpriteId);
		
		if (manager != null)
			objClass = new NetClass(manager.game.getNetwork().getObjController(), "" + id, 7);
		else
			objClass = new OfflineClass();
		
		x = new NetVar.nDouble(startX, "x", objClass);
		y = new NetVar.nDouble(startY, "y", objClass);
		angle = new NetVar.nDouble(90.0, "a", objClass);
		scaleX = new NetVar.nDouble(1.0, "scaleX", objClass);
		scaleY = new NetVar.nDouble(1.0, "scaleY", objClass);
		spriteId = new NetVar.nString(sSpriteId, "spriteId", objClass);
		health = new NetVar.nDouble(100.0, "hlth", objClass);
		hitbox = new CollisionBox(CollisionBox.Type.Hitbox, this);
		attackbox = new CollisionBox(CollisionBox.Type.AttackBox, this);
		x.autoUpdate = y.autoUpdate = angle.autoUpdate = scaleX.autoUpdate = scaleY.autoUpdate = objClass.autoUpdate = true;
		
		this.manager = manager;
		this.id = id;
	}
	
	protected Entity(String spriteId, double startX, double startY, Manager<?> manager) {
		this(spriteId, startX, startY, manager, IdGenerator.getInstance().getId());
	}
	
	public Entity(String spriteId, int x, int y) {
		this(spriteId, x, y, null, -1l);
	}
	
	public static Entity create(String spriteId, int x, int y, Manager<Entity> manager) {
		Entity e = new Entity(spriteId, x, y, manager, IdGenerator.getInstance().getId());
		e.createOnNetwork(true);
		manager.add(e);
		return e;
	}
	
	public void updateOnNetwork() {
		/*x.update();
		y.update();
		angle.update();
		width.update();
		height.update();
		spriteId.update();
		hitbox.update();
		attackbox.update();
		objClass.update();*/
	}
	
	boolean allowUpdates = false;
	protected void createOnNetwork(boolean allowUpdates) {
		if (!createdOnNetwork && manager != null) {
			manager.getCreator().createOnNetwork(this);
			createdOnNetwork = true;
		}
	}
	
	public final Manager<?> getManager() { return manager; }
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
	public final boolean isRemoved() { return removed; }
	public final boolean owned() { return owned; }
	
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
	
	public void draw() {
		draw(manager.gameBoard);
	}
	
	public void draw(GameBoard gameBoard) {
		if (!visible)
			return;

		int x = this.x.get().intValue();
		int y = this.y.get().intValue();
		if ((angle.get().intValue() - 90) % 360 == 0) {
			int w = (int) (sprite.getWidth() * scaleX.get());
			int h = (int) (sprite.getHeight() * scaleY.get());
			sprite.draw(x, y, w, h, 0, 0, sprite.getWidth(), sprite.getHeight(), gameBoard);
		} else {
			if (scaleX.get() != 1 || scaleY.get() != 1)
				sprite.draw(x, y, angle.get(), scaleX.get(), scaleY.get(), gameBoard);
			else
				sprite.draw(x, y, angle.get(), gameBoard);
		}
	}
	
	public ArrayList<? extends Entity> collided() {
		return collided(manager.getList().size());
	}
	public ArrayList<? extends Entity> collided(int maxReturns) {
		return collidedWith(manager.getList(), maxReturns);
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
	
	public <T extends Entity> boolean collidedWith(T type) {
		if (this.hitbox.intersects(type.hitbox))
			return true;
		return false;
	}
	
	protected void remove_localOnly() {
		manager.remove(this);
		removed = true;
	}
	
	public void remove() {
		remove_localOnly();
		if (createdOnNetwork) {
			manager.getCreator().removeOnNetwork(this);
		}
	}
	
	public MarkupMsg createToMsg() {
		MarkupMsg m = new MarkupMsg();
		m.addAttribute(x);
		m.addAttribute(y);
		m.addAttribute(spriteId);
		return m;
	}
	
	public void performLogic() { }
}
