package net.xuset.tSquare.game;

import java.io.IOException;

import net.xuset.tSquare.events.EventHandler;
import net.xuset.tSquare.game.entity.ManagerController;
import net.xuset.tSquare.game.particles.ParticleController;
import net.xuset.tSquare.system.Network;



public abstract class Game implements Runnable {
	private static final int milliToNano = 1000000;
	
	private boolean stopGame = false;
	private boolean pauseGame = false;
	private int delta = 0;
	private int currentFps = 0;

	protected int targetFps = 100;

	protected final long userId;
	protected final Network network;
	protected final ManagerController managerController;
	protected final ParticleController particleController;
	protected final EventHandler eventHandler;
	
	protected abstract void logicLoop();
	protected abstract void displayLoop();

	public int getDelta() { return delta; }
	public int getCurrentFps() { return currentFps; }
	public boolean isPaused() { return pauseGame; }
	public boolean isStopped() { return stopGame; }

	
	public Game(Network network) {
		if (network == null) {
			try {
				network = Network.startupServer(3000);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		this.network = network;
		userId = network.userId;
		managerController = new ManagerController(network.objController);
		particleController = new ParticleController(this);
		eventHandler = new EventHandler();
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
			network.objController.distributeAllUpdates();
			network.flush();
			displayLoop();
			
			pause = nextDisplayTime - System.nanoTime();
			sleepNanos(pause);
			delta = (int) ((System.nanoTime() - (nextDisplayTime - skipTime)) / milliToNano);
			if (delta > 100) delta = 100;
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
	
	@Override
	public void run() {
		startGame();
	}
	
	public void pauseGame(boolean pause) {
		pauseGame = pause;
	}
	
	public void stopGame() {
		stopGame = true;
	}
}
