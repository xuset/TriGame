package triGame.game.entities.buildings.types;

import java.awt.Color;
import java.awt.Graphics2D;

import tSquare.game.GameBoard.ViewRect;
import tSquare.game.entity.EntityKey;
import tSquare.math.Point;
import triGame.game.ManagerService;
import triGame.game.entities.Person;
import triGame.game.entities.buildings.Building;
import triGame.game.entities.zombies.Zombie;
import triGame.game.shopping.ShopItem;
import triGame.game.shopping.UpgradeItem;

public class FreezeTower extends Building {
	private static final double circles = 4.0;
	private static final int radiusSpeed = 20;
	private static final Color radiusColor = new Color(184, 24, 210);

	private final ManagerService managers;
	private final UpgradeItem rangeUpgrade;
	private double currentRadius = 1;

	@Override public int getVisibilityRadius() { return rangeUpgrade.getValue(); }

	public FreezeTower(double x, double y, ManagerService managers, EntityKey key) {
		super(INFO.spriteId, x, y, INFO, key);
		this.managers = managers;
		rangeUpgrade = new UpgradeItem(new ShopItem("Range", 100),3, INFO.visibilityRadius, 25);
		upgrades.addUpgrade(rangeUpgrade);
		upgrades.addUpgrade(new UpgradeItem(new ShopItem("Freeze rate", 100),3, 2500, -500));
	}
	
	@Override
	public void performLogic(int frameDelta) {
		currentRadius += frameDelta * radiusSpeed / 1000.0;
		double delta = rangeUpgrade.getValue() / circles;
		if (currentRadius >= delta)
			currentRadius = 1.0;
		
		for (Zombie z : managers.zombie.list) {
			double distance = Point.distance(z.getCenterX(), z.getCenterY(), getCenterX(), getCenterY());
			if (distance < rangeUpgrade.getValue()) {
				z.freeze(30);
			}
		}
		
		for (Person p : managers.person.list) {
			double distance = Point.distance(p.getCenterX(), p.getCenterY(), getCenterX(), getCenterY());
			if (distance < rangeUpgrade.getValue()) {
				p.freeze(30);
			}
		}
	}
	
	@Override
	public void draw(Graphics2D g, ViewRect rect) {
		super.draw(g, rect);
		int x = (int) (getCenterX() - rect.getX());
		int y = (int) (getCenterY() - rect.getY());
		double delta = rangeUpgrade.getValue() / circles;
		g.setColor(radiusColor);
		for (int i = 0; i < circles; i++) {
			int r = (int) (currentRadius + i * delta);
			g.drawOval(x - r, y - r, 2 * r, 2 * r);
		}
		
		
	}
	
	public static final BuildingInfo INFO = new BuildingInfo(
			"media/FreezeTower.png",    //spriteId
			"freezeTower",    //Creator hash map key
			75,        //visibilityRadius
			"Slow down the hoards.",
			new ShopItem("Freeze tower", 200),
			true,    //has a healthBar
			true,    //has an UpgradeManager
			true
	);
}
