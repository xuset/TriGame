package triGame.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;

import objectIO.netObject.NetObjectController;
import objectIO.netObject.NetVar;
import objectIO.netObject.NetVarChange;
import objectIO.connections.Connection;

import tSquare.game.DrawBoard;
import tSquare.game.GameIntegratable;
import tSquare.system.PeripheralInput;
import triGame.game.entities.PersonManager;
import triGame.game.entities.zombies.Zombie;
import triGame.game.entities.zombies.ZombieManager;

public class RoundHandler implements GameIntegratable{
	private static final Font roundNumberFont = new java.awt.Font("Arial", Font.BOLD, 30);
	private static final Font roundStartFont = new java.awt.Font("Arial", Font.BOLD, 25);
	private ZombieManager zombieManager;
	private PeripheralInput.Keyboard keyboard;
	private DrawBoard drawBoard;
	private NetVar roundVar;
	private boolean isServer;
	private int roundNumber = 0;
	private boolean roundOnGoing = false;
	private int zombiesToSpawn = 0;
	private boolean drawingFadingRoundNumber = false;
	private PersonManager personManager;
	private int maxPlayers = 0;
	
	public int getRoundNumber() { return roundNumber; }
	public boolean isRoundOnGoing() { return roundOnGoing; }
	
	public RoundHandler(ZombieManager manager, PeripheralInput.Keyboard keyboard, DrawBoard drawBoard, NetObjectController netCon,  boolean isServer, PersonManager personManager) {
		zombieManager = manager;
		this.keyboard = keyboard;
		this.drawBoard = drawBoard;
		this.isServer = isServer;
		this.personManager = personManager;
		roundVar = new NetVar(netCon, "round");
		roundVar.onChangeEvent = new NetVarChange() {
			public void onChange(NetVar v, Connection c) {
				setRound(v.getToInt());
			}
		};
		
	}
	
	public void startNextRound() {
		startRound(++roundNumber);
	}

	public void startRound(int round) {
		setRound(round);
		roundVar.set(roundNumber);
	}
	
	private void setRound(int round) {
		roundNumber = round;
		roundOnGoing = true;
		if (personManager.getList().size() > maxPlayers) maxPlayers = personManager.getList().size();
		zombiesToSpawn = ((roundNumber * roundNumber) / 10 + roundNumber) * maxPlayers;
		drawingFadingRoundNumber = true;
	}
	
	public void performLogic() {
		if (isServer) {
			if (roundOnGoing) {
				if (zombiesToSpawn == 0 && zombieManager.getZombiesAlive() == 0) {
					roundOnGoing = false;
				} else if (zombiesToSpawn > 0) {
					spawnIn();
				}
			} else if (keyboard.isPressed(KeyEvent.VK_ENTER)) {
					startNextRound();
			}
		}
	}
	
	private static final int initialWaitDelay = 500;
	private long nextSpawnTime = 0;
	private void spawnIn() {
		if (zombieManager.getZombiesAlive() < Zombie.MAX_ZOMBIES && System.currentTimeMillis() > nextSpawnTime) {
			zombieManager.create();
			zombiesToSpawn--;
			nextSpawnTime = System.currentTimeMillis() + initialWaitDelay;
		}
	}

	
	public void draw() {
		if (roundOnGoing == false && isServer) {
			Graphics2D g = (Graphics2D) drawBoard.getDrawing();
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
