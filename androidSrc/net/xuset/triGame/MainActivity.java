package net.xuset.triGame;

import net.xuset.tSquare.files.AssetFileFactory;
import net.xuset.tSquare.files.IFileFactory;
import net.xuset.tSquare.system.DrawBoard;
import net.xuset.tSquare.system.IDrawBoard;
import net.xuset.triGame.intro.DummyUpdateChecker;
import net.xuset.triGame.intro.IUpdateChecker;
import net.xuset.triGame.intro.IpGetterIFace;
import net.xuset.triGame.intro.MainStartup;
import net.xuset.triGame.settings.Settings;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.SurfaceView;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final IDrawBoard drawBoard = new DrawBoard(MainActivity.this);
		SurfaceView viewDraw = (SurfaceView) drawBoard.getBackend();
		viewDraw.setFocusable(true);
		viewDraw.setFocusableInTouchMode(true);
		viewDraw.requestFocus();
		setContentView(viewDraw);
		
		int screenWidth = viewDraw.getResources().getDisplayMetrics().widthPixels;
		int blockSize = getBlockSize(screenWidth);
		new AndroidGame(drawBoard, this, blockSize);
	}
	
	private int getBlockSize(int appWidth) {
		return 50 * (appWidth / 650);
	}
	
	private static class AndroidGame extends Thread {
		private final IDrawBoard drawBoard;
		private final Context context;
		private final int initBlockSize;
		
		public AndroidGame(IDrawBoard drawBoard, Context context, int initBlockSize) {
			this.drawBoard = drawBoard;
			this.context = context;
			this.initBlockSize = initBlockSize;
			start();
		}
		
		@Override
		public void run() {
			IFileFactory fileFactory = new AssetFileFactory(context.getAssets());
			Settings settings = createDefaultSettings();
			IpGetterIFace ipGetter = new WifiIpGetter(context);
			IUpdateChecker updateChecker = new DummyUpdateChecker();
			new MainStartup(drawBoard, fileFactory, settings, ipGetter, updateChecker);
		}
		
		private Settings createDefaultSettings() {
			Settings s = new Settings();
			s.blockSize = initBlockSize;
			s.defaultBlockSize = initBlockSize;
			s.drawUiTouch = true;
			s.enableSound = true;
			return s;
		}
	}
}
