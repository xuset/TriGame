package triGame.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import tSquare.game.GameIntegratable;
import tSquare.game.GameBoard.ViewRect;


public class GameOver implements GameIntegratable{
	private static final Font font = new Font("Arial", Font.BOLD, 70);
	private static final Color color = new Color(255,0,0);
	
	@Override
	public void performLogic(int frameDelta) { }

	@Override
	public void draw(Graphics2D g, ViewRect rect) {
		String message = "GAME  OVER!";
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setFont(font);
		
		int width = g.getFontMetrics().stringWidth(message);
		g.setColor(Color.black);
		g.drawString(message, (int) rect.getWidth() / 2 - width / 2 + 4, (int) rect.getHeight() / 2 + 4);
		g.setColor(color);
		g.drawString(message, (int) rect.getWidth() / 2 - width / 2, (int) rect.getHeight() / 2);

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
	}

}
