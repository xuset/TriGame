package net.xuset.tSquare.system.sound;

import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.SoundPool;
import net.xuset.tSquare.files.AssetFile;

public class AndroidSound implements ISound {
	private static final int maxSounds = 10;
	private static final SoundPool soundPool = new SoundPool(maxSounds, AudioManager.STREAM_MUSIC, 0);
	
	private final int soundId;
	
	AndroidSound(AssetFile file) {
		AssetFileDescriptor fd = file.getFileDescriptor();
		if (fd == null)
			throw new RuntimeException("Could not open file descriptor");
		soundId = soundPool.load(fd, 1);
	}

	AndroidSound(String url) {
		soundId = soundPool.load(url, 1);
	}
	
	@Override
	public void play() {
		if (!SoundStore.isMuteOn())
			soundPool.play(soundId, 100, 100, 1, 0, 1.0f);
	}

	@Override
	public void stop() {
		soundPool.stop(soundId);
	}

	@Override
	public void close() {
		soundPool.release();
	}

}
