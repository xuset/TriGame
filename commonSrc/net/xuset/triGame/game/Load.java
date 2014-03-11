package net.xuset.triGame.game;

import net.xuset.tSquare.files.IFile;
import net.xuset.tSquare.files.IFileFactory;
import net.xuset.tSquare.imaging.AnimatedSprite;
import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.IImage;
import net.xuset.tSquare.imaging.IImageFactory;
import net.xuset.tSquare.imaging.ImageFactory;
import net.xuset.tSquare.imaging.ScaledGraphics;
import net.xuset.tSquare.imaging.Sprite;
import net.xuset.tSquare.imaging.TsColor;
import net.xuset.tSquare.imaging.TsFont;
import net.xuset.tSquare.imaging.TsTypeFace;
import net.xuset.tSquare.system.sound.ISound;
import net.xuset.tSquare.system.sound.SoundFactory;
import net.xuset.tSquare.system.sound.SoundStore;
import net.xuset.triGame.game.entities.Person;
import net.xuset.triGame.game.entities.PointParticle;
import net.xuset.triGame.game.entities.PointWell;
import net.xuset.triGame.game.entities.SpawnHole;
import net.xuset.triGame.game.entities.buildings.Wall;
import net.xuset.triGame.game.entities.buildings.types.Barrier;
import net.xuset.triGame.game.entities.buildings.types.FreezeTower;
import net.xuset.triGame.game.entities.buildings.types.HeadQuarters;
import net.xuset.triGame.game.entities.buildings.types.HealthTower;
import net.xuset.triGame.game.entities.buildings.types.LightTower;
import net.xuset.triGame.game.entities.buildings.types.MortarTower;
import net.xuset.triGame.game.entities.buildings.types.PointCollector;
import net.xuset.triGame.game.entities.buildings.types.SmallTower;
import net.xuset.triGame.game.entities.buildings.types.SteelBarrier;
import net.xuset.triGame.game.entities.buildings.types.StrongWall;
import net.xuset.triGame.game.entities.buildings.types.Tower;
import net.xuset.triGame.game.entities.dropPacks.DropPack;
import net.xuset.triGame.game.entities.projectiles.MortarProjectile;
import net.xuset.triGame.game.entities.projectiles.Projectile;
import net.xuset.triGame.game.entities.zombies.BossZombie;
import net.xuset.triGame.game.entities.zombies.Zombie;
import net.xuset.triGame.game.ui.arsenal.UiTrashCan;


public abstract class Load {
	
	private static final String[] spriteUrlOnDisk = new String[] {
		PointCollector.INFO.spriteId,
		LightTower.INFO.spriteId,
		PointWell.SPRITE_ID,
		SteelBarrier.INFO.spriteId,
		FreezeTower.INFO.spriteId,
		MortarTower.INFO.spriteId,
		HealthTower.INFO.spriteId,
		MortarProjectile.SPRITE_ID,
		UiTrashCan.SPRITE_ID
	};
	
	private static final String[] soundUrlOnDisk = new String[] {
		Projectile.SOUND_ID
	};
	
	static void loadResources(int blockSize, IFileFactory fileFactory) {
		drawSprites(blockSize);
		loadSpritesOnDisk(blockSize, fileFactory);
		loadAnimSpritesOnDisk(blockSize, fileFactory);
		loadSoundsOnDisk(fileFactory);
	}
	
	private static void loadSpritesOnDisk(int blockSize, IFileFactory ff) {
		float scale = blockSize / 150.0f;
		for (String url : spriteUrlOnDisk) {
			IImage img = loadAndScaleImage(url, scale, ff);
			Sprite.add(new Sprite(url, img, 1.0f / blockSize));
		}
	}
	
	private static void loadAnimSpritesOnDisk(int blockSize, IFileFactory ff) {
		float scale = blockSize / 50.0f;
		IImage img = loadAndScaleImage(Wall.CRACKS_SPRITE_ID, scale, ff);
		AnimatedSprite wallSprite = new AnimatedSprite(
				Wall.CRACKS_SPRITE_ID, img, 1, 3, 3);
		AnimatedSprite.ADD(Wall.CRACKS_SPRITE_ID, wallSprite);
	}
	
	private static IImage loadAndScaleImage(String url, float scale, IFileFactory ff) {
		IFile file = ff.open(url);
		IImageFactory factory = new ImageFactory();
		IImage img = factory.loadImage(file);
		IImage scaled = factory.createScaled(img, scale, scale);
		file.close();
		return scaled;
	}
	
	private static void loadSoundsOnDisk(IFileFactory ff) {
		for (String url : soundUrlOnDisk) {
			IFile file = ff.open(url);
			ISound sound = SoundFactory.instance.loadSound(file);
			SoundStore.add(url, sound);
			file.close();
		}
	}
	
	private static void drawSprites(int blockSize) {
		spriteSpawnHole(blockSize);
		spritePerson(blockSize);
		spriteZombie(blockSize);
		spriteBossZombie(blockSize);
		spriteBarrier(blockSize);
		spriteTower(blockSize);
		spriteSmallTower(blockSize);
		spriteProjectile(blockSize);
		spriteHQ(blockSize);
		spritePointParticle(blockSize);
		spriteHealthPack(blockSize);
		spritePointPack(blockSize);
		spriteStrongWall(blockSize);
	}
	
	public static IImage triangleImage(TsColor color, int blockSize) {
		final int size = (int) (0.8 * blockSize); //40px for 50px block size

		IImageFactory factory = new ImageFactory();
		IImage image = factory.createEmpty(size, size);
		IGraphics g = new ScaledGraphics(image.getGraphics(), blockSize / 50.0f);
		g.setAntiAlias(true);
		g.setColor(TsColor.darkGray);
		g.fillTriangle(0, 0, 40, 40);
		g.setColor(color);
		g.fillTriangle(5, 5, 30, 30);
		g.dispose();
		return image;
	}
	
	private static void spriteSpawnHole(int blockSize) {
		if (Sprite.exists(SpawnHole.SPRITE_ID))
			return;

		IImageFactory factory = new ImageFactory();
		final float scale = blockSize / 50.0f; //drawn in 50x50px
		IImage image = factory.createEmpty(blockSize, blockSize);
		IGraphics g = new ScaledGraphics(image.getGraphics(), scale);
		g.setColor(TsColor.darkGray);
		g.fillTriangle(10, 10, 30, 30);
		g.setColor(TsColor.lightGray);
		g.fillTriangle(16, 18, 18, 18);
		g.dispose();
		Sprite.add(new Sprite(SpawnHole.SPRITE_ID, image, 1.0f / blockSize));
	}
	
	private static void spritePerson(int blockSize) {
		if (Sprite.exists(Person.SPRITE_ID))
			return;
		
		Sprite.add(new Sprite(Person.SPRITE_ID,
				triangleImage(TsColor.cyan, blockSize),
				1.0f / blockSize));
	}
	
	private static void spriteZombie(int blockSize) {
		if (Sprite.exists(Zombie.SPRITE_ID))
			return;
		
		Sprite.add(new Sprite(Zombie.SPRITE_ID,
				triangleImage(TsColor.red, blockSize),
				1.0f / blockSize));
	}
	
	private static void spriteBossZombie(int blockSize) {
		if (Sprite.exists(BossZombie.SPRITE_ID))
			return;
		
		Sprite.add(new Sprite(BossZombie.SPRITE_ID,
				triangleImage(TsColor.black, blockSize),
				1.0f / blockSize));
	}
	
	private static void spriteBarrier(int blockSize) {
		if (Sprite.exists(Barrier.INFO.spriteId))
			return;

		final float scale = blockSize / 50.0f;

		IImageFactory factory = new ImageFactory();
		IImage image = factory.createEmpty(blockSize, blockSize);
		IGraphics g = new ScaledGraphics(image.getGraphics(), scale);
		g.setColor(TsColor.darkGray);
		g.fillRect(0, 0, 50, 50);
		g.setColor(TsColor.gray);
		g.fillRect(5, 5, 40, 40);
		g.dispose();
		
		Sprite.add(new Sprite(Barrier.INFO.spriteId, image, 1.0f / blockSize));
	}
	
	private static void spriteTower(int blockSize) {
		if (Sprite.exists(Tower.INFO.spriteId))
			return;

		IImageFactory factory = new ImageFactory();
		IImage image = factory.createEmpty(blockSize, blockSize);
		IGraphics g = new ScaledGraphics(image.getGraphics(), blockSize / 50.0f);
		g.setColor(TsColor.yellow);
		g.fillOval(5, 5, 40, 40);
		g.setColor(TsColor.black);
		g.fillRect(23, 0, 4, 25);
		g.dispose();
		
		Sprite.add(new Sprite(Tower.INFO.spriteId, image, 1.0f / blockSize));
	}
	
	private static void spriteSmallTower(int blockSize) {
		if (Sprite.exists(SmallTower.INFO.spriteId))
			return;

		IImageFactory factory = new ImageFactory();
		IImage image = factory.createEmpty(blockSize, blockSize);
		IGraphics g = new ScaledGraphics(image.getGraphics(), blockSize / 50.0f);
		g.setColor(250, 200, 30);
		g.fillOval(7, 7, 36, 36);
		g.setColor(TsColor.yellow);
		g.fillRect(23, 0, 4, 37);
		g.dispose();
		
		Sprite.add(new Sprite(SmallTower.INFO.spriteId, image, 1.0f / blockSize));
		
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
	
	private static void spriteProjectile(int blockSize) {
		if (Sprite.exists(Projectile.SPRITE_ID))
			return;

		IImageFactory factory = new ImageFactory();
		int height = blockSize / 5;
		int width = blockSize < 25 ? 1 : blockSize / 25;
		IImage image = factory.createEmpty(width, height);
		IGraphics g = new ScaledGraphics(image.getGraphics(), blockSize / 50.0f);
		g.setColor(255, 80, 0);
		g.fillRect(0, 0, 2, 10);
		g.dispose();
		Sprite.add(new Sprite(Projectile.SPRITE_ID, image, 1.0f / blockSize));
	}
	
	private static void spriteHQ(int blockSize) {
		if (Sprite.exists(HeadQuarters.INFO.spriteId))
			return;
		
		

		IImageFactory factory = new ImageFactory();
		IImage image = factory.createEmpty(blockSize * 2, blockSize * 2);
		IGraphics g = new ScaledGraphics(image.getGraphics(), blockSize / 50.0f);
		g.setAntiAlias(true);
		g.setColor(TsColor.darkGray);
		g.fillRoundedRect(0, 0, 100, 100, 25, 25);
		g.setColor(TsColor.lightGray);
		g.fillRoundedRect(5, 5, 90, 90, 25, 25);
		g.setColor(new TsColor(15, 30, 150));
		g.fillRoundedRect(10, 10, 80, 30, 25, 25);
		g.fillRoundedRect(10, 60, 80, 30, 25, 25);
		g.setColor(new TsColor(40, 80, 120));
		String hq = "HQ";
		g.setFont(new TsFont("Arial", 20, TsTypeFace.BOLD));
		float hqWidth = g.getTextWidth(hq);
		g.drawText(50 - hqWidth / 2, 58, hq);
		g.dispose();
		Sprite.add(new Sprite(HeadQuarters.INFO.spriteId, image, 1.0f / blockSize));
	}
	
	private static void spritePointParticle(int blockSize) {
		if (Sprite.exists(PointParticle.SPRITE_ID))
			return;

		IImageFactory factory = new ImageFactory();
		int size = (int) (0.12 * blockSize);
		IImage image = factory.createEmpty(size, size);
		IGraphics g = new ScaledGraphics(image.getGraphics(), blockSize / 50.0f);
		g.setColor(TsColor.yellow);
		g.fillRect(0, 2, 6, 2);
		g.fillRect(2, 0, 2, 6);
		g.dispose();
		Sprite.add(new Sprite(PointParticle.SPRITE_ID, image, 1.0f / blockSize));
	}
	
	private static IImage createDropPack(TsColor background, TsColor foreground, 
			int blockSize) {

		IImageFactory factory = new ImageFactory();
		final int size = (int) (blockSize * 24.0f / 50.0f);
		IImage image = factory.createEmpty(size, size);
		IGraphics g = new ScaledGraphics(image.getGraphics(), blockSize / 50.0f);
		g.setColor(TsColor.darkGray);
		g.fillRect(0, 0, 24, 24);
		g.setColor(background);
		g.fillRect(3, 3, 18, 18);
		g.setColor(foreground);
		g.fillRect(10, 3, 4, 18);
		g.fillRect(3, 10, 18, 4);
		g.dispose();
		return image;
	}
	
	private static void spriteHealthPack(int blockSize) {
		if (Sprite.exists(DropPack.HealthInfo.SPRITE_ID))
			return;
		
		IImage image = createDropPack(TsColor.white, TsColor.red, blockSize);
		Sprite.add(new Sprite(DropPack.HealthInfo.SPRITE_ID, image, 1.0f / blockSize));
	}
	
	private static void spritePointPack(int blockSize) {
		if (Sprite.exists(DropPack.PointInfo.SPRITE_ID))
			return;
		
		IImage image = createDropPack(TsColor.gray, TsColor.yellow, blockSize);
		Sprite.add(new Sprite(DropPack.PointInfo.SPRITE_ID, image, 1.0f / blockSize));
	}
	
	private static void spriteStrongWall(int blockSize) {
		if (Sprite.exists(StrongWall.INFO.spriteId))
			return;

		final float scale = blockSize / 50.0f;

		IImageFactory factory = new ImageFactory();
		IImage image = factory.createEmpty(blockSize, blockSize);
		IGraphics g = new ScaledGraphics(image.getGraphics(), scale);
		g.setColor(TsColor.darkGray);
		g.fillRect(0, 0, 50, 50);
		g.setColor(TsColor.black.lighter());
		g.fillRect(5, 5, 40, 40);
		g.dispose();
		
		Sprite.add(new Sprite(StrongWall.INFO.spriteId, image, 1.0f / blockSize));
	}
}
