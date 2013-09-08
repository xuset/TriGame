package tSquare.game.entity;


import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;

import objectIO.connection.Connection;
import objectIO.netObject.NetClass;
import objectIO.netObject.NetVar;
import tSquare.game.GameBoard;
import tSquare.game.GameIntegratable;
import tSquare.game.Manager;
import tSquare.imaging.Sprite;
import tSquare.math.Point;


public class Entity implements GameIntegratable{
	private boolean removed = false;
	
	protected NetClass netClass;
	protected VarContainer varContainer;
	protected Manager<? extends Entity> manager;
	protected Sprite sprite;
	protected double x = 0;
	protected double y = 0;
	protected int width = 0;
	protected int height = 0;
	protected String spriteId;
	protected double angle = 90;
	protected Long id;
	protected boolean owned = true;
	protected boolean createdOnNetwork = false;
	protected double health = 100;
	
	public Rectangle hitbox = new Rectangle();
	public boolean visible = true;
	public boolean collidable = true;
	
	protected Entity(String spriteId, int startX, int startY, Manager<?> manager, long id) {
		this.spriteId = spriteId;
		this.x = startX;
		this.y = startY;
		this.lastX = (int) startX;
		this.lastY = (int) startY;
		this.manager = manager;
		this.id = id;
		this.sprite = Sprite.add(spriteId);
		this.width = this.sprite.width;
		this.height = this.sprite.height;
		this.setHitboxDimensions(this.width, this.height);
	}
	
	protected class VarContainer {
		public static final String X_ID = "x";
		public static final String Y_ID = "y";
		public static final String ANGLE_ID = "a";
		public static final String HEALTH_ID = "h";
		
		public NetVar x = new NetVar(netClass, X_ID);
		public NetVar y = new NetVar(netClass, Y_ID);
		public NetVar angle = new NetVar(netClass, ANGLE_ID);
		public NetVar health = new NetVar(netClass, HEALTH_ID);
		
		public void remove() {
			netClass.remove();
		}
		
		public void update() {
			x.update();
			y.update();
			angle.update();
			health.update();
			netClass.update();
		}
		
		public VarContainer() {
			x.onChangeEvent = new EntityOnChange() {
				public void onChange(NetVar v, Connection c) {
					Entity.this.x = v.getToDouble();
					centerHitbox();
				}
			};
			
			y.onChangeEvent = new EntityOnChange() {
				public void onChange(NetVar v, Connection c) {
					Entity.this.y = v.getToDouble();
					centerHitbox();
				}
			};
			
			angle.onChangeEvent = new EntityOnChange() {
				public void onChange(NetVar v, Connection c) {
					Entity.this.angle = v.getToDouble();
				}
			};
			
			health.onChangeEvent = new EntityOnChange() {
				public void onChange(NetVar v, Connection c) {
					Entity.this.health = v.getToDouble();
				}
			};
		}
	}
	
	protected Entity(String spriteId, int x, int y, Manager<?> manager) {
		this(spriteId, x, y, manager, manager.getUniqueId());
	}
	
	public Entity(String spriteId, int x, int y) {
		this(spriteId, x, y, null, -1l);
	}
	
	public static Entity create(String spriteId, int x, int y, Manager<Entity> manager) {
		Entity e = new Entity(spriteId, x, y, manager);
		e.createOnNetwork(true);
		manager.add(e);
		return e;
	}
	
	public void reload() {
		this.sprite = Sprite.add(spriteId);
		this.width = this.sprite.width;
		this.height = this.sprite.height;
		this.setHitboxDimensions(this.width, this.height);
	}
	
	public void updateOnNetwork() {
		varContainer.update();
	}
	
	boolean allowUpdates = false;
	protected void createOnNetwork(boolean allowUpdates) {
		if (!createdOnNetwork && manager != null) {
			this.allowUpdates = allowUpdates;
			if (allowUpdates) {
				netClass = new NetClass(manager.getNetwork().getObjController(), String.valueOf(id), 4);
				varContainer = new VarContainer();
			}
			manager.getEntityCreater().createOnNetwork(this);
			createdOnNetwork = true;
		}
	}
	
	void createUpdateVars() {
		netClass = new NetClass(manager.getNetwork().getObjController(), String.valueOf(id), 4);
		varContainer = new VarContainer();
	}
	
	public boolean owned() { return owned; }
	public Manager<?> getManager() { return manager; }
	public String getSpriteId() { return sprite.getUrl(); }
	public double getAngle() { return angle; }
	public double getX() { return x; }
	public double getY() { return y; }
	public int getIntX() { return (int) x; }
	public int getIntY() { return (int) y; }
	public Point getLocation() { return new Point(x, y); }
	public int getWidth() { return sprite.width; }
	public int getHeight() { return sprite.height; }
	public double getCenterX() { return x + width/2; }
	public double getCenterY() { return y + height/2; }
	public Point getCenter() { return new Point(x + width/2, y + height/2); }
	public long getId() { return id; }
	public double getHealth() { return health; }
	public boolean isRemoved() { return removed; }
	
	public void setAngle(double degrees) {
		this.angle = degrees;
		if (varContainer != null)
			varContainer.angle.set(degrees);
	}
	
	public void setX(double x) {
		setLocation(x, this.y);
	}
	public void setY(double y) {
		setLocation(this.x, y);
	}
	public void setLocation(Point p) {
		setLocation(p.x, p.y);
	}
	public void setCenter(Point p) {
		setCenter(p.x, p.y);
	}
	public void setCenter(double x, double y) {
		setLocation(x - width/2, y - height/2);
	}
	public void moveForward(double distance) {
		double newX = x + (Math.cos(Math.toRadians(-angle)) * distance);
		double newY = y +  (Math.sin(Math.toRadians(-angle)) * distance);
		setLocation(newX, newY);
	}
	private int lastX;
	private int lastY;
	public void setLocation(double x, double y) {
		this.x = x;
		this.y = y;
		centerHitbox();
		if ((lastX != (int) x || lastY != (int) y) && varContainer != null) {
			lastX = (int) x;
			lastY = (int) y;
			varContainer.x.set(lastX);
			varContainer.y.set(lastY);
		}
	}
	
	public void moveForward(double speed, int time) {
		
	}
	
	public void turn(Point p) {
		setAngle(Point.degrees(this.x + this.width/2, this.y + this.height/2, p.x, p.y));
	}
	public void turn(double x, double y) {
		setAngle(Point.degrees(this.x + this.width/2, this.y + this.height/2, x, y));
	}
	
	public double modifyHealth(double delta) {
		health += delta;
		if (varContainer != null)
			varContainer.health.set(health);
		return health;
	}
	
	public void draw() {
		draw(manager.gameBoard);
	}
	
	public void draw(GameBoard gameBoard) {
		if (visible) {
			if (((int) this.angle - 90) % 360 == 0)
				this.sprite.draw((int) x, (int) y, gameBoard);
			else
				this.sprite.draw((int) x, (int) y, angle, gameBoard);
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
			manager.getEntityCreater().removeOnNetwork(this);
			if (varContainer != null)
				varContainer.remove();
		}
	}
	
	protected void centerHitbox() {
		hitbox.x = (int) x + width/2 - hitbox.width/2;
		hitbox.y = (int) y + height/2 - hitbox.height/2;
	}
	
	public void setHitboxDimensions(int width, int height) {
		hitbox.width = width;
		hitbox.height = height;
		centerHitbox();
	}
	
	public boolean setManager(Manager<? extends Entity> manager) {
		if (this.manager == null) {
			this.manager = manager;
			return true;
		}
		return false;
	}
	
	public String createToString() {
		return spriteId + ":" + (int) x + ":" + (int) y;
	}
	
	public void performLogic() { }
}
