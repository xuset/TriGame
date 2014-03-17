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
	private static final IFont standardFont = new TsFont("Arial", 25/50.0f, TsTypeFace.BOLD);
	
	private static final IFont roundStartFont = new TsFont("Arial", 25, TsTypeFace.BOLD);
	public static void drawEnterToStart(IGraphics g) {
		Draw.drawXCenteredText("\'Enter\' to start Round", TsColor.lightGray,
				roundStartFont, 0, TsColor.black, 0, 45, g);
	}
	
	public static void drawTapToStart(IGraphics g) {
		drawXCenteredText("Tap to start Round", TsColor.lightGray,
				roundStartFont, 0, TsColor.black, 0, 45, g);
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
		float y = (float) (view.getY() + view.getHeight() * 0.1 + g.getTextHeight());
		float x = (float) (view.getCenterX() - g.getTextWidth(message)/2);

		g.setColor(color);
		g.drawText(x, y, message);
	}
	
	private static final IFont youWinFont = new TsFont("Arial", 30/50.0f, TsTypeFace.BOLD);
	public static void drawYouWin(IGraphics g) {
		int red = (int) (Math.random() * 200 + 50);
		int green = (int) (Math.random() * 200 + 50);
		int blue = (int) (Math.random() * 200 + 50);
		TsColor color = new TsColor(red, green, blue);
		float offsetX = (float) g.getView().getX();
		float offsetY = (float) g.getView().getY() + 1;
		drawXCenteredText("YOU WIN!", color, youWinFont,
				2/50.0f, TsColor.black, offsetX, offsetY, g);
	}
	
	public static void drawPickASide(IGraphics g) {
		float offsetX = (float) g.getView().getX();
		float offsetY = (float) g.getView().getY() + 2;
		Draw.drawXCenteredText("Pick your zone.", TsColor.red,
				standardFont, 2/50.0f, TsColor.black, offsetX, offsetY, g);
	}
	
	public static void drawXCenteredText(String text, TsColor color, IFont font,
			float shadowOffset, TsColor shadow, float offsetX, float y, IGraphics g) {
		IRectangleR rect = g.getView();
		g.setFont(font);
		float messageWidth = g.getTextWidth(text);
		float x = (float) ((rect.getWidth() - messageWidth) / 2);
		drawText(text, color, font, shadowOffset, shadow, offsetX + x, y, g);
	}
	
	public static void drawText(String text, TsColor color, IFont font,
			float shadowOffset, TsColor shadow, float x, float y, IGraphics g) {
		
		g.setFont(font);
		if (shadow != null) {
			g.setColor(shadow);
			g.drawText(x + shadowOffset, y + shadowOffset, text);
		}
		g.setColor(color);
		g.drawText(x, y, text);
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
