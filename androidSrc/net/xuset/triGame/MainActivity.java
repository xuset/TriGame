package net.xuset.triGame;

import net.xuset.tSquare.files.AssetFileFactory;
import net.xuset.tSquare.files.IFileFactory;
import net.xuset.tSquare.system.DrawBoard;
import net.xuset.tSquare.system.IDrawBoard;
import net.xuset.triGame.game.ui.IBrowserOpener;
import net.xuset.triGame.intro.IpGetterIFace;
import net.xuset.triGame.intro.MainStartup;

import android.media.AudioManager;
import android.os.Bundle;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.SurfaceView;
import android.view.View;

public class MainActivity extends Activity {
	private MainStartup gameStarter;
	private AndroidSettings settings;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		IDrawBoard drawBoard = createDrawBoard();
		int blockSize = getBlockSize((View) drawBoard.getBackend());
		settings = new AndroidSettings(this, blockSize);
		gameStarter = createGameStarter(drawBoard);
	}
	
	@Override
	public void onPause() {
		settings.saveSettings();
		super.onPause();
	}
	
	@Override
	public void onStart() {
	    setVolumeControlStream(AudioManager.STREAM_MUSIC);
		if (!gameStarter.isAlive())
			gameStarter.resumeGame();
		super.onStart();
	}
	
	@Override
	public void onStop() {
		gameStarter.suspendGame();
		if (gameStarter.didErrorOccure())
			finish();
		super.onStop();
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
		DisplayMetrics dm = view.getResources().getDisplayMetrics();
		return (int) (50 * (dm.widthPixels / 650) * (dm.xdpi / 442.451));
	}
	
	private MainStartup createGameStarter(IDrawBoard db) {
		IFileFactory ff = new AssetFileFactory(getAssets());
		IpGetterIFace ip = new WifiIpGetter(this);
		IBrowserOpener bo = new AndroidBrowserOpener(this);
		return new MainStartup(db, ff, settings, ip, bo);
	}
}
