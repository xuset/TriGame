package net.xuset.triGame;

import net.xuset.tSquare.files.AssetFileFactory;
import net.xuset.tSquare.files.IFileFactory;
import net.xuset.tSquare.system.DrawBoard;
import net.xuset.tSquare.system.IDrawBoard;
import net.xuset.triGame.intro.GameIntro;
import net.xuset.triGame.settings.Settings;

import android.os.Bundle;
import android.app.Activity;
import android.view.SurfaceView;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		IDrawBoard drawBoard = new DrawBoard(MainActivity.this);
		SurfaceView viewDraw = (SurfaceView) drawBoard.getBackend();
		viewDraw.setFocusable(true);
		viewDraw.setFocusableInTouchMode(true);
		viewDraw.requestFocus();
		setContentView(viewDraw);
		
		new AndroidGame(drawBoard);
	}
	
	private class AndroidGame extends Thread {
		private final IDrawBoard drawBoard;
		public AndroidGame(IDrawBoard drawBoard) {
			this.drawBoard = drawBoard;
			start();
		}
		
		@Override
		public void run() {
			IFileFactory fileFactory = new AssetFileFactory(getAssets());
			GameIntro intro = new GameIntro(drawBoard, fileFactory, createDefaultSettings());
			intro.createGame().startGame();
			//TriGame tGame = new TriGame(
			//new GameInfo(Network.createOffline(), GameType.SURVIVAL, NetworkType.SOLO),
			//		drawBoard, fileFactory, createDefaultSettings());
			//tGame.startGame();
		}
		
		private Settings createDefaultSettings() {
			Settings s = new Settings();
			s.blockSize = 100;
			s.defaultBlockSize = 100;
			s.drawUiTouch = true;
			s.enableSound = true;
			return s;
		}
	}
}
