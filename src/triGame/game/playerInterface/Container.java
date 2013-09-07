package triGame.game.playerInterface;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import triGame.game.RoundHandler;
import triGame.game.TriGame;
import triGame.game.entities.building.SmallTower;
import triGame.game.entities.building.Tower;
import triGame.game.entities.wall.Barrier;
import triGame.game.entities.wall.TrapDoor;
import triGame.game.projectile.gun.AbstractGun;
import triGame.game.projectile.gun.GunManager;

public class Container extends JPanel implements MouseListener{
	private static final long serialVersionUID = 8495420087293979003L;
	
	private PlayerInfoPanel playerInfoPanel;
	private ItemInfoPanel itemInfoPanel;
	private TwoColumnContainerPanel arsenalPanel;
	private GunPanel gunPanel;
	private UpgradePanel upgradePanel;
	private Attacher attacher;
	
	public Container(TriGame game) {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.setPreferredSize(new Dimension(PlayerInterface.WIDTH, game.getDisplay().getHeight()));
		this.addMouseListener(this);
		playerInfoPanel = new PlayerInfoPanel(game.shop);
		itemInfoPanel = new ItemInfoPanel();
		arsenalPanel = new TwoColumnContainerPanel();
		upgradePanel = new UpgradePanel();
		gunPanel = new GunPanel(game.shop, upgradePanel, itemInfoPanel);
		attacher = new Attacher(game, upgradePanel, gunPanel);
		this.add(playerInfoPanel);
		this.add(itemInfoPanel);
		this.add(arsenalPanel);
		this.add(gunPanel);
		this.add(upgradePanel);
		load(game.gunManager);
		game.getDisplay().add(this, BorderLayout.LINE_END);
		game.getDisplay().pack();
		this.roundHandler = game.roundHandler;
	}
	
	private RoundHandler roundHandler;
	public void displayPointsAndRound() {
		playerInfoPanel.setPointsAndRound(roundHandler.getRoundNumber());
	}
	
	public Attacher getAttacher() {
		return attacher;
	}
	
	private void load(GunManager gunManager) {
		playerInfoPanel.setPoints(0);
		playerInfoPanel.setRound(0);
		ShopItemIcon icon = new ShopItemIcon(Barrier.NEW_BARRIER, Barrier.SPRITE_ID, itemInfoPanel, attacher, gunPanel);
		itemInfoPanel.setCost(icon.item.getCost());
		itemInfoPanel.setItemName(icon.item.getName());
		arsenalPanel.setBorder(BorderFactory.createTitledBorder("Arsenal"));
		arsenalPanel.addArsenal(icon);
		arsenalPanel.addArsenal(new ShopItemIcon(Tower.NEW_TOWER, Tower.SPRITE_ID, itemInfoPanel, attacher, gunPanel));
		arsenalPanel.addArsenal(new ShopItemIcon(TrapDoor.NEW_TRAP_DOOR, TrapDoor.SPRITE_ID, itemInfoPanel, attacher, gunPanel));
		arsenalPanel.addArsenal(new ShopItemIcon(SmallTower.NEW_TOWER, SmallTower.SPRITE_ID, itemInfoPanel, attacher, gunPanel));
		for (AbstractGun g : gunManager.getShopableGuns()) {
			gunPanel.addGuns(new GunItem(g.getUnlockItem(), g.getUpgradeManager(), g.name, gunPanel));
		}
	}

	public void mouseClicked(MouseEvent e) {
		attacher.attached = false;
		attacher.dragged = false;
		gunPanel.unselectAllItems();
		upgradePanel.clearUpgradeManager();
	}

	public void mouseEntered(MouseEvent e) { }
	public void mouseExited(MouseEvent e) { }
	public void mousePressed(MouseEvent e) { }
	public void mouseReleased(MouseEvent e) { }
}
