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
	protected NetVar.nInt width;
	protected NetVar.nInt height;
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
		width = new NetVar.nInt(sprite.getWidth(), "w", objClass);
		height = new NetVar.nInt(sprite.getHeight(), "h", objClass);
		spriteId = new NetVar.nString(sSpriteId, "spriteId", objClass);
		health = new NetVar.nDouble(100.0, "hlth", objClass);
		hitbox = new CollisionBox(CollisionBox.Type.Hitbox, this);
		attackbox = new CollisionBox(CollisionBox.Type.AttackBox, this);
		x.autoUpdate = y.autoUpdate = angle.autoUpdate = objClass.autoUpdate = true;
		
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
	public final double getAngle() { return angle.get(); }
	public final double getX() { return x.get(); }
	public final double getY() { return y.get(); }
	public final int getIntX() { return x.get().intValue(); }
	public final int getIntY() { return y.get().intValue(); }
	public final Point getLocation() { return new Point(x.get(), y.get()); }
	public final int getWidth() { return width.get(); }
	public final int getHeight() { return height.get(); }
	public final double getCenterX() { return x.get() + width.get()/2; }
	public final double getCenterY() { return y.get() + height.get()/2; }
	public final Point getCenter() { return new Point(x.get() + width.get()/2, y.get() + height.get()/2); }
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
		setLocation(x - width.get()/2, y - height.get()/2);
	}
	public void setCenter(Point p) {
		setCenter(p.x, p.y);
	}
	public void setLocation(double x, double y) {
		this.x.set(x);
		this.y.set(y);
	}
	
	public void turn(double x, double y) {
		setAngle(Point.degrees(this.x.get() + width.get()/2.0, this.y.get() + height.get()/2.0, x, y));
	}
	public void turn(Point p) {
		turn(p.x, p.y);
	}
	
	public double modifyHealth(double delta) {
		health.set(health.get() + delta);
		return health.get();
	}
	
	public void draw() {
		draw(manager.gameBoard);
	}
	
	public void draw(GameBoard gameBoard) {
		if (visible) {
			if ((angle.get().intValue() - 90) % 360 == 0)
				this.sprite.draw(x.get().intValue(), y.get().intValue(), gameBoard);
			else
				this.sprite.draw(x.get().intValue(), y.get().intValue(), angle.get(), gameBoard);
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
