package triGame.game.entities;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import objectIO.netObject.NetVar;
import tSquare.game.GameIntegratable;
import tSquare.game.GameBoard.ViewRect;
import tSquare.game.entity.Entity;
import tSquare.game.entity.EntityKey;
import tSquare.system.PeripheralInput;
import triGame.game.ManagerService;
import triGame.game.Params;
import triGame.game.entities.buildings.Building;
import triGame.game.entities.buildings.types.TrapDoor;
import triGame.game.safeArea.SafeAreaBoard;

public class Person extends Entity implements GameIntegratable{
	public static final String SPRITE_ID = "person";
	private static final int maxHealth = 100;
	
	private final int speed = 300;
	private final ManagerService managers;
	private final SafeAreaBoard safeBoard;
	private final PeripheralInput.Keyboard keyboard;
	private final HealthBar healthBar;
	private final NetVar.nLong ownerId;
	
	private int freezeSpeed = 0;
	
	long getOwnerId() { return ownerId.get(); }
	
	Person(double x, double y, EntityKey key, ManagerService managers,
			SafeAreaBoard safeBoard, PeripheralInput.Keyboard keyboard, long ownerIdL) {
		
		super(SPRITE_ID, x, y, key);
		this.managers = managers;
		this.safeBoard = safeBoard;
		this.keyboard = keyboard;
		healthBar = new HealthBar(this);
		ownerId = new NetVar.nLong(0l, "ownerId", objClass);
		if (owned())
			ownerId.set(ownerIdL);
			
			
	}
	
	private boolean up;
	private boolean down;
	private boolean left;
	private boolean right;
	@Override
	public void performLogic(int frameDelta) {
		if (health.get() <= 0)
			remove();
		if (owned() && !removeRequested()) {
			move(frameDelta);
			double pickedUpHealth = managers.healthPack.grabHealth(attackbox);
			if (pickedUpHealth > 0) {
				double max = maxHealth - getHealth();
				if (pickedUpHealth > max)
					pickedUpHealth = max;
				modifyHealth(pickedUpHealth);
			}
				
		}
	}

	@Override
	public void draw(Graphics2D g, ViewRect rect) {
		super.draw(g, rect);
		healthBar.draw(g, rect);
	}
	
	private void move(int frameDelta) {
		up = keyboard.isPressed(KeyEvent.VK_W);
		down = keyboard.isPressed(KeyEvent.VK_S);
		left = keyboard.isPressed(KeyEvent.VK_A);
		right = keyboard.isPressed(KeyEvent.VK_D);
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
		if (up || down || left || right)
			moveForward((speed - freezeSpeed) * frameDelta / 1000);
		freezeSpeed = 0;
	}
	
	private Entity collidedBuilding;
	private int numOfCollisions;
	public void moveForward(double distance) {
		setX(getX() + Math.cos(Math.toRadians(-getAngle())) * distance);
		if (this.getCenterX() > Params.GAME_WIDTH || this.getCenterX() < 0 || safeBoard.insideSafeArea((int) getCenterX(), (int) getCenterY()) == false)
			setX(getX() + Math.cos(Math.toRadians(-getAngle())) * distance * -1);
		else {
			buildingCollisions();
			if (numOfCollisions > 0 && (collidedBuilding.getSpriteId() != TrapDoor.INFO.spriteId || numOfCollisions > 1)) {
				setX(getX() + Math.cos(Math.toRadians(-getAngle())) * distance * -1);
				if (numOfCollisions == 1 && up == false && down == false && collidedBuilding.getSpriteId() != TrapDoor.INFO.spriteId) {
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
			if (numOfCollisions > 0 && (collidedBuilding.getSpriteId() != TrapDoor.INFO.spriteId || numOfCollisions > 1)) {
				setY(getY() + Math.sin(Math.toRadians(-getAngle())) * distance * -1);
				if (numOfCollisions == 1 && left == false && right == false && collidedBuilding.getSpriteId() != TrapDoor.INFO.spriteId) {
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
	
	private void buildingCollisions() {
		collidedBuilding = null;
		ArrayList<Building> bList = this.collidedWith(managers.building.list, 2);
		int hits = bList.size();
		if (bList.isEmpty() == false) {
			collidedBuilding = bList.get(0);
		}
		numOfCollisions = hits;
	}
	
	public void freeze(int speedDelta) {
		freezeSpeed = speedDelta;
	}
}