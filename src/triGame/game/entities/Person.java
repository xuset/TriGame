package triGame.game.entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import objectIO.connections.Connection;
import objectIO.netObject.NetVar;
import objectIO.netObject.ObjControllerI;
import tSquare.game.GameIntegratable;
import tSquare.game.GameBoard.ViewRect;
import tSquare.game.entity.Entity;
import tSquare.game.entity.EntityKey;
import tSquare.imaging.Sprite;
import tSquare.math.Point;
import tSquare.system.PeripheralInput;
import triGame.game.Load;
import triGame.game.ManagerService;
import triGame.game.Params;
import triGame.game.SafeBoard;
import triGame.game.entities.buildings.Building;
import triGame.game.entities.dropPacks.DropPack;

public class Person extends Entity implements GameIntegratable{
	public static final String SPRITE_ID = "person";
	private static final int maxHealth = 100;
	
	private final int speed = 300;
	private final ManagerService managers;
	private final SafeBoard safeBoard;
	private final PeripheralInput.Keyboard keyboard;
	private final HealthBar healthBar;
	private NetVar.nLong ownerId;
	private NetVar.nInt color;
	
	private double realSpeed = 0;
	private boolean moved = false;
	
	long getOwnerId() { return ownerId.get(); }
	
	public boolean isDead() { return getHealth() <= 0 || removeRequested(); }
	public boolean didMove() { return moved; }
	
	Person(double x, double y, EntityKey key, ManagerService managers,
			SafeBoard safeBoard, PeripheralInput.Keyboard keyboard,
			long ownerIdL, boolean isServer) {
		
		super(SPRITE_ID, x, y, key);
		this.managers = managers;
		this.safeBoard = safeBoard;
		this.keyboard = keyboard;
		healthBar = new HealthBar(this);
		
		if (owned()) {
			ownerId.set(ownerIdL);
			if (isServer)
				color.set(Color.cyan.getRGB());
			else
				color.set(getRandomColor());
		}
	}
	
	@Override
	protected void setNetObjects(ObjControllerI objClass) {
		super.setNetObjects(objClass);

		ownerId = new NetVar.nLong(0l, "ownerId", objClass);
		color = new NetVar.nInt(0, "color", objClass);
		color.setEvent(true, new NetVar.OnChange<Integer>() {
			@Override public void onChange(NetVar<Integer> var, Connection c) {
				Color color = new Color(var.get());
				BufferedImage personImage = Load.triangleImage(color);
				sprite = new Sprite("", personImage);
			}
		});
	}

	public void giveFullHealth() {
		double toAdd = maxHealth - getHealth();
		modifyHealth(toAdd);
	}
	
	public void freeze(double speedDelta) {
		realSpeed = speed * speedDelta;
	}
	
	private boolean up;
	private boolean down;
	private boolean left;
	private boolean right;
	@Override
	public void performLogic(int frameDelta) {
		moved = false;
		if (owned() && !isDead()) {
			if (!safeBoard.insideSafeArea((int) getCenterX(), (int) getCenterY())) {
				moveToSafeArea(frameDelta);
				return;
			}
			
			move(frameDelta);
			pickupDropPacks();
		}
	}

	@Override
	public void draw(Graphics2D g, ViewRect rect) {
		if (isDead())
			return;
		super.draw(g, rect);
		healthBar.draw(g, rect);
	}
	
	private Entity collidedBuilding;
	private int numOfCollisions;
	@Override
	public void moveForward(double distance) {
		setX(getX() + Math.cos(Math.toRadians(-getAngle())) * distance);
		if (this.getCenterX() > Params.GAME_WIDTH || this.getCenterX() < 0 || safeBoard.insideSafeArea((int) getCenterX(), (int) getCenterY()) == false)
			setX(getX() + Math.cos(Math.toRadians(-getAngle())) * distance * -1);
		else {
			buildingCollisions();
			if (numOfCollisions > 0) {
				setX(getX() + Math.cos(Math.toRadians(-getAngle())) * distance * -1);
				if (numOfCollisions == 1 && up == false && down == false) {
					int direction = 1;
					if (getCenterY() >= collidedBuilding.getCenterY())
						direction = 1;
					else
						direction = -1;
					setY(getY() + 2 * direction);
					if (this.numberOfCollisions(managers.building.list, 1) > 0 || this.getCenterY() > Params.GAME_HEIGHT || this.getCenterY() < 0 || safeBoard.insideSafeArea((int) getCenterX(), (int) getCenterY()) == false)
						setY(getY() + 2 * -direction);
				}
			}
		}
		setY(getY() + Math.sin(Math.toRadians(-getAngle())) * distance);
		if (this.getCenterY() > Params.GAME_HEIGHT || this.getY() + this.getHeight()/2 < 0 || safeBoard.insideSafeArea((int) getCenterX(), (int) getCenterY()) == false)
			setY(getY() + Math.sin(Math.toRadians(-getAngle())) * distance * -1);
		else {
			buildingCollisions();
			if (numOfCollisions > 0) {
				setY(getY() + Math.sin(Math.toRadians(-getAngle())) * distance * -1);
				if (numOfCollisions == 1 && left == false && right == false) {
					int direction = 1;
					if (getCenterX() >= collidedBuilding.getCenterX())
						direction = 1;
					else
						direction = -1;
					setX(getX() + 2 * direction);
					if (this.numberOfCollisions(managers.building.list, 1) > 0 || this.getCenterX() > Params.GAME_WIDTH || this.getCenterX() < 0 || safeBoard.insideSafeArea((int) getCenterX(), (int) getCenterY()) == false)
						setX(getX() + 2 * -direction);
				}
			}
		}
	}
	
	private void pickupDropPacks() {
		DropPack drop = managers.dropPack.grabAnyPacks(attackbox);
		if (drop != null && drop.isHealthPack()) {
			double pickedUpHealth = drop.pickup();
			double max = maxHealth - getHealth();
			if (pickedUpHealth > max)
				pickedUpHealth = max;
			modifyHealth(pickedUpHealth);
		}
	}
	
	private int getRandomColor() {
		int r = (int) (150 + Math.random() * 100);
		int g = (int) (100 + Math.random() * 150);
		int b = (int) (100 + Math.random() * 150);
		Color myColor = new Color(r, g, b);
		return myColor.getRGB();
	}
	
	private void moveToSafeArea(int frameDelta) {
		Point moveTo = new Point(Params.GAME_WIDTH / 2, Params.GAME_HEIGHT / 2);
		Building hq = managers.building.getHQ();
		if (hq != null)
			moveTo.set(hq.getX(), hq.getY());
		
		turn(moveTo);
		super.moveForward(400 * frameDelta / 1000.0);
	}
	
	private void move(int frameDelta) {
		up = keyboard.isUpPressed();
		down = keyboard.isDownPressed();
		left = keyboard.isLeftPressed();
		right = keyboard.isRightPressed();
		if (up) {
			if (left)
				setAngle(135);
			if (right)
				setAngle(45);
			if (!left && !right)
				setAngle(90);
		} else if (down) {
			if (left)
				setAngle(225);
			if (right)
				setAngle(315);
			if (!left && !right)
				setAngle(270);
		} else if (left && !right) {
			setAngle(180);
		} else if (right && !left) {
			setAngle(0);
		}
		
		if (up || down || left || right) {
			moved = true; //moved is reset to false every tick. 
			moveForward(realSpeed * frameDelta / 1000.0);
		}
		realSpeed = speed;
	}
	
	private void buildingCollisions() {
		collidedBuilding = null;
		ArrayList<Building> bList = this.collidedWith(managers.building.list, 2);
		int hits = bList.size();
		if (bList.isEmpty() == false) {
			collidedBuilding = bList.get(0);
		}
		numOfCollisions = hits;
	}
}
