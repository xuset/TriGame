package triGame.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;

import objectIO.netObject.NetVar;
import objectIO.netObject.ObjController;
import objectIO.connections.Connection;
import tSquare.game.DrawBoard;
import tSquare.game.GameIntegratable;
import tSquare.game.GameBoard.ViewRect;
import tSquare.system.PeripheralInput;
import triGame.game.entities.Person;
import triGame.game.entities.PersonManager;
import triGame.game.entities.zombies.Zombie;
import triGame.game.entities.zombies.ZombieManager;
import triGame.game.shopping.ShopManager;

public class RoundHandler implements GameIntegratable{
	private static final Font roundNumberFont = new java.awt.Font("Arial", Font.BOLD, 30);
	private static final Font roundStartFont = new java.awt.Font("Arial", Font.BOLD, 25);
	
	private final ManagerService managerService;
	private final PeripheralInput.Keyboard keyboard;
	private final DrawBoard drawBoard;
	private final NetVar.nInt roundVar;
	private final NetVar.nBool roundOnGoing;
	private final boolean isServer;
	private final ShopManager shop;
	
	private int roundNumber = 0;
	private int zombiesToSpawn = 0;
	private boolean drawingFadingRoundNumber = false;
	private int maxPlayers = 0;
	
	public int getRoundNumber() { return roundNumber; }
	public boolean isRoundOnGoing() { return roundOnGoing.get(); }
	public boolean areZombiesSpawning() { return zombiesToSpawn > 0; }
	
	public RoundHandler(ManagerService service, PeripheralInput.Keyboard keyboard,
			DrawBoard drawBoard, ObjController netCon,  boolean isServer, ShopManager shop) {
		
		this.managerService = service;
		this.keyboard = keyboard;
		this.drawBoard = drawBoard;
		this.isServer = isServer;
		this.shop = shop;
		roundOnGoing = new NetVar.nBool(false,"roundOnGoing", netCon);
		roundVar = new NetVar.nInt(0, "round", netCon);
		roundVar.event = new NetVar.OnChange<Integer>() {
			@Override public void onChange(NetVar<Integer> var, Connection c) {
				setRound(var.get());
			}
		};
		
	}
	
	public void startNextRound() {
		startRound(++roundNumber);
	}

	public void startRound(int round) {
		setRound(round);
		roundVar.set(roundNumber);
		roundOnGoing.set(true);
	}
	
	private void setRound(int round) {
		roundNumber = round;
		onNewRound();
		PersonManager manager = managerService.person;
		if (manager.list.size() > maxPlayers) maxPlayers = manager.list.size();
		zombiesToSpawn = ((roundNumber * roundNumber) / 10 + roundNumber) * maxPlayers;
		drawingFadingRoundNumber = true;
	}
	
	private void onNewRound() {
		if (roundNumber == 1)
			return;
		
		PersonManager manager = managerService.person;
		Person player = manager.getPlayer();
		if (player != null && player.isDead()) {
			player.giveFullHealth();
		} else {
			shop.addPoints(50);
		}
	}
	
	@Override
	public void performLogic(int frameDelta) {
		if (isServer) {
			if (roundOnGoing.get()) {
				if (zombiesToSpawn == 0 && managerService.zombie.getZombiesAlive() == 0) {
					roundOnGoing.set(false);
				} else if (zombiesToSpawn > 0) {
					spawnIn();
				}
			} else if (keyboard.isPressed(KeyEvent.VK_ENTER)) {
					startNextRound();
			}
		}
	}
	
	private int getWaitDelay() { 
		int d =  (int) ((700 - 100 * maxPlayers) - roundNumber * 10.0);
		d = d < 100 ? 100 : d;
		return d;
	}
	
	private long nextSpawnTime = 0;
	private void spawnIn() {
		ZombieManager manager = managerService.zombie;
		if (roundNumber % 10 == 0) {//if boss round
			manager.createBoss(roundNumber);
			zombiesToSpawn = 0;
			return;
		}
		if (manager.getZombiesAlive() < Zombie.MAX_ZOMBIES && System.currentTimeMillis() > nextSpawnTime) {
			manager.create(roundNumber);
			zombiesToSpawn--;
			nextSpawnTime = System.currentTimeMillis() + getWaitDelay();
		}
	}

	
	@Override
	public void draw(Graphics2D g, ViewRect rect) {
		if (!roundOnGoing.get() && isServer) {
			//Graphics2D g = (Graphics2D) drawBoard.getDrawing();
			g.setFont(roundStartFont);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setColor(Color.BLACK);
			String message = "\'Enter\' to start Round";
			int messageWidth = g.getFontMetrics().stringWidth(message);
			g.drawString(message, (drawBoard.getWidth() - messageWidth) / 2, 45);
			g.setColor(Color.LIGHT_GRAY);
			g.drawString(message, (drawBoard.getWidth() - messageWidth) / 2 - 2, 47);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		}
		if (drawingFadingRoundNumber) {
			drawRoundNumber();
		}
	}
	
	private static final int elapsedTime = 3000;
	private long startTime = 0;
	private long timeSinceStart = 0;
	private void drawRoundNumber() {
		if (startTime == 0 && drawingFadingRoundNumber)
			startTime = System.currentTimeMillis();
		timeSinceStart = System.currentTimeMillis() - startTime;
		if (timeSinceStart < elapsedTime) {
			String message = "ROUND " + roundNumber;
			double visibility = Math.max(0, -Math.abs( (timeSinceStart - elapsedTime/2.0) / (elapsedTime/2.0) ) + 1);
			Graphics2D g = (Graphics2D) drawBoard.getDrawing();
			g.setColor(new Color(255, 20, 20, (int) (255 * visibility)));
			g.setFont(roundNumberFont);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			int messageWidth = g.getFontMetrics().stringWidth(message);
			g.drawString(message, (drawBoard.getWidth() - messageWidth) / 2, drawBoard.getHeight() / 2);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		} else {
			drawingFadingRoundNumber = false;
			startTime = 0l;
		}
		
	}
}
