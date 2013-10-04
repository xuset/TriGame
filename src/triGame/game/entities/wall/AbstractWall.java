package triGame.game.entities.wall;

import objectIO.markupMsg.MarkupMsg;
import objectIO.markupMsg.MsgAttribute;
import tSquare.game.entity.Entity;

public abstract class AbstractWall extends Entity{
	String identifier;
	
	protected WallManager manager;

	public String getIdentifier() {
		return identifier;
	}
	
	public AbstractWall(String spriteId, int x, int y, WallManager manager, long entityId, String identifier) {
		super(spriteId, x, y, manager, entityId);
		this.manager = manager;
		manager.objectGrid.turnOnRectangle(x, y, getWidth(), getHeight());
		this.identifier = identifier;
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
	public void remove() {
		super.remove();
		manager.objectGrid.turnOffRectangle(getX(), getY(), getWidth(), getHeight());
	}

	@Override
	public MarkupMsg createToMsg() {
		MarkupMsg msg = super.createToMsg();
		msg.addAttribute(new MsgAttribute("type").set(getIdentifier()));
		return msg;
	}
}
