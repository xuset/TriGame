package triGame.game.entities.projectiles;

import objectIO.markupMsg.MarkupMsg;
import objectIO.markupMsg.MsgAttribute;
import tSquare.game.entity.CreationHandler;
import tSquare.game.entity.Creator;
import tSquare.game.entity.EntityKey;
import tSquare.game.entity.Manager;

class ProjectileCreator extends Creator<Projectile> {
	public static interface ICreate { Projectile create(
			String spriteId, int x, int y,
			double angle, int speed, int damage,
			boolean noBuildingCollisions, EntityKey key); }
	
	public final static String HASH_MAP_KEY = "projectile";
	
	private final ICreate iCreate;

	public ProjectileCreator(CreationHandler handler, ICreate iCreate) {
		super(HASH_MAP_KEY, handler);
		this.iCreate = iCreate;
	}
	
	public Projectile create(String spriteId, int x, int y, double angle,
			int speed, int damage, boolean noBuildingCollisions,
			Manager<Projectile> manager) {
		
		EntityKey key = getNewKey(manager);
		MarkupMsg msg = new MarkupMsg();
		msg.addAttribute(new MsgAttribute("spriteId").set(spriteId));
		msg.addAttribute(new MsgAttribute("x").set(x));
		msg.addAttribute(new MsgAttribute("y").set(y));
		msg.addAttribute(new MsgAttribute("angle").set(angle));
		msg.addAttribute(new MsgAttribute("speed").set(speed));
		msg.addAttribute(new MsgAttribute("damage").set(damage));
		msg.addAttribute(new MsgAttribute("building collisions").set(noBuildingCollisions));
		networkCreate(key, msg);
		Projectile p = iCreate.create(spriteId, x, y, angle, speed,
				damage, noBuildingCollisions, key);
		localCreate(p, manager);
		return p;
	}

	@Override
	protected Projectile parseMsg(MarkupMsg msg, EntityKey key) {
		String spriteId = msg.getAttribute("spriteId").getString();
		int x = msg.getAttribute("x").getInt();
		int y = msg.getAttribute("y").getInt();
		double angle = msg.getAttribute("angle").getDouble();
		int speed = msg.getAttribute("speed").getInt();
		int damage = msg.getAttribute("damage").getInt();
		boolean bc = msg.getAttribute("building collisions").getBool();
		Projectile p = iCreate.create(spriteId, x, y, angle, speed, damage, bc, key);
		return p;
	}

}
