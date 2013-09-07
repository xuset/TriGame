package triGame.game.entities;

import java.awt.event.KeyEvent;
import java.util.ArrayList;


import tSquare.game.GameIntegratable;
import tSquare.game.entity.Entity;
import tSquare.system.PeripheralInput;
import triGame.game.entities.building.Building;
import triGame.game.entities.wall.AbstractWall;
import triGame.game.entities.wall.TrapDoor;
import triGame.game.safeArea.SafeAreaBoard;

public class Person extends Entity implements GameIntegratable{
	public static final String SPRITE_ID = "person";
	
	private final int speed = 300;
	private PersonManager manager;
	private PeripheralInput.Keyboard input;
	private HealthBar healthBar;
	
	public Person(int x, int y, PersonManager manager, long id) {
		super(SPRITE_ID, x, y, manager, id);
		this.manager = manager;
		this.input = manager.getInput();
		healthBar = new HealthBar(manager.gameBoard, this);
		super.setHitboxDimensions((getWidth()<=getHeight())? getWidth() : getHeight(), (getWidth()<=getHeight())? getWidth() : getHeight());
	}
	
	public static Person create(int x, int y, PersonManager manager) {
		Person p = new Person(x, y, manager, manager.getUniqueId());
		p.createOnNetwork(true);
		manager.add(p);
		AbstractWall wall = p.collidedWithFirst(manager.getWallManager().getList());
		if (wall != null) //if player spawns inside wall, remove the wall
			wall.remove();
		return p;
	}
	
	public double modifyHealth(double delta) {
		double r = super.modifyHealth(delta);
		if (health <=0) {
			manager.setGameOver();
			remove();
		}
		return r;
	}
	
	private boolean up;
	private boolean down;
	private boolean left;
	private boolean right;
	public void performLogic() {
		if (this.equals(manager.getPlayer())) {
			move();
			/*
			 * All collisions between zombies and persons are handled by the server (in the entities.zombies.Zombie class).
			 * This is fine except for when there is a high network latency.
			 */
			varContainer.update();
		}
	}
	
	public void draw() {
		super.draw();
		healthBar.drawHealthBar();
	}
	
	private void move() {
		up = input.isPressed(KeyEvent.VK_W);
		down = input.isPressed(KeyEvent.VK_S);
		left = input.isPressed(KeyEvent.VK_A);
		right = input.isPressed(KeyEvent.VK_D);
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
			this.moveForward(manager.getDelta() * speed / 1000);
	}
	
	private Entity collidedBuilding;
	private int numOfCollisions;
	public void moveForward(double distance) {
		SafeAreaBoard safeBoard = manager.getSafeAreaBoard();
		x += (Math.cos(Math.toRadians(-angle)) * distance);
		if (this.getCenterX() > manager.gameBoard.getWidth() || this.getCenterX() < 0 || safeBoard.insideSafeArea((int) getCenterX(), (int) getCenterY()) == false)
			x += (Math.cos(Math.toRadians(-angle)) * distance * -1);
		else {
			wallCollisions();
			if (numOfCollisions > 0 && (collidedBuilding.getSpriteId() != TrapDoor.SPRITE_ID || numOfCollisions > 1)) {
				x += (Math.cos(Math.toRadians(-angle)) * distance * -1);
				if (numOfCollisions == 1 && up == false && down == false && collidedBuilding.getSpriteId() != TrapDoor.SPRITE_ID) {
					int direction = 1;
					if (getCenterY() >= collidedBuilding.getCenterY())
						direction = 1;
					else
						direction = -1;
					y += 2 * direction;
					centerHitbox();
					if (this.numberOfCollisions(manager.getWallManager().getList(), 1) > 0 || this.getCenterY() > manager.gameBoard.getHeight() || this.getCenterY() < 0 || safeBoard.insideSafeArea((int) getCenterX(), (int) getCenterY()) == false)
						y += 2 * -direction;
				}
			}
		}
		y += (Math.sin(Math.toRadians(-angle)) * distance);
		if (this.getCenterY() > manager.gameBoard.getHeight() || this.getY() + this.getHeight()/2 < 0 || safeBoard.insideSafeArea((int) getCenterX(), (int) getCenterY()) == false)
			y += (Math.sin(Math.toRadians(-angle)) * distance * -1);
		else {
			wallCollisions();
			if (numOfCollisions > 0 && (collidedBuilding.getSpriteId() != TrapDoor.SPRITE_ID || numOfCollisions > 1)) {
				y += (Math.sin(Math.toRadians(-angle)) * distance * -1);
				if (numOfCollisions == 1 && left == false && right == false && collidedBuilding.getSpriteId() != TrapDoor.SPRITE_ID) {
					int direction = 1;
					if (getCenterX() >= collidedBuilding.getCenterX())
						direction = 1;
					else
						direction = -1;
					x += 2 * direction;
					centerHitbox();
					if (this.numberOfCollisions(manager.getWallManager().getList(), 1) > 0 || this.getCenterX() > manager.gameBoard.getWidth() || this.getCenterX() < 0 || safeBoard.insideSafeArea((int) getCenterX(), (int) getCenterY()) == false)
						x += 2 * -direction;
				}
			}
		}
		setX(x);
		setY(y);
	}
	
	/*private void wallCollisions() {
		if (buildings == null)
			buildings = manager.getBuildingManager().getList();
		centerHitbox();
		collidedBuilding = null;
		int hits = 0;
		for (int i = 0; i < buildings.size() && hits < 2; i++) {
			Building b = buildings.get(i);
			if (b.collidable == true && b.hitbox.intersects(this.hitbox)) {
				hits++;
				if (hits == 1)
					collidedBuilding = b;
			}
		}
		numOfCollisions = hits;
	}*/
	
	private void wallCollisions() {
		centerHitbox();
		collidedBuilding = null;
		ArrayList<Building> bList = this.collidedWith(manager.getBuildingManager().getList(), 2);
		int hits = bList.size();
		if (bList.size() < 2) {
			ArrayList<AbstractWall> wList = this.collidedWith(manager.getWallManager().getList(), 2 - hits);
			hits += wList.size();
			if (wList.isEmpty() == false) {
				collidedBuilding = wList.get(0);
			}
		}
		if (bList.isEmpty() == false) {
			collidedBuilding = bList.get(0);
		}
		numOfCollisions = hits;
	}
	
	public String createToString() {
		return (int) x + ":" + (int) y;
	}
}
