package net.xuset.triGame;

import net.xuset.tSquare.files.AssetFileFactory;
import net.xuset.tSquare.files.IFileFactory;
import net.xuset.tSquare.system.DrawBoard;
import net.xuset.tSquare.system.IDrawBoard;
import net.xuset.triGame.intro.GameIntro;
import net.xuset.triGame.intro.IpGetterIFace;
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
		
		new AndroidGame(drawBoard, this);
	}
	
	private static class AndroidGame extends Thread {
		private final IDrawBoard drawBoard;
		private final Context context;
		public AndroidGame(IDrawBoard drawBoard, Context context) {
			this.drawBoard = drawBoard;
			this.context = context;
			start();
		}
		
		@Override
		public void run() {
			IFileFactory fileFactory = new AssetFileFactory(context.getAssets());
			Settings settings = createDefaultSettings();
			IpGetterIFace ipGetter = new WifiIpGetter(context);
			GameIntro intro = new GameIntro(drawBoard, fileFactory, settings, ipGetter);
			intro.createGame().startGame();
		}
		
		private Settings createDefaultSettings() {
			Settings s = new Settings();
			s.blockSize = 120;
			s.defaultBlockSize = 120;
			s.drawUiTouch = true;
			s.enableSound = true;
			return s;
		}
	}
}
