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

	private static final TsFont roundNumberFont = new TsFont("Arial", 60/50.0f, TsTypeFace.BOLD);
	public static void drawRoundNumber(IGraphics g, int number, long timeDiff, long totalTime) {
		if (timeDiff > totalTime)
			return;
		IRectangleR view = g.getView();
		String message = "ROUND " + number;
		double visibility = Math.max(0, -Math.abs( (timeDiff - totalTime/2.0) / (totalTime/2.0) ) + 1);
		TsColor color = new TsColor(230, 20, 20, (int) (255 * visibility));
		g.setFont(roundNumberFont);
		float y = (float) (view.getCenterY() - g.getTextHeight()/4);
		float x = (float) (view.getCenterX() - g.getTextWidth(message)/2);

		g.setColor(color);
		g.drawText(x, y, message);
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

	private static final IFont gameOverFont = new TsFont("Arial", 70/50.f, TsTypeFace.BOLD);
	private static final TsColor gameOverColor = new TsColor(255,0,0);
	public static void drawGameOver(IGraphics g) {
		g.setFont(gameOverFont);
		String text = "GAME  OVER!";
		float textWidth = g.getTextWidth(text);
		float x = (float) (g.getView().getCenterX() - textWidth/2);
		float y = (float) (g.getView().getCenterY());
		g.setColor(TsColor.black);
		g.drawText(x + 2/50.0f, y + 2/50.0f, text);
		g.setColor(gameOverColor);
		g.drawText(x, y, text);
	}
	
	public static void drawXCenteredText(String text, TsColor color, IFont font,
			TsColor shadow, float y, IGraphics g) {
		IRectangleR rect = g.getView();
		g.setFont(font);
		float messageWidth = g.getTextWidth(text);
		float x = (float) ((rect.getWidth() - messageWidth) / 2);
		drawText(text, color, font, shadow, x, y, g);
	}
	
	public static void drawText(String text, TsColor color, IFont font, TsColor shadow,
			float x, float y, IGraphics g) {
		
		g.setFont(font);
		if (shadow != null) {
			g.setColor(shadow);
			g.drawText(x, y, text);
		}
		g.setColor(color);
		g.drawText(x - 2, y + 2, text);
	}

	private static final IFont statFont = new TsFont("Arial", 12/50.0f, TsTypeFace.BOLD);
	private static final TsColor statBackGround = new TsColor(30, 30, 30, 150);
	public static void drawStats(int points, int round, int killed, int fps, IGraphics g) {
		final float gutter = 1/5.0f;
		final String[] strings = new String[] {
				"  " + points,
				"Round " + round,
				"Killed " + killed + " zombies",
				fps + "FPS",
		};
		
		g.setFont(statFont);
		
		float stringHeight = g.getTextHeight();
		float stringWidth = g.getTextWidth(strings[0]);
		for (String s : strings) {
			float w = g.getTextWidth(s);
			if (w > stringWidth)
				stringWidth = w;
		}

		g.setColor(statBackGround);
		g.fillRoundedRect(gutter, gutter,
				stringWidth + 2 * gutter,
				stringHeight * strings.length + gutter,
				15/50.f, 15/50.f);
		

		g.setColor(TsColor.lightGray);
		for (int i = 0; i < strings.length; i++)
			g.drawText(2 * gutter, 2 * gutter + (stringHeight) * i + stringHeight / 2, strings[i]);
		
		Sprite pointParticle = Sprite.get(PointParticle.SPRITE_ID);
		pointParticle.draw(2 * gutter, 2 * gutter, g);
	}
}
