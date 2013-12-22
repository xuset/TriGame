package triGame.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import objectIO.connections.sockets.p2pServer.client.ClientConnection;
import objectIO.connections.sockets.p2pServer.client.ClientHub;
import objectIO.connections.sockets.p2pServer.client.ConnectionEvent;
import objectIO.netObject.NetVar;
import tSquare.game.DrawBoard;
import tSquare.game.Game;
import tSquare.game.GameBoard;
import tSquare.game.GameBoard.ViewRect;
import tSquare.imaging.Sprite;
import tSquare.system.Display;
import tSquare.system.PeripheralInput;
import tSquare.util.PopUp;
import triGame.game.entities.Person;
import triGame.game.entities.PointParticle;
import triGame.game.entities.buildings.Building;
import triGame.game.guns.GunManager;
import triGame.game.shopping.ShopManager;
import triGame.game.ui.UserInterface;
import triGame.intro.GameInfo;

public class TriGame extends Game{
	private final Display display;
	private final DrawBoard drawBoard;
	private final GameBoard gameBoard;
	private final PeripheralInput input;
	private final ShopManager shop;
	private final GameOver gameOver;
	
	private final TiledBackground background;
	private final ManagerService managerService;
	private final GameMode gameMode;
	private final GunManager gunManager;
	private final UserInterface ui;
	private final Person player;

	private boolean isGameOver = false;
	
	public TriGame(GameInfo gameInfo) {
		super(gameInfo.getNetwork());
		NetVar.nBool startGame = new NetVar.nBool(false, "start", network.objController);
		Load.sprites();

		gameOver = new GameOver();
		input = new PeripheralInput();
		shop = new ShopManager(300);
		display = new Display(500, 500, "Attack of the Triangles! - " + (network.isServer ? "Server" : "Client"));
		drawBoard = new DrawBoard(display.getWidth(), display.getHeight(), display);
		gameBoard = new GameBoard(Params.GAME_WIDTH, Params.GAME_HEIGHT, drawBoard);
	
		gameMode = GameMode.factoryCreator(gameInfo.getGameType(),
				shop, network.objController, network.isServer, input.keyboard);
		
		managerService = new ManagerService(managerController, input.keyboard,
				shop, particleController, network.isServer, userId, gameMode);
		ui = new UserInterface(display, drawBoard, managerService, shop, input.mouse);
		gameMode.setDependencies(managerService);
		if (network.isServer) {
			network.getServerInstance().accepter.stop();
			gameMode.createMap();
		}
		gunManager = new GunManager(managerController, managerService, shop, input.keyboard);
		player = gameMode.spawnInPlayer();
		background = new TiledBackground(display, player);
		drawBoard.addMouseListener(ui.attacher);
		drawBoard.addKeyListener(input.keyboard);
		drawBoard.addMouseMotionListener(input.mouse);
		drawBoard.requestFocus();
		managerService.building.addItemsToUI(ui);
		gunManager.addGunsToUI(ui);
		ui.arsenal.panel.switchGroup(ui.arsenal.towerGroup);
		display.pack();
		network.getClientInstance().conEvent = connectionEvent;
		if (network.isServer) {
			startGame.set(true);
		}
		while (!startGame.get()) {
			network.objController.distributeRecievedUpdates();
			try { Thread.sleep(10); } catch (Exception ex) { }
		}
	}

	protected void logicLoop() {
		//System.out.println("free: " + (Runtime.getRuntime().freeMemory() / 1024 / 1024));
		int frameDelta = getDelta();
		
		
		if (!isGameOver) {
			isGameOver = gameMode.isGameOver();
			gunManager.performLogic(frameDelta);
		}


		managerService.person.performLogic(frameDelta);
		managerService.dropPack.performLogic(frameDelta);
		managerService.zombie.performLogic(frameDelta);
		managerService.building.performLogic(frameDelta);
		managerService.projectile.performLogic(frameDelta);
		
		if (player.didMove() || player.isDead())
			ui.attacher.clearAttached();
		
		if (player.isDead() || isGameOver) {
			if (!managerService.building.interactives.isEmpty()) {
				Building HQ = managerService.building.interactives.get(0);
				gameBoard.centerViewWindowCordinates(HQ.getCenterX(), HQ.getCenterY());
				background.centerTo = HQ;
			}
		} else {
			ui.attacher.performLogic(frameDelta);
			gameBoard.centerViewWindowCordinates(player.getCenterX(), player.getCenterY());
			background.centerTo = player;
		}
		
		gameMode.performLogic(frameDelta);
	}
	
	private final ConnectionEvent connectionEvent = new ConnectionEvent() {
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
			PopUp popup = new PopUp(300, 200, "Disconnected",
					"Lost connection to server. The game has ended.");
			popup.display();
		}
	};

	protected void displayLoop() {
		drawBoard.clearBoard();
		
		Graphics2D g = (Graphics2D) drawBoard.getDrawing();
		ViewRect rect = gameBoard.viewable;
		
		background.draw(g, rect);
		managerService.pointWell.draw(g, rect);
		managerService.building.draw(g, rect);
		managerService.spawnHole.draw(g, rect);
		managerService.projectile.draw(g, rect);
		managerService.zombie.draw(g, rect);
		managerService.dropPack.draw(g, rect);
		managerService.person.draw(g, rect);
		gameMode.getSafeBoard().draw(g, rect);
		gameMode.draw(g, rect);
		ui.attacher.draw(g, rect);
		particleController.draw(g, rect);
		
		if (isGameOver)
			gameOver.draw(g, rect);
		
		drawStats();
		drawBoard.exportToScreen();
	}
	
	private static final Font statFont = new Font("Arial", Font.BOLD, 12);
	private static final Color statBackGround = new Color(30, 30, 30, 150);
	private void drawStats() {
		final int ix = 10;
		final int iy = 13;
		final int iw = 110;
		final int ih = 70;
		Graphics2D g = (Graphics2D) drawBoard.getDrawing();
		g.setColor(statBackGround);
		g.fillRoundRect(ix -10, iy - 13, iw, ih, 15, 15);
		g.setColor(Color.LIGHT_GRAY);
		g.setFont(statFont);
		g.drawString("  " + shop.getPointCount(), ix, iy);
		g.drawString("Round " + gameMode.getRoundNumber(), ix, iy + 1 * 15);
		g.drawString("Killed " + managerService.zombie.getZombiesKilled() + " zombies", ix, iy + 2 * 15);
		g.drawString(getCurrentFps() + "FPS", ix, iy + 3 * 15);
		Sprite point = Sprite.get(PointParticle.SPRITE_ID);
		point.draw(ix, iy - 8, g);
	}
	
	public void shutdown() {
		stopGame();
		network.disconnect();
		display.dispose();
	}
}
