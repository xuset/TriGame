package tSquare.game;

import java.io.IOException;

import tSquare.events.EventHandler;
import tSquare.game.entity.EntityCreater;
import tSquare.math.IdGenerator;
import tSquare.system.Network;

//TODO implement a timer based game loop

public abstract class Game implements Runnable {
	private static final int milliToNano = 1000000;
	
	private boolean stopGame = false;
	private boolean pauseGame = false;
	private int delta = 0;
	private int currentFps = 0;
	private long userId;
	
	EntityCreater entityCreater;
	
	protected IdGenerator idGenerator = new IdGenerator();
	protected Network network;

	public ManagerController managerController;
	public EventHandler eventHandler;
	public int targetFps = 100;
	
	protected abstract void logicLoop();
	protected abstract void displayLoop();

	public IdGenerator getIdGenerator() { return idGenerator; }
	public Network getNetwork() { return network; }
	public long getUserId() { return userId; }
	public int getDelta() { return delta; }
	public int getCurrentFps() { return currentFps; }
	public boolean isPaused() { return pauseGame; }
	public boolean isStopped() { return stopGame; }
	
	public Game() {
		try {
			network = Network.startupServer(3000, 3l);
		} catch (IOException e) {
			e.printStackTrace();
		}
		construct();
	}
	
	public Game(Network network) {
		this.network = network;
		construct();
	}
	
	private void construct() {
		userId = network.getUserId();
		managerController = new ManagerController(this);
		eventHandler = new EventHandler();
		entityCreater = new EntityCreater(managerController, network.getObjController());
	}
	
	public void startGame() {
		pauseGame = false;
		stopGame = false;
		int skipTime = 1000/targetFps * milliToNano;
		long nextDisplayTime = System.nanoTime() + skipTime;
		long pause;
		delta = skipTime / milliToNano;
		while(stopGame == false) {
			skipTime = 1000/targetFps * milliToNano;
			if (pauseGame == false) {
				logicLoop();
				eventHandler.handleEvents();
			}
			network.getObjController().distributeRecievedUpdates();
			displayLoop();
			pause = nextDisplayTime - System.nanoTime();
			sleepNanos(pause);
			delta = (int) ((System.nanoTime() - (nextDisplayTime - skipTime)) / milliToNano);
			nextDisplayTime = System.nanoTime() + skipTime;
			currentFps = 1000 / delta;
		}
	}
	
	private static final long precision = 8 * milliToNano; //in nanoseconds
    public static void sleepNanos (long nanoDuration) {
        final long stopTime = System.nanoTime() + nanoDuration;
        long timeLeft = nanoDuration;
        while(timeLeft > 0) {
            if (timeLeft > precision)
				try { Thread.sleep (1); } catch (InterruptedException e) { e.printStackTrace(); }
			else
				try { Thread.sleep (0); } catch (InterruptedException e) { e.printStackTrace(); }
            timeLeft = stopTime - System.nanoTime();
        }

    }
	
	/*private static void sleep(long milliseconds) {
		if (milliseconds > 0) {
			long start = System.currentTimeMillis();
			while(start + milliseconds > System.currentTimeMillis()) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
		}
	}*/
	
	public void run() {
		startGame();
	}
	
	public void pauseGame(boolean pause) {
		pauseGame = pause;
	}
	
	public void stopGame() {
		stopGame = false;
	}
}
