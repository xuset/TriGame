package triGame.game.ui;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import tSquare.game.GameBoard;
import tSquare.imaging.Sprite;
import tSquare.system.PeripheralInput;
import triGame.game.TriGame;
import triGame.game.entities.building.Building;
import triGame.game.entities.building.BuildingManager;
import triGame.game.entities.building.SmallTower;
import triGame.game.entities.building.Tower;
import triGame.game.entities.wall.Barrier;
import triGame.game.entities.wall.TrapDoor;
import triGame.game.entities.wall.WallManager;
import triGame.game.shopping.ShopItem;

public class Attacher extends MouseAdapter{
	private PeripheralInput.Mouse mouse;
	private UserInterface ui;
	private Building selected;
	private TriGame game;
	
	private GameBoard gameBoard() { return game.getGameBoard(); }
	private WallManager wallManager() { return game.wallManager; }
	private BuildingManager buildingManager() { return game.buildingManager; }
	
	public int radius = 0;
	public ShopItem shopItem;
	public boolean attached = false;
	public BufferedImage image;
	public boolean dragged = false;
	
	public Attacher(TriGame game, UserInterface ui) {
		this.game = game;
		this.mouse = game.getInput().mouse;
		this.ui = ui;
	}
	
	public void setAttached(ShopItem shopItem, BufferedImage image, boolean dragged) {
		setAttached(shopItem, image, dragged, 0);
	}
	
	public void setAttached(ShopItem shopItem, BufferedImage image, boolean dragged, int radius) {
		this.radius = radius;
		this.shopItem = shopItem;
		this.attached = true;
		this.image = image;
		this.dragged = dragged;
	}
	
	public void unsetAttached() {
		shopItem = null;
		image = null;
		attached = false;
		dragged = false;
		radius = 0;
	}

	public void draw() {
		if (attached) {
			int boardX = roundX(mouse.x + gameBoard().viewable.getX());
			int boardY = roundY(mouse.y + gameBoard().viewable.getY());
			int graphicX = boardX - (int) gameBoard().viewable.getX();
			int graphicY = boardY - (int) gameBoard().viewable.getY();
			Graphics g = gameBoard().getGraphics();
			gameBoard().getGraphics().drawImage(image, graphicX, graphicY, null);
			g.setColor(Color.white);
			g.drawRect(graphicX, graphicY, image.getWidth(), image.getHeight());
			drawRadius(graphicX, graphicY, radius);
		} else if (selected != null) {
			drawRadius((int) (selected.getX() - gameBoard().viewable.getX()), (int) (selected.getY() - gameBoard().viewable.getY()), selected.getVisibilityRadius());
		}
	}
	
	private void drawRadius(int x, int y, int radius) {
		if (radius > 0) {
			Graphics g = gameBoard().getGraphics();
			g.setColor(Color.green);
			g.drawOval(x - radius, y - radius, radius * 2, radius * 2);
		}
	}
	
	private int roundX(double x) {
		return ((int) x / TriGame.BLOCK_WIDTH) * TriGame.BLOCK_WIDTH;
	}
	
	private int roundY(double y) {
		return ((int) y / TriGame.BLOCK_HEIGHT) * TriGame.BLOCK_HEIGHT;
	}
		
	public void mouseClicked(MouseEvent e) {
		selected = null;
		if (e.getButton() == MouseEvent.BUTTON1) {
			if (attached == true) {
				int x = roundX(mouse.x + gameBoard().viewable.getX());
				int y = roundX(mouse.y + gameBoard().viewable.getY());
				int width = image.getWidth();
				int height = image.getHeight();
				if (buildingManager().objectGrid.isRectangleOpen(x, y, width, height) &&
						wallManager().objectGrid.isRectangleOpen(x, y, width, height)) {
					if (shopItem == Barrier.NEW_BARRIER) {
						if (wallManager().purchaseBarrier(x, y) == null)
							unsetAttached();
					}
					if (shopItem == TrapDoor.NEW_TRAP_DOOR) {
						if (wallManager().purchaseTrapDoor(x, y) == null)
							unsetAttached();
					}
					if (shopItem == Tower.NEW_TOWER) {
						if (buildingManager().purchaseTower(x, y) == null)
							unsetAttached();
					}
					if (shopItem == SmallTower.NEW_TOWER) {
						if (buildingManager().purchaseSmallTower(x, y) == null)
							unsetAttached();
					}
					if (dragged)
						attached = false;
				} else {
					unsetAttached();
				}
				
			} else if (attached == false){
				boolean found = false;
				for (Building b : buildingManager().getList()) {
					if (b.owned() && b.hitbox.contains(mouse.x + gameBoard().viewable.getX(), mouse.y + gameBoard().viewable.getY())) {
						ui.upgrades.set(b.getUpgradeManager(), Sprite.get(b.getSpriteId()).getImage());
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
	}	
}
