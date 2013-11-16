package tSquare.game;

import java.awt.Graphics2D;

import tSquare.game.GameBoard.ViewRect;

public interface GameIntegratable {
	public void performLogic(int frameDelta);
	public void draw(Graphics2D g, ViewRect rect);
}
