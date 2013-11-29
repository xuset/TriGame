package triGame.game.ui;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import tSquare.game.GameBoard.ViewRect;
import tSquare.game.GameIntegratable;
import tSquare.imaging.Sprite;
import tSquare.system.PeripheralInput;
import triGame.game.ManagerService;
import triGame.game.Params;
import triGame.game.entities.LocManCreator;
import triGame.game.entities.buildings.Building;
import triGame.game.shopping.ShopItem;
import triGame.game.shopping.ShopManager;

public class Attacher extends MouseAdapter implements GameIntegratable{
	private final PeripheralInput.Mouse mouse;
	private final UserInterface ui;
	private final ManagerService managers;
	private final ShopManager shop;
	
	private Building selected;
	private ViewRect rect;
	private boolean dragged = false;
	private AttachedItem attached = null;
	private boolean mouseClicked = false;
	
	
	public void setAttached(AttachedItem attached, boolean dragged) {
		this.attached = attached;
		this.dragged = dragged;
	}
	
	public void clearAttached() {
		attached = null;
		dragged = false;
	}
	
	public boolean isAttached() { return attached != null; }
	public AttachedItem getAttached() { return attached; }
	
	public static class AttachedItem {
		private BufferedImage image = null;
		public int visibilityRadius = 0;
		public ShopItem shopItem = null;
		public LocManCreator<?> creator = null;
		
		public AttachedItem(BufferedImage img, int radius, ShopItem item, LocManCreator<?> c) {
			image = img;
			visibilityRadius = radius;
			shopItem = item;
			creator = c;
		}
	}
	
	public Attacher(ManagerService service, UserInterface ui,
			ShopManager shop,PeripheralInput.Mouse mouse) {
		
		this.mouse = mouse;
		this.ui = ui;
		managers = service;
		this.shop = shop;
	}

	@Override
	public void draw(Graphics2D g, ViewRect rect) {
		this.rect = rect;
		if (isAttached()) {
			int boardX = Params.roundToGrid(mouse.x + rect.getX());
			int boardY = Params.roundToGrid(mouse.y + rect.getY());
			int graphicX = boardX - (int) rect.getX();
			int graphicY = boardY - (int) rect.getY();
			int width = attached.image.getWidth();
			int height = attached.image.getHeight();
			
			g.drawImage(attached.image, graphicX, graphicY, null);
			
			Color color;
			if (shop.canPurchase(attached.shopItem) &&
					attached.creator.isValidLocation(boardX, boardY))
				color = Color.green;
			else
				color = Color.red;
			g.setColor(color);
			
			g.drawRect(graphicX, graphicY, attached.image.getWidth(), attached.image.getHeight());
			drawRadius(graphicX + width/2, graphicY + height/2, attached.visibilityRadius, color, g);
		} else if (selected != null) {
			int x = (int) (selected.getCenterX() - rect.getX());
			int y = (int) (selected.getCenterY() - rect.getY());
			drawRadius(x, y, selected.getVisibilityRadius(), Color.green, g);
		}
	}
	
	private void drawRadius(int x, int y, int radius, Color c, Graphics2D g) {
		if (radius > 0) {
			g.setColor(c);
			g.drawOval(x - radius, y - radius, radius * 2, radius * 2);
		}
	}
		
	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1)
			mouseClicked = true;
		else
			mouseClicked = false;
	}
	
	@Override
	public void performLogic(int frameDelta) {
		if (mouseClicked) {
			selected = null;
			mouseClicked = false;
			if (isAttached())
				placeAttached();
			else
				selectBuilding();
		}
	}
	
	private void placeAttached() {
		int x = Params.roundToGrid(mouse.x + rect.getX());
		int y = Params.roundToGrid(mouse.y + rect.getY());
		if (attached.creator.isValidLocation(x, y)) {
			if (shop.purchase(attached.shopItem))
				attached.creator.create(x, y);
			if (dragged)
				clearAttached();
		} else {
			clearAttached();
		}
	}
	
	private void selectBuilding() {
		boolean found = false;
		for (Building b : managers.building.list) {
			if (b.isUpgradable() && b.owned() &&
					b.hitbox.contains(mouse.x + rect.getX(), mouse.y + rect.getY())) {
				
				ui.upgrades.set(b.upgrades, Sprite.get(b.getSpriteId()).getImage());
				ui.switchTo(ui.upgrades);
				selected = b;
				found = true;
				break;
			}
		}
		if (found == false && ui.isSelected(ui.upgrades))
			ui.switchTo(ui.arsenal);
	}
}
