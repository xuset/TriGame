package net.xuset.triGame.game;

import net.xuset.objectIO.connections.sockets.p2pServer.client.ClientConnection;
import net.xuset.objectIO.connections.sockets.p2pServer.client.ClientHub;
import net.xuset.objectIO.connections.sockets.p2pServer.client.ConnectionEvent;
import net.xuset.objectIO.netObject.NetVar;
import net.xuset.tSquare.files.IFileFactory;
import net.xuset.tSquare.game.Game;
import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.IImageFactory;
import net.xuset.tSquare.imaging.ScaledGraphics;
import net.xuset.tSquare.imaging.ScaledImageFactory;
import net.xuset.tSquare.imaging.TransformedGraphics;
import net.xuset.tSquare.imaging.TsColor;
import net.xuset.tSquare.math.rect.IRectangleW;
import net.xuset.tSquare.math.rect.Rectangle;
import net.xuset.tSquare.system.IDrawBoard;
import net.xuset.tSquare.system.input.InputHolder;
import net.xuset.tSquare.system.sound.SoundStore;
import net.xuset.triGame.game.GameInfo.NetworkType;
import net.xuset.triGame.game.entities.Person;
import net.xuset.triGame.game.entities.TriangleSpriteCreator;
import net.xuset.triGame.game.entities.buildings.Building;
import net.xuset.triGame.game.entities.buildings.BuildingGetter;
import net.xuset.triGame.game.guns.GunManager;
import net.xuset.triGame.game.shopping.ShopDrawer;
import net.xuset.triGame.game.shopping.ShopManager;
import net.xuset.triGame.game.ui.UserInterface;
import net.xuset.triGame.game.ui.gameInput.IGameInput;
import net.xuset.triGame.settings.Settings;



public class TriGame extends Game{
	private final IDrawBoard drawBoard;
	private final ShopManager shop;
	private final ShopDrawer shopDrawer;
	private final IGameInput gameInput;
	private final GameGrid gameGrid;
	private final TiledBackground background;
	private final int blockSize; //px
	private final Settings settings;
	
	private final ManagerService managerService;
	private final GameMode gameMode;
	private final GunManager gunManager;
	private final Person player;
	private final UserInterface ui;
	private final IRectangleW viewableRect = new Rectangle();

	private boolean isGameOver = false;
	
	public TriGame(GameInfo gameInfo, IDrawBoard drawBoard, IFileFactory fileFactory,
			InputHolder input, Settings settings) {
		
		super(gameInfo.getNetwork());
		this.settings = settings;
		NetVar.nBool startGame = new NetVar.nBool(false, "start", network.objController);
		
		blockSize = settings.blockSize;
		Load.loadResources(blockSize, fileFactory);

		this.drawBoard = drawBoard;
		shop = new ShopManager(300);
		shopDrawer = new ShopDrawer(shop.observer());
		IImageFactory scaledFactory = new ScaledImageFactory(blockSize);
		gameGrid = new GameGrid(100, 100);
		background = new TiledBackground(scaledFactory);
		
		BuildingGetter buildingGetter = new BuildingGetter();
		PointConverter pConv = new PointConverter(viewableRect, blockSize);
		ui = new UserInterface(input, pConv, shop, buildingGetter, settings);
		gameInput = ui.getGameInput();
	
		gameMode = GameMode.factoryCreator(gameInfo.getGameType(),
				shop, gameGrid, network.objController, network.isServer,
				gameInput.getRoundInput(), scaledFactory);
		
		managerService = new ManagerService(managerController, gameInput.getPlayerInput(),
				shop, gameGrid, particleController, network.isServer, userId, gameMode,
				new TriangleSpriteCreator(blockSize), buildingGetter);
		gameMode.setDependencies(managerService);
		
		
		gunManager = new GunManager(managerService, shop, gameInput.getGunInput());
		player = gameMode.spawnInPlayer();
		
		managerService.building.addItemsToUI(ui.getArsenalItemAdder());
		gunManager.addGunsToUI(ui.getArsenalItemAdder());
		
		if (network.isServer)
			gameMode.createMap();
		if (gameInfo.getNetworkType() != NetworkType.SOLO)
			network.getClientInstance().conEvent = new GameConnectionEvent();
		
		if (network.isServer) {
			startGame.set(true);
		}
		while (!startGame.get()) {
			network.objController.distributeRecievedUpdates();
			try { Thread.sleep(10); } catch (Exception ex) { }
		}
		gameMode.onGameStart();
	}

	@Override
	protected void logicLoop() {
		//System.out.println("free: " + (Runtime.getRuntime().freeMemory() / 1024 / 1024));
		int frameDelta = getDelta();
		ui.setGameOver(isGameOver);
		
		SoundStore.setMuteOnAll(!settings.enableSound);
		
		if (!isGameOver && !ui.isPaused()) {
			managerService.person.update(frameDelta);
			isGameOver = gameMode.isGameOver();
			gunManager.update(frameDelta);
		}


		managerService.dropPack.update(frameDelta);
		managerService.zombie.update(frameDelta);
		managerService.building.update(frameDelta);
		managerService.projectile.update(frameDelta);
		
		if (player.didMove() || player.isDead() || ui.isPaused())
			ui.clearAttachedBuildings();
		
		if (player.isDead() || isGameOver) {
			if (!managerService.building.interactives.isEmpty()) {
				Building HQ = managerService.building.interactives.get(0);
				viewableRect.setCenter(HQ.getCenterX(), HQ.getCenterY());
				background.setCenter(HQ);
			}
		} else {
			viewableRect.setCenter(player.getCenterX(), player.getCenterY());
			background.setCenter(player);
		}
		gameMode.update(frameDelta);
		gameInput.update(frameDelta);
		
		if (ui.exitGameRequested())
			shutdown();
	}

	@Override
	protected void displayLoop() {
		IGraphics g = drawBoard.getGraphics();
		if (g == null)
			return;
		
		ScaledGraphics scaleG = new ScaledGraphics(g, blockSize);
		viewableRect.setDimensions(scaleG.getView().getWidth(), scaleG.getView().getHeight());
		IGraphics transG = new TransformedGraphics(scaleG, viewableRect);
		
		g.setColor(TsColor.darkGray);
		g.fillRect(0, 0, (float) g.getView().getWidth(), (float) g.getView().getHeight());
		background.draw(transG);
		managerService.pointWell.draw(transG);
		managerService.building.draw(transG);
		managerService.spawnHole.draw(transG);
		managerService.projectile.draw(transG);
		managerService.zombie.draw(transG);
		managerService.dropPack.draw(transG);
		managerService.person.draw(transG);
		gameMode.draw(transG);
		particleController.draw(transG);
		ui.draw(g, transG, blockSize);
		
		Draw.drawStats(shop.getPointCount(), gameMode.getRoundNumber(),
				managerService.zombie.getZombiesKilled(), getCurrentFps(), scaleG);
		shopDrawer.draw(getDelta(), scaleG);
		drawBoard.flushScreen();
	}
	
	@Override
	public void pauseGame(boolean pause) {
		super.pauseGame(pause);
		
		ui.showPauseScreen(pause);
	}
	
	public void shutdown() {
		stopGame();
		network.disconnect();
	}
	
	private class GameConnectionEvent implements ConnectionEvent {
		@Override public void onConnection(ClientHub hub, ClientConnection connection) { }

		@Override
		public void onDisconnection(ClientHub hub, ClientConnection connection) {
			if (network.isServer) {
				Person p = managerService.person.getByUserId(connection.getEndId());
				if (p != null)
					p.remove();
			}
		}
		
		@Override
		public void onServerDisconnect(ClientHub hub) {
			if (TriGame.this.isStopped())
				return;
			isGameOver = true;
			//PopUp popup = new PopUp(300, 200, "Disconnected",
			//		"Lost connection to server. The game has ended.");
			System.err.println("Lost connection to server. The game has ended.");
			//popup.display();
		}
	}
}
