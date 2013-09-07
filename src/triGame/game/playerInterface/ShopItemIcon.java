package triGame.game.playerInterface;


import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import tSquare.imaging.Sprite;
import triGame.game.shopping.ShopItem;

public class ShopItemIcon extends JLabel implements MouseListener{
	private static final long serialVersionUID = -3425550835043822788L;
	
	private Attacher attacher;
	private ItemInfoPanel infoPanel;
	private GunPanel gunPanel;
	
	public ShopItem item;
	public String spriteId;
	
	public ShopItemIcon(ShopItem item, String spriteId, ItemInfoPanel infoPanel, Attacher attacher, GunPanel gunPanel) {
		this.infoPanel = infoPanel;
		this.attacher = attacher;
		this.gunPanel = gunPanel;
		if (spriteId == null)
			this.setText(item.getName());
		else
			this.setIcon(new ImageIcon(Sprite.get(spriteId).getImage(), item.getName()));
		this.item = item;
		this.spriteId = spriteId;
		this.addMouseListener(this);
		this.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
	}
	
	public void mouseEntered(MouseEvent e) {
		infoPanel.setItemName(item.getName());
		infoPanel.setCost(item.getCost());
		this.setBorder(BorderFactory.createLineBorder(Color.blue, 1));
		gunPanel.unselectAllItems();
	}
	
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			if (e.getClickCount() > 1) {
				attacher.dragged = false;
				attacher.attached = true;
				attacher.spriteId = spriteId;
			} else {
				attacher.dragged = true;
				attacher.attached = true;
				attacher.spriteId = spriteId;
			}
		}
	}
	
	public void mouseExited(MouseEvent e) {
		this.setBorder(BorderFactory.createEmptyBorder(1, 1, 1 ,1));
	}
	
	public void mouseClicked(MouseEvent e) { }
	public void mouseReleased(MouseEvent e) { }
	
}
