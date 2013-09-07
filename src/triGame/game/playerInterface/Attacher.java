package triGame.game.playerInterface;


import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

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
	private GameBoard gameBoard;
	private PeripheralInput.Mouse mouse;
	private UpgradePanel upgradePanel;
	private GunPanel gunPanel;
	private BuildingManager buildingManager;
	private WallManager wallManager;
	//private ShopManager shopManager;
	
	//private ShopItem shopItem;
	public boolean attached = false;
	public String spriteId = "";
	public boolean dragged = false;
	
	public Attacher(TriGame game, UpgradePanel upgradePanel, GunPanel gunPanel) {
		this.gameBoard = game.getGameBoard();
		this.mouse = game.getInput().mouse;
		this.upgradePanel = upgradePanel;
		this.gunPanel = gunPanel;
		this.buildingManager = game.buildingManager;
		this.wallManager = game.wallManager;
		//this.shopManager = game.shop;
	}
	
	public void setAttached(ShopItem shopItem, String spriteId, boolean dragged) {
		//this.shopItem = shopItem;
		this.attached = true;
		this.spriteId = spriteId;
		this.dragged = dragged;
	}
	
	public void unsetAttached() {
		//shopItem = null;
		attached = false;
		spriteId = null;
		dragged = false;
	}
	
	public void draw() {
		if (attached) {
			gameBoard.getGraphics().setColor(Color.white);
			int mouseX = roundX(mouse.x + gameBoard.viewable.getX());
			int mouseY = roundY(mouse.y + gameBoard.viewable.getY());
			BufferedImage image = Sprite.get(spriteId).getImage();
			gameBoard.draw(image, mouseX, mouseY);
			gameBoard.getGraphics().drawRect(mouseX - (int) gameBoard.viewable.getX(), mouseY - (int) gameBoard.viewable.getY(), image.getWidth(), image.getHeight());
			
		}
	}
	
	private int roundX(double x) {
		return ((int) x / TriGame.BLOCK_WIDTH) * TriGame.BLOCK_WIDTH;
	}
	
	private int roundY(double y) {
		return ((int) y / TriGame.BLOCK_HEIGHT) * TriGame.BLOCK_HEIGHT;
	}
	
	public void dettach() {
		attached = false;
		dragged = false;
	}
		
	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			gunPanel.unselectAllItems();
			if (attached == true) {
				double x1 = mouse.x + gameBoard.viewable.getX();
				double y1 = mouse.y + gameBoard.viewable.getY();
				Sprite s = Sprite.get(spriteId);
				if (s != null) {
					int width = s.getImage().getWidth();
					int height = s.getImage().getHeight();
					if (buildingManager.objectGrid.isRectangleOpen((int) x1,(int) y1, width, height)) {
						int x = roundX(mouse.x + gameBoard.viewable.getX());
						int y = roundY(mouse.y + gameBoard.viewable.getY());
						if (spriteId == Barrier.SPRITE_ID) {
							wallManager.purchaseBarrier(x, y);
						}
						if (spriteId == TrapDoor.SPRITE_ID) {
							wallManager.purchaseTrapDoor(x, y);
						}
						if (spriteId == Tower.SPRITE_ID) {
							buildingManager.purchaseTower(x, y);
						}
						if (spriteId == SmallTower.SPRITE_ID) {
							buildingManager.purchaseSmallTower(x, y);
						}
						if (dragged)
							attached = false;
					}
				}
			} else if (attached == false){
				ArrayList<Building> walls = buildingManager.getList();
				Building w = null;
				for (int i = 0; w == null && i < walls.size(); i++) {
					if (walls.get(i).hitbox.contains(mouse.x + gameBoard.viewable.getX(), mouse.y + gameBoard.viewable.getY())) {
						w = walls.get(i);
					}
				}
				if (w != null) {
					upgradePanel.setUpgradeManager(w.getUpgradeManager(), w.getSpriteId());
				} else {
					upgradePanel.clearUpgradeManager();
				}
			}
		}
	}	
}
