package triGame.game.versus;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

import tSquare.imaging.Sprite;
import tSquare.util.Observer;
import triGame.game.entities.PointParticle;
import triGame.game.entities.zombies.BossZombie;
import triGame.game.shopping.ShopItem;
import triGame.game.shopping.ShopManager;
import triGame.game.ui.FocusSurrender;
import triGame.game.ui.arsenal.ArsenalGroup;

class ZombieUI extends ArsenalGroup{
	
	private final JLabel lblImage;
	private final JLabel lblDescription;
	private final JLabel lblPoints;
	private final JLabel lblPrice;
	private final JButton btnPurchase;
	private final FocusSurrender focus;
	private final VersusSpawner spawner;
	private final ShopManager shop;
	private final ShopItem bossItem = new ShopItem("Boss zombie", 100);

	ZombieUI(FocusSurrender focus, VersusSpawner spawner, ShopManager shop) {
		super("Zombie", null);
		this.focus = focus;
		this.spawner = spawner;
		this.shop = shop;
		
		lblImage = new JLabel(new ImageIcon(Sprite.get(BossZombie.SPRITE_ID).image));
		lblDescription = new JLabel("Send the other zone a surprise for just ");
		lblPoints = new JLabel(new ImageIcon(Sprite.get(PointParticle.SPRITE_ID).image));
		lblPrice = new JLabel(bossItem.getCost() + ".");
		
		btnPurchase = new JButton("Purchase");
		btnPurchase.addActionListener(new PurchaseListener());
		
		panel.add(lblImage);
		panel.add(lblDescription);
		panel.add(lblPoints);
		panel.add(lblPrice);
		panel.add(btnPurchase);
		
		ShopObserver observer = new ShopObserver();
		shop.observer().watch(observer);
		observer.observeChange(shop);
		
	}
	
	private class PurchaseListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (shop.purchase(bossItem))
				spawner.spawnBoss();
			focus.surrenderFocus();
		}
	}
	
	private class ShopObserver implements Observer.Change<ShopManager> {
		@Override
		public void observeChange(ShopManager t) {
			if (t.getPointCount() >= bossItem.getCost())
				lblPrice.setForeground(Color.black);
			else
				lblPrice.setForeground(Color.red);
		}
	}

}
