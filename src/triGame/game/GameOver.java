package triGame.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import tSquare.game.DrawBoard;
import tSquare.game.GameIntegratable;


public class GameOver implements GameIntegratable{
	private DrawBoard drawBoard;
	private static final Font font = new Font("Arial", Font.BOLD, 70);
	private static final Color color = new Color(255,0,0);
	public GameOver(DrawBoard drawBoard) {
		this.drawBoard = drawBoard;
	}
	public void performLogic() { }

	public void draw() {
		String message = "GAME  OVER!";
		Graphics2D g = (Graphics2D) drawBoard.getDrawing();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setFont(font);
		int width = g.getFontMetrics().stringWidth(message);
		g.setColor(Color.black);
		g.drawString(message, drawBoard.getWidth() / 2 - width / 2 + 4, drawBoard.getHeight() / 2 + 4);
		g.setColor(color);
		g.drawString(message, drawBoard.getWidth() / 2 - width / 2, drawBoard.getHeight() / 2);

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
	}

}
