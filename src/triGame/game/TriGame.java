package triGame.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import objectIO.connections.p2pServer.client.ClientConnection;
import objectIO.connections.p2pServer.client.ClientHub;
import objectIO.connections.p2pServer.client.ConnectionEvent;
import tSquare.game.DrawBoard;
import tSquare.game.Game;
import tSquare.game.GameBoard;
import tSquare.game.GameBoard.ViewRect;
import tSquare.system.Display;
import tSquare.system.Network;
import tSquare.system.PeripheralInput;
import tSquare.util.PlaceHolder;
import tSquare.util.PopUp;
import triGame.game.entities.Person;
import triGame.game.guns.GunManager;
import triGame.game.safeArea.SafeAreaBoard;
import triGame.game.shopping.ShopManager;
import triGame.game.ui.UserInterface;


//TODO create NetFunction to receive request for initial player spawn location and color

public class TriGame extends Game{
	private final Display display;
	private final DrawBoard drawBoard;
	private final GameBoard gameBoard;
	private final PeripheralInput input;
	private final ShopManager shop;
	private final SafeAreaBoard safeBoard;
	private final GameOver gameOver;
	
	private final TiledBackground background;
	private final ManagerService managerService;
	private final GunManager gunManager;
	private final RoundHandler roundHandler;
	private final UserInterface ui;
	private final Person player;

	private boolean isGameOver = false;
	
	public TriGame(Network network) {
		super(network);
		Load.sprites();

		safeBoard = new SafeAreaBoard();
		gameOver = new GameOver();
		input = new PeripheralInput();
		shop = new ShopManager(500);
		display = new Display(500, 500, "Triangle Game - " + (network.isServer ? "Server" : "Client"));
		drawBoard = new DrawBoard(display.getWidth(), display.getHeight(), display);
		gameBoard = new GameBoard(5000, 5000, drawBoard);
		
		PlaceHolder<RoundHandler> phRoundHandler = new PlaceHolder<RoundHandler>();
		
		managerService = new ManagerService(managerController, safeBoard, input.keyboard,
				shop, particleController, network.isServer, phRoundHandler, userId);
		ui = new UserInterface(display, managerService, shop, input.mouse);
		roundHandler = new RoundHandler(managerService, input.keyboard, drawBoard,
				network.objController, network.isServer);
		if (network.isServer) {
			Map.createRandomMap(managerService, safeBoard);
		}
		gunManager = new GunManager(managerController, managerService, shop, input.keyboard);
		player = managerService.person.create(gameBoard.getWidth() / 2 - 50, gameBoard.getHeight() / 2 -100);
		background = new TiledBackground(drawBoard, player);
		drawBoard.addMouseListener(ui.attacher);
		drawBoard.addKeyListener(input.keyboard);
		drawBoard.addMouseMotionListener(input.mouse);
		drawBoard.requestFocus();
		managerService.building.addItemsToUI(ui);
		gunManager.addGunsToUI(ui);
		ui.arsenal.panel.switchGroup(ui.arsenal.towerGroup);
		display.pack();
		phRoundHandler.set(roundHandler);
		network.getClientInstance().conEvent = connectionEvent;
	}

	protected void logicLoop() {
		//System.out.println("free: " + (Runtime.getRuntime().freeMemory() / 1024 / 1024));
		int frameDelta = getDelta();
		
		if (isGameOver == false) {
			managerService.person.performLogic(frameDelta);
			roundHandler.performLogic(frameDelta);
			gunManager.performLogic(frameDelta);
			ui.attacher.performLogic(frameDelta);
		}
		
		managerService.zombie.performLogic(frameDelta);
		managerService.building.performLogic(frameDelta);
		gameBoard.centerViewWindowCordinates(player.getCenterX(), player.getCenterY());
		managerService.projectile.performLogic(frameDelta);
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
		safeBoard.draw(g, rect);
		managerService.person.draw(g, rect);
		ui.attacher.draw(g, rect);
		roundHandler.draw(g, rect);
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
		g.drawString("$" + shop.points, ix, iy);
		g.drawString("Round " + roundHandler.getRoundNumber(), ix, iy + 1 * 15);
		g.drawString("Killed " + managerService.zombie.getZombiesKilled() + " zombies", ix, iy + 2 * 15);
		g.drawString(getCurrentFps() + "FPS", ix, iy + 3 * 15);
	}
}
