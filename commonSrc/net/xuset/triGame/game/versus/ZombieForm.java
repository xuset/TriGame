package net.xuset.triGame.game.versus;

import net.xuset.tSquare.imaging.Sprite;
import net.xuset.tSquare.imaging.TsColor;
import net.xuset.tSquare.system.input.mouse.MouseAction;
import net.xuset.tSquare.system.input.mouse.TsMouseEvent;
import net.xuset.tSquare.ui.Alignment;
import net.xuset.tSquare.ui.Axis;
import net.xuset.tSquare.ui.UiButton;
import net.xuset.tSquare.ui.UiForm;
import net.xuset.tSquare.ui.UiLabel;
import net.xuset.tSquare.ui.UiSlider;
import net.xuset.tSquare.ui.layout.UiLayout;
import net.xuset.tSquare.ui.layout.UiQueueLayout;
import net.xuset.tSquare.util.Observer.Change;
import net.xuset.triGame.game.entities.zombies.BossZombie;
import net.xuset.triGame.game.shopping.ShopManager;
import net.xuset.triGame.game.ui.arsenal.ArsenalSubForm;

public class ZombieForm extends ArsenalSubForm {
	private static final double minSpeed = 3/5.0, minHealth = 150;
	private static final double maxSpeed = 3.0, maxHealth = 500;
	private static final double pricePerSpeed = 150.0 / (maxSpeed - minSpeed);
	private static final double pricePerHealth = 150.0/ (maxHealth - minHealth);
	private static final int initPrice = 50;
	
	private final VersusSpawner versusSpawner;
	private final ShopManager shop;
	private final UiLabel lblPrice = new UiLabel();
	
	private int price = initPrice;
	private double health = minHealth;
	private double speed = minSpeed;
	
	public ZombieForm(VersusSpawner versusSpawner, ShopManager shop) {
		super("Zombies");
		this.versusSpawner = versusSpawner;
		this.shop = shop;
		shop.observer().watch(new ShopListener());
		
		UiButton btnBuy = new UiButton("Buy");
		btnBuy.addMouseListener(new BtnBuyAction());
		
		UiLabel lblZombiePic = new UiLabel();
		lblZombiePic.setImage(Sprite.get(BossZombie.SPRITE_ID).image);
		
		getLayout().setAlignment(Axis.Y_AXIS, Alignment.CENTER);
		getLayout().setAlignment(Axis.X_AXIS, Alignment.CENTER);
		getLayout().add(lblZombiePic);
		getLayout().add(createFrmSlider());
		getLayout().add(lblPrice);
		getLayout().add(btnBuy);
		
		recalcPrice();
	}
	
	private UiForm createFrmSlider() {
		UiForm frmSlider = new UiForm();
		UiLayout l = new UiQueueLayout(10, 5, frmSlider);
		frmSlider.setLayout(l);
		l.setOrientation(Axis.Y_AXIS);
		UiForm frmHealth = new UiForm();
		frmHealth.getLayout().add(new UiLabel("Health: "));
		UiForm frmSpeed = new UiForm();
		frmSpeed.getLayout().add(new UiLabel("Speed: "));
		UiSlider sldrHealth = new UiSlider();
		UiSlider sldrSpeed = new UiSlider();
		sldrHealth.setSliderListener(new SldrHealthAction());
		sldrSpeed.setSliderListener(new SldrSpeedAction());
		frmHealth.getLayout().add(sldrHealth);
		frmSpeed.getLayout().add(sldrSpeed);
		l.add(frmHealth);
		l.add(frmSpeed);
		return frmSlider;
	}
	
	private void recalcPrice() {
		double healthPrice = (health - minHealth) * pricePerHealth;
		double speedPrice = (speed - minSpeed) * pricePerSpeed;
		price = (int) (initPrice + healthPrice + speedPrice);
		
		lblPrice.setText("$" + price);
		lblPrice.setForeground(
				shop.getPointCount() >= price ? TsColor.black : TsColor.red);
	}
	
	private class ShopListener implements Change<ShopManager> {
		@Override
		public void observeChange(ShopManager t) {
			recalcPrice();
		}
	}
	
	private class BtnBuyAction implements Change<TsMouseEvent> {
		@Override
		public void observeChange(TsMouseEvent t) {
			if (t.action == MouseAction.RELEASE &&
					shop.getPointCount() >= price) {
				
				shop.addPoints(-price);
				versusSpawner.spawnBoss(health, speed);
			}
		}
	}
	
	private class SldrHealthAction implements UiSlider.SliderChange {
		@Override
		public void onChange(double newPosition) {
			health = ((maxHealth - minHealth) * newPosition + minHealth);
			recalcPrice();
		}
	}
	
	private class SldrSpeedAction implements UiSlider.SliderChange {
		@Override
		public void onChange(double newPosition) {
			speed = ((maxSpeed - minSpeed) * newPosition + minSpeed);
			recalcPrice();
		}
	}
	
	
}
