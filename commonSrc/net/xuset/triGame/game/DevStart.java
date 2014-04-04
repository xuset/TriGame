package net.xuset.triGame.game;

import java.io.IOException;
import java.net.UnknownHostException;

import net.xuset.tSquare.files.IFileFactory;
import net.xuset.tSquare.math.IdGenerator;
import net.xuset.tSquare.system.IDrawBoard;
import net.xuset.tSquare.system.Network;
import net.xuset.triGame.game.GameInfo.NetworkType;
import net.xuset.triGame.game.GameMode.GameType;
import net.xuset.triGame.game.ui.DummyBrowserOpener;
import net.xuset.triGame.settings.Settings;

public class DevStart {
	
	public static void startSolo(GameType gameType, IDrawBoard db, IFileFactory ff,
			Settings settings) {
		
		GameInfo gInfo = new GameInfo(Network.createOffline(), gameType, NetworkType.SOLO);
		TriGame tGame = new TriGame(gInfo, db, ff, db.createInputListener(), settings,
				new DummyBrowserOpener());
		tGame.startGame();
	}
	
	public static void joinGame(GameType type, IDrawBoard db, IFileFactory ff, String ip,
			int port, Settings settings) throws UnknownHostException, IOException {
		
		Network n = Network.connectToServer(ip, port, IdGenerator.getNext());
		GameInfo gInfo = new GameInfo(n, type, NetworkType.SOLO);
		TriGame tGame = new TriGame(gInfo, db, ff, db.createInputListener(), settings,
				new DummyBrowserOpener());
		tGame.startGame();
	}
	
	public static void hostGame(GameType type, IDrawBoard db, IFileFactory ff, int port,
			int players, Settings settings) throws IOException {

		Network n = Network.startupServer(port);
		n.waitForClientsToConnect(players, Integer.MAX_VALUE);
		GameInfo gInfo = new GameInfo(n, type, NetworkType.SOLO);
		TriGame tGame = new TriGame(gInfo, db, ff, db.createInputListener(), settings,
				new DummyBrowserOpener());
		tGame.startGame();
	}
	

	
	public static void startLocalMultiplayer(final GameType type, final IDrawBoard db1,
			final IDrawBoard db2, final IFileFactory ff, final Settings settings) {
		
		Runnable serverTask = new Runnable() {
			@Override
			public void run() {
				try {
					hostGame(type, db1, ff, 3000, 1, settings);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		
		new Thread(serverTask).start();
		try {
			joinGame(type, db2, ff, "127.0.0.1", 3000, settings);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
