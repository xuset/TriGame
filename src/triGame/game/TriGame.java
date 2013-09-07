package triGame.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import tSquare.game.DrawBoard;
import tSquare.game.Game;
import tSquare.game.GameBoard;
import tSquare.system.Display;
import tSquare.system.Network;
import tSquare.system.PeripheralInput;
import triGame.game.entities.Person;
import triGame.game.entities.PersonManager;
import triGame.game.entities.SpawnHoleManager;
import triGame.game.entities.building.BuildingManager;
import triGame.game.entities.building.HeadQuarters;
import triGame.game.entities.wall.WallManager;
import triGame.game.entities.zombies.ZombieManager;
import triGame.game.playerInterface.PlayerInterface;
import triGame.game.projectile.gun.GunManager;
import triGame.game.safeArea.SafeAreaBoard;
import triGame.game.shopping.ShopManager;


//TODO create NetFunction to receive request for initial player spawn location and color

public class TriGame extends Game{
	public static final int BLOCK_WIDTH = 50;
	public static final int BLOCK_HEIGHT = 50;
	
	private Display display;
	private DrawBoard drawBoard;
	private GameBoard gameBoard;
	private PeripheralInput input = new PeripheralInput();
	private TiledBackground background;
	private PlayerInterface playerInterface;
	private boolean isGameOver = false;
	
	public PersonManager personManager;
	public BuildingManager buildingManager;
	public WallManager wallManager;
	public ZombieManager zombieManager;
	public SpawnHoleManager spawnHoleManager;
	public GunManager gunManager;
	public ShopManager shop = new ShopManager(500);
	public RoundHandler roundHandler;
	public Person player;
	public SafeAreaBoard safeBoard;
	public GameOver gameOver;
	
	public Display getDisplay() { return display; }
	public DrawBoard getDrawBoard() { return drawBoard; }
	public GameBoard getGameBoard() { return gameBoard; }
	public PeripheralInput getInput() { return input; }
	
	public TriGame() {
	}
	public TriGame(Network network, boolean wait) {
		super(network);
		if (wait && network.isServer()) {
			while (network.getHub().getAllConnections().size() == 0) { try { Thread.sleep(10); } catch (Exception ex) { } }
		}
	}
	
	public void load() {
		display = new Display(500, 500, "Triangle Game - " + (getNetwork().isServer()? "Server" : "Client"));
		drawBoard = new DrawBoard(display.getWidth() - PlayerInterface.WIDTH, display.getHeight(), display);
		gameBoard = new GameBoard(5000, 5000, drawBoard);
		drawBoard.addKeyListener(input.keyboard);
		drawBoard.addMouseMotionListener(input.mouse);
		drawBoard.requestFocus();
		personManager = new PersonManager(managerController, this, gameBoard);
		buildingManager = new BuildingManager(managerController, this, gameBoard, zombieManager);
		wallManager = new WallManager(managerController, this, gameBoard);
		spawnHoleManager = new SpawnHoleManager(managerController, gameBoard);
		zombieManager = new ZombieManager(managerController, this, gameBoard);
		roundHandler = new RoundHandler(zombieManager, input.keyboard, drawBoard, getNetwork().getObjController(), getNetwork().isServer(), personManager);
		safeBoard = new SafeAreaBoard(gameBoard);
		Load.sprites();
		if (getNetwork().isServer()) {
			HeadQuarters.create(gameBoard.getWidth() / 2 - 50, gameBoard.getHeight() / 2 - 50, buildingManager);
			Map.createRandomMap(wallManager, buildingManager, spawnHoleManager, safeBoard);
		}
		player = Person.create(gameBoard.getWidth() / 2 - 50, gameBoard.getHeight() / 2 -100, personManager);
		gunManager = new GunManager(this, gameBoard);
		background = new TiledBackground(gameBoard, drawBoard, player, BLOCK_WIDTH, BLOCK_HEIGHT);
		playerInterface = new PlayerInterface(this);
		drawBoard.addMouseListener(playerInterface.getAttacher());
		gameOver = new GameOver(drawBoard);
	}

	protected void logicLoop() {
		System.out.println("free: " + (Runtime.getRuntime().freeMemory() / 1024 / 1024));
		personManager.performLogic();
		gameBoard.centerViewWindowCordinates(player.getCenterX(), player.getCenterY());
		roundHandler.performLogic();
		zombieManager.performLogic();
		buildingManager.performLogic();
		wallManager.performLogic();
		gunManager.performLogic();
		playerInterface.displayPointsAndRound();
		spawnHoleManager.completeListModifications();
	}

	protected void displayLoop() {
		drawBoard.clearBoard();
		background.draw();
		wallManager.draw();
		buildingManager.draw();
		spawnHoleManager.draw();
		gunManager.draw();
		zombieManager.draw();
		safeBoard.draw();
		personManager.draw();
		playerInterface.getAttacher().draw();
		roundHandler.draw();
		if (isGameOver)
			gameOver.draw();
		drawStats();
		drawBoard.exportToScreen();
	}
	
	public void setGameOver() {
		isGameOver = true;
		if (personManager.getList().contains(player))
			player.remove();
	}
	
	private static final Font statFont = new Font("Arial", Font.BOLD, 12);
	private void drawStats() {
		Graphics g = drawBoard.getDrawing();
		g.setColor(Color.GREEN);
		g.setFont(statFont);
		g.drawString("points: " + shop.points, 50, 50);
		g.drawString("FPS: " + getCurrentFps(), 50, 65);
		g.drawString("Delta: " + getDelta(), 50, 80);
	}
}
