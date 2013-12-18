package triGame.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import tSquare.imaging.AnimatedSprite;
import tSquare.imaging.ImageProcess;
import tSquare.imaging.Sprite;
import tSquare.system.Sound;
import triGame.game.entities.Person;
import triGame.game.entities.PointParticle;
import triGame.game.entities.PointWell;
import triGame.game.entities.SpawnHole;
import triGame.game.entities.buildings.Wall;
import triGame.game.entities.buildings.types.Barrier;
import triGame.game.entities.buildings.types.FreezeTower;
import triGame.game.entities.buildings.types.HeadQuarters;
import triGame.game.entities.buildings.types.HealthTower;
import triGame.game.entities.buildings.types.LightTower;
import triGame.game.entities.buildings.types.MortarTower;
import triGame.game.entities.buildings.types.PointCollector;
import triGame.game.entities.buildings.types.SmallTower;
import triGame.game.entities.buildings.types.SteelBarrier;
import triGame.game.entities.buildings.types.Tower;
import triGame.game.entities.buildings.types.TrapDoor;
import triGame.game.entities.dropPacks.DropPack;
import triGame.game.entities.projectiles.Projectile;
import triGame.game.entities.zombies.BossZombie;
import triGame.game.entities.zombies.Zombie;


public abstract class Load {
	static void sprites() {
		spriteSpawnHole();
		spritePerson();
		spriteZombie();
		spriteBossZombie();
		spriteBarrier();
		spriteTower();
		spriteSmallTower();
		//spriteLaserTower();
		spriteTrapDoor();
		spriteProjectile();
		spriteHQ();
		spritePointParticle();
		spritePointCollector();
		spriteLightTower();
		spritePointWell();
		spriteHealthPack();
		spritePointPack();
		spriteSteelBarrier();
		spriteFreezeTower();
		spriteMortorTower();
		spriteHealthTower();
		Sound.add(Projectile.SOUND_ID);
		AnimatedSprite.ADD(Wall.CRACKS_SPRITE_ID, new AnimatedSprite(Wall.CRACKS_SPRITE_ID, 1, 3, 3));
	}
	
	public static BufferedImage triangleImage(Color color) {
		BufferedImage image = new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Polygon p = new Polygon(); p.addPoint(0, 40); p.addPoint(40, 40); p.addPoint(20, 0);
		g.setColor(Color.DARK_GRAY);
		g.fillPolygon(p);
		p = new Polygon(); p.addPoint(5, 35); p.addPoint(35, 35); p.addPoint(20, 8);
		g.setColor(color);
		g.fillPolygon(p);
		g.dispose();
		return image;
	}
	
	private static void spriteSpawnHole() {
		if (Sprite.exists(SpawnHole.SPRITE_ID))
			return;
		BufferedImage image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setColor(Color.DARK_GRAY);
		g.fillOval(0, 0, 50, 50);
		g.dispose();
		Sprite.add(new Sprite(SpawnHole.SPRITE_ID, image));
	}
	
	private static void spritePerson() {
		if (Sprite.exists(Person.SPRITE_ID))
			return;
		
		Sprite.add(new Sprite(Person.SPRITE_ID, triangleImage(Color.cyan)));
	}
	
	private static void spriteZombie() {
		if (Sprite.exists(Zombie.SPRITE_ID))
			return;
		
		Sprite.add(new Sprite(Zombie.SPRITE_ID, triangleImage(Color.red)));
	}
	
	private static void spriteBossZombie() {
		if (Sprite.exists(BossZombie.SPRITE_ID))
			return;
		
		Sprite.add(new Sprite(BossZombie.SPRITE_ID, triangleImage(Color.black)));
	}
	
	private static void spriteBarrier() {
		if (Sprite.exists(Barrier.INFO.spriteId))
			return;
		BufferedImage image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, 50, 50);
		g.setColor(Color.gray);
		g.fillRect(5, 5, 40, 40);
		g.dispose();
		image = ImageProcess.createCompatiableImage(image);
		Sprite.add(new Sprite(Barrier.INFO.spriteId, image));
	}
	
	private static void spriteTower() {
		if (Sprite.exists(Tower.INFO.spriteId))
			return;
		BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setColor(Color.YELLOW);
		g.fillOval(10, 10, 80, 80);
		g.setColor(Color.black);
		g.fillRect(46, 0, 8, 50);
		g.dispose();
		Sprite.add(new Sprite(Tower.INFO.spriteId, ImageProcess.scale(image, 0.5, 0.5)));
	}
	
	private static void spriteSmallTower() {
		if (Sprite.exists(SmallTower.INFO.spriteId))
			return;
		BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setColor(new Color(250, 200, 30));
		g.fillOval(15, 15, 70, 70);
		g.setColor(Color.yellow);
		g.fillRect(46, 0, 8, 75);
		g.dispose();
		Sprite.add(new Sprite(SmallTower.INFO.spriteId, ImageProcess.scale(image, 0.5, 0.5)));
		
	}
	
	/*private static void spriteLaserTower() {
		if (Sprite.exists(LaserTower.INFO.spriteId))
			return;
		BufferedImage image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setColor(Color.ORANGE);
		g.fillOval(0, 0, 50, 50);
		g.dispose();
		new Sprite(LaserTower.INFO.spriteId, image);
	}*/
	
	private static void spriteTrapDoor() {
		if (Sprite.exists(TrapDoor.INFO.spriteId))
			return;
		BufferedImage image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, 50, 50);
		g.setColor(Color.gray);
		g.fillRect(5, 5, 40, 40);
		g.setColor(Color.green);
		g.fillRect(5, 20, 40, 10);
		g.fillRect(20, 5, 10, 40);
		g.dispose();
		Sprite.add(new Sprite(TrapDoor.INFO.spriteId, image));
	}
	
	private static void spriteProjectile() {
		if (Sprite.exists(Projectile.SPRITE_ID))
			return;
		BufferedImage image = new BufferedImage(2, 10, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setColor(new Color(255, 80, 0));
		g.fillRect(0, 0, 2, 10);
		g.dispose();
		Sprite.add(new Sprite(Projectile.SPRITE_ID, image));
	}
	
	private static void spriteHQ() {
		if (Sprite.exists(HeadQuarters.INFO.spriteId))
			return;
		BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.DARK_GRAY);
		g.fillRoundRect(0, 0, 100, 100, 25, 25);
		g.setColor(Color.LIGHT_GRAY);
		g.fillRoundRect(5, 5, 90, 90, 25, 25);
		g.setColor(new Color(15, 30, 150));
		//g.fillRoundRect(10, 10, 30, 80, 25, 25);
		//g.fillRoundRect(60, 10, 30, 80, 25, 25);
		g.fillRoundRect(10, 10, 80, 30, 25, 25);
		g.fillRoundRect(10, 60, 80, 30, 25, 25);
		g.setColor(new Color(40, 80, 120));
		String hq = "HQ";
		g.setFont(new Font("Arial", Font.BOLD, 20));
		int hqWidth = g.getFontMetrics().stringWidth(hq);
		g.drawString(hq, 50 - hqWidth / 2, 58);
		g.dispose();
		Sprite.add(new Sprite(HeadQuarters.INFO.spriteId, image));
	}
	
	private static void spritePointParticle() {
		if (Sprite.exists(PointParticle.SPRITE_ID))
			return;
		BufferedImage image = new BufferedImage(6, 6, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setColor(Color.yellow);
		g.fillRect(0, 2, 6, 2);
		g.fillRect(2, 0, 2, 6);
		g.dispose();
		Sprite.add(new Sprite(PointParticle.SPRITE_ID, image));
	}
	
	private static void spritePointCollector() {
		Sprite.add(PointCollector.INFO.spriteId);
	}
	
	private static void spriteLightTower() {
		Sprite.add(LightTower.INFO.spriteId);
	}
	
	private static void spritePointWell() {
		Sprite.add(PointWell.SPRITE_ID);
	}
	
	private static BufferedImage createDropPack(Color background, Color foreground) {
		BufferedImage image = new BufferedImage(24, 24, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		g.setColor(Color.darkGray);
		g.fillRect(0, 0, 24, 24);
		g.setColor(background);
		g.fillRect(3, 3, 18, 18);
		g.setColor(foreground);
		g.fillRect(10, 3, 4, 18);
		g.fillRect(3, 10, 18, 4);
		g.dispose();
		return image;
	}
	
	private static void spriteHealthPack() {
		if (Sprite.exists(DropPack.HealthInfo.SPRITE_ID))
			return;
		
		BufferedImage image = createDropPack(Color.white, Color.red);
		Sprite.add(new Sprite(DropPack.HealthInfo.SPRITE_ID, image));
	}
	
	private static void spritePointPack() {
		if (Sprite.exists(DropPack.PointInfo.SPRITE_ID))
			return;
		
		BufferedImage image = createDropPack(Color.gray, Color.yellow);
		Sprite.add(new Sprite(DropPack.PointInfo.SPRITE_ID, image));
	}
	
	private static void spriteSteelBarrier() {
		Sprite.add(SteelBarrier.INFO.spriteId);
	}
	
	private static void spriteFreezeTower() {
		Sprite.add(FreezeTower.INFO.spriteId);
	}
	
	private static void spriteMortorTower() {
		Sprite.add(MortarTower.INFO.spriteId);
	}
	
	private static void spriteHealthTower() {
		Sprite.add(HealthTower.INFO.spriteId);
	}
}
