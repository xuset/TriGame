package triGame.game.entities.building;

import objectIO.markupMsg.MarkupMsg;
import objectIO.markupMsg.MsgAttribute;
import tSquare.game.entity.Entity;
import triGame.game.entities.HealthBar;
import triGame.game.shopping.Upgradable;
import triGame.game.shopping.UpgradeManager;

public abstract class Building extends Entity implements Upgradable{
	protected BuildingManager manager;
	protected UpgradeManager upgrades;
	protected long ownerId;
	protected HealthBar healthBar;
	protected int visibilityRadius;
	
	public abstract String getIdentifier();
	
	public int getVisibilityRadius() { return visibilityRadius; }
	
	public Building(String url, int x, int y, BuildingManager manager, long ownerId, long entityId, int visibilityRadius) {
		super(url, x, y, manager, entityId);
		this.manager = manager;
		this.ownerId = ownerId;
		this.visibilityRadius = visibilityRadius;
		upgrades = new UpgradeManager(manager.getShopManager(), "Building");
		healthBar = new HealthBar(manager.gameBoard, this);
		manager.getSafeBoard().addVisibilityForEntity(this, visibilityRadius);
		manager.objectGrid.turnOnRectangle(x, y, getWidth(), getHeight());
	}

	@Override
	public double modifyHealth(double delta) {
		super.modifyHealth(delta);
		if (getHealth() <= 0) {
			remove();
		}
		return getHealth();
	}

	@Override
	public void draw() {
		super.draw();
		healthBar.drawHealthBar();
	}

	@Override
	public void remove() {
		super.remove();
		manager.getSafeBoard().removeVisibility(this);
		manager.objectGrid.turnOffRectangle(getX(), getY(), getWidth(), getHeight());
	}
	
	public UpgradeManager getUpgradeManager() {
		return upgrades;
	}

	@Override
	public  MarkupMsg createToMsg() {
		MarkupMsg msg = super.createToMsg();
		msg.addAttribute(new MsgAttribute("type").set(getIdentifier()));
		msg.addAttribute(new MsgAttribute("owner").set(ownerId));
		return msg;
	}
}
