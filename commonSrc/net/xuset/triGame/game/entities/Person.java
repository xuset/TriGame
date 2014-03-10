package net.xuset.triGame.game.entities;

import java.util.ArrayList;

import net.xuset.objectIO.netObject.NetVar;
import net.xuset.objectIO.netObject.ObjControllerI;
import net.xuset.tSquare.game.GameIntegratable;
import net.xuset.tSquare.game.entity.Entity;
import net.xuset.tSquare.game.entity.EntityKey;
import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.TsColor;
import net.xuset.triGame.game.GameGrid;
import net.xuset.triGame.game.ManagerService;
import net.xuset.triGame.game.SafeBoard;
import net.xuset.triGame.game.entities.buildings.Building;
import net.xuset.triGame.game.entities.dropPacks.DropPack;
import net.xuset.triGame.game.ui.gameInput.IPlayerInput;



public class Person extends Entity implements GameIntegratable{
	public static final String SPRITE_ID = "person";
	private static final int maxHealth = 100;
	
	private final GameGrid gameGrid;
	private final TriangleSpriteCreator triangleCreator;
	private final int speed = 6; //blocks per second
	private final ManagerService managers;
	private final SafeBoard safeBoard;
	private final IPlayerInput playerInput;
	private final HealthBar healthBar;
	private NetVar.nLong ownerId;
	private NetVar.nInt color;
	private boolean createdSprite = false;
	
	private double realSpeed = 0;
	private boolean moved = false;
	
	long getOwnerId() { return ownerId.get(); }
	
	public boolean isDead() { return getHealth() <= 0 || removeRequested(); }
	public boolean didMove() { return moved; }
	
	Person(double x, double y, EntityKey key, ManagerService managers,
			SafeBoard safeBoard, IPlayerInput playerInput,
			long ownerIdL, boolean isServer, GameGrid gameGrid,
			TriangleSpriteCreator triangleCreator) {
		
		super(SPRITE_ID, x, y, key);
		this.gameGrid = gameGrid;
		this.managers = managers;
		this.safeBoard = safeBoard;
		this.playerInput = playerInput;
		this.triangleCreator = triangleCreator;
		healthBar = new HealthBar(this);
		
		if (owned()) {
			ownerId.set(ownerIdL);
			if (isServer)
				color.set(TsColor.cyan.getRGBA());
			else
				color.set(getRandomColor());
		}
	}
	
	@Override
	protected void setNetObjects(ObjControllerI objClass) {
		super.setNetObjects(objClass);

		ownerId = new NetVar.nLong(0l, "ownerId", objClass);
		color = new NetVar.nInt(0, "color", objClass);
	}

	public void giveFullHealth() {
		double toAdd = maxHealth - getHealth();
		modifyHealth(toAdd);
	}
	
	public void freeze(double speedDelta) {
		realSpeed = speed * speedDelta;
	}

	@Override
	public void update(int frameDelta) {
		if (!createdSprite) {
			sprite = triangleCreator.createTriangle(color.get().intValue());
			createdSprite = true;
		}
		moved = false;
		if (owned() && !isDead()) {
			if (!safeBoard.insideSafeArea(getCenterX(), getCenterY())) {
				moveToSafeArea(frameDelta);
				return;
			}
			
			move(frameDelta);
			pickupDropPacks();
		}
	}

	@Override
	public void draw(IGraphics g) {
		if (isDead())
			return;
		super.draw(g);
		healthBar.draw(g);
	}
	
	//TODO clean up this horrid mess that has been here since alpha.
	
	private Entity collidedBuilding;
	private int numOfCollisions;
	@Override
	public void moveForward(double distance) {
		setX(getX() + Math.cos(getAngle()) * distance);
		if (this.getCenterX() > gameGrid.getGridWidth() || this.getCenterX() < 0 || safeBoard.insideSafeArea(getCenterX(), getCenterY()) == false)
			setX(getX() + Math.cos(getAngle()) * distance * -1);
		else {
			buildingCollisions();
			if (numOfCollisions > 0) {
				setX(getX() + Math.cos(getAngle()) * distance * -1);
				if (numOfCollisions == 1 && Math.abs(Math.sin(getAngle())) < 0.1) {
					int direction = 1;
					if (getCenterY() >= collidedBuilding.getCenterY())
						direction = 1;
					else
						direction = -1;
					setY(getY() + distance * direction);
					if (this.numberOfCollisions(managers.building.list, 1) > 0 || this.getCenterY() > gameGrid.getGridHeight() || this.getCenterY() < 0 || safeBoard.insideSafeArea(getCenterX(), getCenterY()) == false)
						setY(getY() + distance * -direction);
				}
			}
		}
		setY(getY() - Math.sin(getAngle()) * distance);
		if (this.getCenterY() > gameGrid.getGridHeight() || this.getY() + this.getHeight()/2 < 0 || safeBoard.insideSafeArea(getCenterX(), getCenterY()) == false)
			setY(getY() - Math.sin(getAngle()) * distance * -1);
		else {
			buildingCollisions();
			if (numOfCollisions > 0) {
				setY(getY() - Math.sin(getAngle()) * distance * -1);
				if (numOfCollisions == 1 && Math.abs(Math.cos(getAngle())) < 0.1) {
					int direction = 1;
					if (getCenterX() >= collidedBuilding.getCenterX())
						direction = 1;
					else
						direction = -1;
					setX(getX() + distance * direction);
					if (this.numberOfCollisions(managers.building.list, 1) > 0 || this.getCenterX() > gameGrid.getGridWidth() || this.getCenterX() < 0 || safeBoard.insideSafeArea(getCenterX(), getCenterY()) == false)
						setX(getX() + distance * -direction);
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
		return TsColor.rgb(r, g, b);
	}
	
	private void moveToSafeArea(int frameDelta) {
		double moveToX = gameGrid.getGridWidth() / 2;
		double moveToY = gameGrid.getGridHeight() / 2;
		Building hq = managers.building.getBuildingGetter().getHQ();
		if (hq != null) {
			moveToX = hq.getX();
			moveToY = hq.getY();
		}
		
		turn(moveToX, moveToY);
		moveForward(8 * frameDelta / 1000.0);
	}
	
	/*private void move(int frameDelta) {
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
	}*/
	
	private void move(int frameDelta) {
		//TODO maybe be concerned about the constant setting and changing of player's angle
		setAngle(playerInput.getMoveAngle());
		moveForward(frameDelta * playerInput.getMoveCoEfficient() * realSpeed / 1000.0);
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
