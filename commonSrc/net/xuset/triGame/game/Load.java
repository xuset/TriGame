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
import net.xuset.triGame.game.guns.GunPistol;
import net.xuset.triGame.game.guns.GunShotgun;
import net.xuset.triGame.game.guns.GunSub;
import net.xuset.triGame.game.ui.PauseHandler;
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
		UiTrashCan.SPRITE_ID,
		PauseHandler.SPRITE_ID
	};
	
	private static final String[] soundUrlOnDisk = new String[] {
		Tower.SOUND_ID,
		MortarProjectile.SOUND_ID,
		GunPistol.SOUND_ID,
		GunSub.SOUND_ID,
		GunShotgun.SOUND_ID
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
			if (SoundStore.get(url) != null)
				continue;
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
		Sprite.add(new Sprite(Person.SPRITE_ID,
				triangleImage(TsColor.cyan, blockSize),
				1.0f / blockSize));
	}
	
	private static void spriteZombie(int blockSize) {
		Sprite.add(new Sprite(Zombie.SPRITE_ID,
				triangleImage(TsColor.red, blockSize),
				1.0f / blockSize));
	}
	
	private static void spriteBossZombie(int blockSize) {
		Sprite.add(new Sprite(BossZombie.SPRITE_ID,
				triangleImage(TsColor.black, blockSize),
				1.0f / blockSize));
	}
	
	private static void spriteBarrier(int blockSize) {
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
	
	private static void spriteProjectile(int blockSize) {
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
		IImageFactory factory = new ImageFactory();
		IImage image = factory.createEmpty(blockSize * 3, blockSize * 3);
		IGraphics g = new ScaledGraphics(image.getGraphics(), blockSize / 50.0f);
		g.setAntiAlias(true);
		g.setColor(TsColor.darkGray);
		g.fillRoundedRect(0, 0, 150, 150, 25, 25);
		g.setColor(TsColor.lightGray);
		g.fillRoundedRect(5, 5, 140, 140, 25, 25);
		g.setColor(new TsColor(15, 30, 150));
		g.fillRoundedRect(10, 10, 130, 45, 25, 25);
		g.fillRoundedRect(10, 95, 130, 45, 25, 25);
		g.setColor(new TsColor(40, 80, 120));
		String hq = "HQ";
		g.setFont(new TsFont("Arial", 40, TsTypeFace.BOLD));
		float textWidth = g.getTextWidth(hq);
		float textHeight = g.getTextHeight();
		g.drawText(75 - textWidth / 2, 75 + textHeight / 4  + 2, hq);
		g.dispose();
		Sprite.add(new Sprite(HeadQuarters.INFO.spriteId, image, 1.0f / blockSize));
	}
	
	private static void spritePointParticle(int blockSize) {
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
		IImage image = createDropPack(TsColor.white, TsColor.red, blockSize);
		Sprite.add(new Sprite(DropPack.HealthInfo.SPRITE_ID, image, 1.0f / blockSize));
	}
	
	private static void spritePointPack(int blockSize) {
		IImage image = createDropPack(TsColor.gray, TsColor.yellow, blockSize);
		Sprite.add(new Sprite(DropPack.PointInfo.SPRITE_ID, image, 1.0f / blockSize));
	}
	
	private static void spriteStrongWall(int blockSize) {
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
