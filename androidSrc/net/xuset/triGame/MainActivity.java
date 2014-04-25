package net.xuset.triGame;

import net.xuset.tSquare.files.AssetFileFactory;
import net.xuset.tSquare.files.IFileFactory;
import net.xuset.tSquare.system.DrawBoard;
import net.xuset.tSquare.system.IDrawBoard;
import net.xuset.triGame.intro.IpGetterIFace;
import net.xuset.triGame.intro.MainStartup;
import net.xuset.triGame.settings.Settings;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.SurfaceView;
import android.view.View;

public class MainActivity extends Activity {
	private AndroidSettings settings = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		IDrawBoard drawBoard = createDrawBoard();
		int blockSize = getBlockSize((View) drawBoard.getBackend());
		settings = new AndroidSettings(this, blockSize);
		
		new AndroidGame(drawBoard, this, settings);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		settings.saveSettings();
	}
	
	private IDrawBoard createDrawBoard() {
		final IDrawBoard drawBoard = new DrawBoard(MainActivity.this);
		SurfaceView viewDraw = (SurfaceView) drawBoard.getBackend();
		viewDraw.setFocusable(true);
		viewDraw.setFocusableInTouchMode(true);
		viewDraw.requestFocus();
		setContentView(viewDraw);
		return drawBoard;
	}
	
	private int getBlockSize(View view) {
		int appWidth = view.getResources().getDisplayMetrics().widthPixels;
		return 50 * (appWidth / 650);
	}
	
	private static class AndroidGame extends Thread {
		private final IDrawBoard drawBoard;
		private final Context context;
		private final Settings settings;
		
		public AndroidGame(IDrawBoard drawBoard, Context context, Settings settings) {
			this.drawBoard = drawBoard;
			this.context = context;
			this.settings = settings;
			start();
		}
		
		@Override
		public void run() {
			IFileFactory fileFactory = new AssetFileFactory(context.getAssets());
			IpGetterIFace ipGetter = new WifiIpGetter(context);
			new MainStartup(drawBoard, fileFactory, settings, ipGetter,
					new AndroidBrowserOpener(context));
		}
	}
}
