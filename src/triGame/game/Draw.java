package triGame.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import tSquare.game.GameBoard.ViewRect;

public final class Draw {
	private static final Font standardFont = new Font("Arial", Font.BOLD, 25);
	
	private static final Font roundStartFont = new java.awt.Font("Arial", Font.BOLD, 25);
	public static void drawEnterToStart(Graphics2D g, ViewRect rect) {
		Draw.drawXCenteredText("\'Enter\' to start Round", Color.LIGHT_GRAY,
				roundStartFont, Color.black, 45, g, rect);
	}

	private static final Font roundNumberFont = new java.awt.Font("Arial", Font.BOLD, 30);
	public static void drawRoundNumber(Graphics2D g, ViewRect rect, int number, long timeDiff, long totalTime) {
		if (timeDiff > totalTime)
			return;
		
		String message = "ROUND " + number;
		double visibility = Math.max(0, -Math.abs( (timeDiff - totalTime/2.0) / (totalTime/2.0) ) + 1);
		Color color = new Color(255, 20, 20, (int) (255 * visibility));
		int y = (int) (rect.getHeight() / 2);
		drawXCenteredText(message, color, roundNumberFont, null, y, g, rect);
	}
	
	private static final Font youWinFont = new Font("Arial", Font.BOLD, 30);
	public static void drawYouWin(Graphics2D g, ViewRect rect) {
		int red = (int) (Math.random() * 200 + 50);
		int green = (int) (Math.random() * 200 + 50);
		int blue = (int) (Math.random() * 200 + 50);
		Color color = new Color(red, green, blue);
		drawXCenteredText("YOU WIN!", color, youWinFont, Color.black, 45, g, rect);
	}
	
	
	public static void drawEnterToStartVersus(Graphics2D g, ViewRect rect) {
		Draw.drawXCenteredText("\'Enter\' to finalize zones", Color.LIGHT_GRAY,
				standardFont, Color.black, 90, g, rect);
	}
	
	public static void drawPickASide(Graphics2D g, ViewRect rect) {
		Draw.drawXCenteredText("Pick your zone.", Color.red,
				standardFont, Color.black, 45, g, rect);
	}
	
	public static void drawXCenteredText(String text, Color color, Font font,
			Color shadow, int y, Graphics2D g, ViewRect rect) {
		
		g.setFont(font);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		int messageWidth = g.getFontMetrics().stringWidth(text);
		if (shadow != null) {
			g.setColor(shadow);
			g.drawString(text, (int) ((rect.getWidth() - messageWidth) / 2), y);
		}
		g.setColor(color);
		g.drawString(text, (int) ((rect.getWidth() - messageWidth) / 2 - 2), y + 2);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
	}
}
