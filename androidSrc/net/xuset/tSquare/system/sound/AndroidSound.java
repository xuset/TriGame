package net.xuset.tSquare.system.sound;

import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.SoundPool;
import net.xuset.tSquare.files.AssetFile;

public class AndroidSound implements ISound {
	private static final int maxSounds = 10;
	private static SoundPool soundPool = createPool();
	
	private final int systemSoundId;
	private int tempSoundId = 0;
	
	AndroidSound(AssetFile file) {
		AssetFileDescriptor fd = file.getFileDescriptor();
		if (fd == null)
			throw new RuntimeException("Could not open file descriptor");
		systemSoundId = soundPool.load(fd, 1);
	}

	AndroidSound(String url) {
		systemSoundId = soundPool.load(url, 1);
	}
	
	@Override
	public void play() {
		if (!SoundStore.isMuteOn()) {
			if (tempSoundId != 0)
				stop();
			
			tempSoundId = soundPool.play(systemSoundId, 100, 100, 6, 0, 1.0f);
			System.out.println("STARTING: " + tempSoundId);
		}
	}

	@Override
	public void stop() {
		System.out.println("STOPING: " + tempSoundId);
		soundPool.stop(tempSoundId);
	}

	@Override
	public void close() {
		soundPool.unload(systemSoundId);
	}
	
	public static void releaseResources() {
		if (soundPool != null)
			soundPool.release();
		soundPool = null;
	}
	
	public static void resetPool() {
		releaseResources();
		soundPool = createPool();
	}
	
	private static SoundPool createPool() {
		return new SoundPool(maxSounds, AudioManager.STREAM_MUSIC, 0);
	}

}
