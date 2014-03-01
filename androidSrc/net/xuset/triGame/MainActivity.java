package net.xuset.triGame;

import net.xuset.tSquare.files.AssetFileFactory;
import net.xuset.tSquare.files.IFileFactory;
import net.xuset.tSquare.system.DrawBoard;
import net.xuset.tSquare.system.IDrawBoard;
import net.xuset.tSquare.system.Network;
import net.xuset.triGame.game.GameInfo;
import net.xuset.triGame.game.TriGame;
import net.xuset.triGame.game.GameInfo.NetworkType;
import net.xuset.triGame.game.GameMode.GameType;

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
//		Demo d = new Demo(drawBoard);
//		d.start();
	}
	
	private class AndroidGame extends Thread {
		private final IDrawBoard drawBoard;
		public AndroidGame(IDrawBoard drawBoard) {
			this.drawBoard = drawBoard;
			start();
		}
		
		@Override
		public void run() {
			
			Network network = Network.createOffline();
			GameInfo info = new GameInfo(network, GameType.SURVIVAL, NetworkType.SOLO);
			IFileFactory fileFactory = new AssetFileFactory(getAssets());
			TriGame tGame = new TriGame(info, drawBoard, fileFactory);
			tGame.startGame();
		}
	}
}
