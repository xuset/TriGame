package triGame.game.versus;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import tSquare.imaging.Sprite;
import tSquare.util.Observer;
import triGame.game.entities.PointParticle;
import triGame.game.entities.zombies.BossZombie;
import triGame.game.shopping.ShopItem;
import triGame.game.shopping.ShopManager;
import triGame.game.ui.FocusSurrender;
import triGame.game.ui.arsenal.ArsenalGroup;
import triGame.game.ui.arsenal.ArsenalPanel;

class ZombieUI extends ArsenalGroup{
	private static final int maxSpeed = 50, maxHealth = 500;
	private static final double pricePerSpeed = 150.0/maxSpeed, pricePerHealth = 150.0/maxHealth;
	
	private final JLabel lblImage;
	private final JLabel lblPrice;
	private final JButton btnPurchase;
	private final JSlider slrHealth;
	private final JSlider slrSpeed;
	private final FocusSurrender focus;
	private final VersusSpawner spawner;
	private final ShopManager shop;
	private final ShopItem bossItem = new ShopItem("Boss zombie", 100);

	ZombieUI(FocusSurrender focus, VersusSpawner spawner,
			ShopManager shop, ArsenalPanel arsenalPanel) {
		
		super("Zombie", null);
		this.focus = focus;
		this.spawner = spawner;
		this.shop = shop;
		
		lblImage = new JLabel(new ImageIcon(Sprite.get(BossZombie.SPRITE_ID).image));
		lblPrice = new JLabel(bossItem.getCost() + "");
		lblPrice.setIcon(new ImageIcon(Sprite.get(PointParticle.SPRITE_ID).image));
		lblPrice.setOpaque(true);
		lblPrice.setBackground(Color.LIGHT_GRAY);
		lblPrice.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
		
		btnPurchase = new JButton("Purchase");
		btnPurchase.addActionListener(new PurchaseListener());
		
		JPanel pnlSlider = new JPanel();
		pnlSlider.setLayout(new BoxLayout(pnlSlider, BoxLayout.Y_AXIS));
		slrHealth = new JSlider(JSlider.HORIZONTAL, 0, maxHealth, 0);
		slrSpeed = new JSlider(JSlider.HORIZONTAL, 0, maxSpeed, 0);
		SliderListener sliderListener = new SliderListener();
		slrHealth.addChangeListener(sliderListener);
		slrSpeed.addChangeListener(sliderListener);
		pnlSlider.add(new JLabel("Health", JLabel.CENTER));
		pnlSlider.add(slrHealth);
		pnlSlider.add(new JLabel("Speed", JLabel.CENTER));
		pnlSlider.add(slrSpeed);
		
		panel.add(lblImage);
		panel.add(pnlSlider);
		panel.add(lblPrice);
		panel.add(btnPurchase);
		
		ShopObserver observer = new ShopObserver();
		shop.observer().watch(observer);
		observer.observeChange(shop);
		
	}
	
	@Override
	protected void onSwitchTo(ArsenalPanel arsenalPanel) {
		arsenalPanel.displayDescription("Send the other zone a surprise");
	}
	
	private class SliderListener implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent e) {
			int price = getCost();
			lblPrice.setText(price + "");
			setPriceColor();
			focus.surrenderFocus();
		}
	}
	
	private class PurchaseListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int price = getCost();
			if (shop.getPointCount() >= price) {
				shop.addPoints(-price);
				spawner.spawnBoss(slrHealth.getValue(), slrSpeed.getValue());
			}
			focus.surrenderFocus();
		}
	}
	
	private class ShopObserver implements Observer.Change<ShopManager> {
		@Override
		public void observeChange(ShopManager t) {
			setPriceColor();
		}
	}
	
	private void setPriceColor() {
		if (shop.getPointCount() >= getCost())
			lblPrice.setForeground(Color.black);
		else
			lblPrice.setForeground(Color.red);
	}
	
	private int getCost() {
		int speed = slrSpeed.getValue();
		int health = slrHealth.getValue();
		int price = (int) (speed * pricePerSpeed + health * pricePerHealth);
		price += bossItem.getCost();
		return price;
	}

}
