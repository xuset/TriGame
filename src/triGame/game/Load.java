package triGame.game;

//import gameDev.paths.ObjectGrid;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import tSquare.imaging.ImageProccess;
import tSquare.imaging.Sprite;
import triGame.game.entities.Person;
import triGame.game.entities.PointParticle;
import triGame.game.entities.PointWell;
import triGame.game.entities.SpawnHole;
import triGame.game.entities.building.HeadQuarters;
import triGame.game.entities.building.LightTower;
import triGame.game.entities.building.PointCollector;
import triGame.game.entities.building.SmallTower;
import triGame.game.entities.building.Tower;
import triGame.game.entities.wall.Barrier;
import triGame.game.entities.wall.TrapDoor;
import triGame.game.entities.zombies.Zombie;
import triGame.game.projectile.ProjectilePistol;


abstract class Load {
	public static void sprites() {
		spriteSpawnHole();
		spritePerson();
		spriteZombie();
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
	}
	
	private static void spriteSpawnHole() {
		if (Sprite.exists(SpawnHole.SPRITE_ID))
			return;
		BufferedImage image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setColor(Color.DARK_GRAY);
		g.fillOval(0, 0, 50, 50);
		g.dispose();
		new Sprite(SpawnHole.SPRITE_ID, image);
	}
	
	private static void spritePerson() {
		if (Sprite.exists(Person.SPRITE_ID))
			return;
		BufferedImage image = new BufferedImage(80, 80, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Polygon p = new Polygon(); p.addPoint(0, 80); p.addPoint(80, 80); p.addPoint(40, 0);
		g.setColor(Color.DARK_GRAY);
		g.fillPolygon(p);
		p = new Polygon(); p.addPoint(10, 70); p.addPoint(70, 70); p.addPoint(40, 16);
		g.setColor(Color.cyan);
		//g.setColor(new Color((int) (120 * Math.random()), (int) (255 * Math.random()), (int) (255 * Math.random())));
		g.fillPolygon(p);
		g.dispose();
		
		new Sprite(Person.SPRITE_ID, ImageProccess.scale(image, 0.5, 0.5));
	}
	
	private static void spriteZombie() {
		if (Sprite.exists(Zombie.SPRITE_ID))
			return;
		BufferedImage image = new BufferedImage(80, 80, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Polygon p = new Polygon(); p.addPoint(0, 80); p.addPoint(80, 80); p.addPoint(40, 0);
		g.setColor(Color.DARK_GRAY);
		g.fillPolygon(p);
		p = new Polygon(); p.addPoint(10, 70); p.addPoint(70, 70); p.addPoint(40, 16);
		g.setColor(Color.red);
		g.fillPolygon(p);
		g.dispose();
		new Sprite(Zombie.SPRITE_ID, ImageProccess.scale(image, 0.5, 0.5));
	}
	
	private static void spriteBarrier() {
		if (Sprite.exists(Barrier.SPRITE_ID))
			return;
		BufferedImage image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, 50, 50);
		g.setColor(Color.gray);
		g.fillRect(5, 5, 40, 40);
		g.dispose();
		image = ImageProccess.createCompatiableImage(image);
		new Sprite(Barrier.SPRITE_ID, image, true);
	}
	
	private static void spriteTower() {
		if (Sprite.exists(Tower.SPRITE_ID))
			return;
		BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setColor(Color.YELLOW);
		g.fillOval(10, 10, 80, 80);
		g.setColor(Color.black);
		g.fillRect(46, 0, 8, 50);
		g.dispose();
		new Sprite(Tower.SPRITE_ID, ImageProccess.scale(image, 0.5, 0.5));
	}
	
	private static void spriteSmallTower() {
		if (Sprite.exists(SmallTower.SPRITE_ID))
			return;
		BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setColor(new Color(250, 200, 30));
		g.fillOval(15, 15, 70, 70);
		g.setColor(Color.yellow);
		g.fillRect(46, 0, 8, 75);
		g.dispose();
		new Sprite(SmallTower.SPRITE_ID, ImageProccess.scale(image, 0.5, 0.5));
		
	}
	
	/*private static void spriteLaserTower() {
		if (Sprite.exists(LaserTower.SPRITE_ID))
			return;
		BufferedImage image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setColor(Color.ORANGE);
		g.fillOval(0, 0, 50, 50);
		g.dispose();
		new Sprite(LaserTower.SPRITE_ID, image);
	}*/
	
	private static void spriteTrapDoor() {
		if (Sprite.exists(TrapDoor.SPRITE_ID))
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
		new Sprite(TrapDoor.SPRITE_ID, image);
	}
	
	private static void spriteProjectile() {
		if (Sprite.exists(ProjectilePistol.SPRITE_ID))
			return;
		BufferedImage image = new BufferedImage(2, 10, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setColor(new Color(255, 80, 0));
		g.fillRect(0, 0, 2, 10);
		g.dispose();
		new Sprite(ProjectilePistol.SPRITE_ID, image);
	}
	
	private static void spriteHQ() {
		if (Sprite.exists(HeadQuarters.SPRITE_ID))
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
		new Sprite(HeadQuarters.SPRITE_ID, image);
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
		new Sprite(PointParticle.SPRITE_ID, image);
	}
	
	private static void spritePointCollector() {
		Sprite.add(PointCollector.SPRITE_ID);
	}
	
	private static void spriteLightTower() {
		Sprite.add(LightTower.SPRITE_ID);
	}
	
	private static void spritePointWell() {
		Sprite.add(PointWell.SPRITE_ID);
	}
}
