package net.xuset.triGame.game;

import net.xuset.tSquare.imaging.IFont;
import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.Sprite;
import net.xuset.tSquare.imaging.TsColor;
import net.xuset.tSquare.imaging.TsFont;
import net.xuset.tSquare.imaging.TsTypeFace;
import net.xuset.tSquare.math.rect.IRectangleR;
import net.xuset.triGame.game.entities.PointParticle;


public final class Draw {
	private static final IFont standardFont = new TsFont("Arial", 25, TsTypeFace.BOLD);
	
	private static final IFont roundStartFont = new TsFont("Arial", 25, TsTypeFace.BOLD);
	public static void drawEnterToStart(IGraphics g) {
		Draw.drawXCenteredText("\'Enter\' to start Round", TsColor.lightGray,
				roundStartFont, TsColor.black, 45, g);
	}
	
	public static void drawTapToStart(IGraphics g) {
		drawXCenteredText("Tap to start Round", TsColor.lightGray,
				roundStartFont, TsColor.black, 45, g);
	}

	private static final TsFont roundNumberFont = new TsFont("Arial", 30, TsTypeFace.BOLD);
	public static void drawRoundNumber(IGraphics g, int number, long timeDiff, long totalTime) {
		if (timeDiff > totalTime)
			return;
		
		String message = "ROUND " + number;
		double visibility = Math.max(0, -Math.abs( (timeDiff - totalTime/2.0) / (totalTime/2.0) ) + 1);
		TsColor color = new TsColor(255, 20, 20, (int) (255 * visibility));
		int y = (int) (g.getView().getHeight() / 2);
		drawXCenteredText(message, color, roundNumberFont, null, y, g);
	}
	
	private static final IFont youWinFont = new TsFont("Arial", 30, TsTypeFace.BOLD);
	public static void drawYouWin(IGraphics g) {
		int red = (int) (Math.random() * 200 + 50);
		int green = (int) (Math.random() * 200 + 50);
		int blue = (int) (Math.random() * 200 + 50);
		TsColor color = new TsColor(red, green, blue);
		drawXCenteredText("YOU WIN!", color, youWinFont, TsColor.black, 45, g);
	}
	
	
	public static void drawEnterToStartVersus(IGraphics g) {
		Draw.drawXCenteredText("\'Enter\' to finalize zones", TsColor.lightGray,
				standardFont, TsColor.black, 90, g);
	}
	
	public static void drawPickASide(IGraphics g) {
		Draw.drawXCenteredText("Pick your zone.", TsColor.red,
				standardFont, TsColor.black, 45, g);
	}

	private static final IFont gameOverFont = new TsFont("Arial", 70, TsTypeFace.BOLD);
	private static final TsColor gameOverColor = new TsColor(255,0,0);
	public static void drawGameOver(IGraphics g) {
		int centerY = (int) (g.getView().getHeight() / 2 - 10);
		Draw.drawXCenteredText("GAME  OVER!", gameOverColor, gameOverFont, TsColor.black, centerY, g);
	}
	
	public static void drawXCenteredText(String text, TsColor color, IFont font,
			TsColor shadow, int y, IGraphics g) {
		IRectangleR rect = g.getView();
		
		g.setFont(font);
		float messageWidth = g.getTextWidth(text);
		if (shadow != null) {
			g.setColor(shadow);
			g.drawText((int) ((rect.getWidth() - messageWidth) / 2), y, text);
		}
		g.setColor(color);
		g.drawText((int) ((rect.getWidth() - messageWidth) / 2 - 2), y + 2, text);
	}

	private static final IFont statFont = new TsFont("Arial", 12, TsTypeFace.BOLD);
	private static final TsColor statBackGround = new TsColor(30, 30, 30, 150);
	public static void drawStats(int points, int round, int killed, int fps, IGraphics g) {
		final int ix = 10;
		final int iy = 13;
		final int iw = 110;
		final int ih = 70;
		g.setColor(statBackGround);
		g.fillRoundedRect(ix -10, iy - 13, iw, ih, 15, 15);
		g.setColor(TsColor.lightGray);
		g.setFont(statFont);
		g.drawText(ix, iy, "  " + points);
		g.drawText(ix, iy + 1 * 15, "Round " + round);
		g.drawText(ix, iy + 2 * 15, "Killed " + killed + " zombies");
		g.drawText(ix, iy + 3 * 15, fps + "FPS");
		Sprite point = Sprite.get(PointParticle.SPRITE_ID);
		point.draw(ix, iy - 8, g);
	}
}
