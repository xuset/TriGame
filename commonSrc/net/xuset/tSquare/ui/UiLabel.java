package net.xuset.tSquare.ui;

import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.IImage;
import net.xuset.tSquare.imaging.TsColor;
import net.xuset.tSquare.imaging.TsFont;

public class UiLabel extends UiComponent {
	private String text = "";
	private TsFont font = new TsFont("Arial", 20);
	private IImage image = null;
	private boolean drawShadow = false;
	private TsColor shadowColor = TsColor.black;
	
	private float hGap = 2.0f, vGap = 2.0f;
	
	public UiLabel() {
		super(0, 0, 0, 0);
	}
	
	public UiLabel(String text) {
		this();
		this.text = text;
	}
	
	public void setFont(TsFont f) { font = f; }
	public TsFont getFont() { return font; }
	
	public String getText() { return text; }
	public void setText(String t) { text = t; }
	
	public void setHorizontalGap(float hGap) { this.hGap = hGap; }
	public void setVerticalGap(float vGap) { this.vGap = vGap; }
	public float getHorizontalGap() { return hGap; }
	public float getVerticleGap() { return vGap; }
	
	public void setDrawShadow(boolean drawShadow) { this.drawShadow = drawShadow; }
	public boolean isDrawingShadow() { return drawShadow; }
	
	public void setShadowColor(TsColor shadowColor) { this.shadowColor = shadowColor; }
	public TsColor getShadowColor() { return shadowColor; }
	
	public IImage getImage() { return image; }
	public void setImage(IImage i) { image = i; }

	@Override
	public void draw(IGraphics g) {
		if (!isVisible())
			return;
		
		super.draw(g);
		g.setFont(getFont());
		calcDimensions(g);
		

		float drawX = getX() + getWidth() / 2;
		float drawY = getY() + getHeight() / 2;
		drawImage(g, drawX, drawY);
		drawText(g, drawX, drawY);
		
		//g.setColor(net.xuset.tSquare.imaging.TsColor.black);
		//g.drawRect(getX(), getY(), getWidth(), getHeight());
	}
	
	private void drawText(IGraphics g, float drawX, float drawY) {
		boolean antiAlias = g.isAntiAliasOn();
		float textX = drawX - g.getTextWidth(getText()) / 2;
		float textY = drawY + g.getTextHeight() / 4;
		
		if (image != null)
			textY += image.getHeight(g);
		
		g.setAntiAlias(true);
		if (isDrawingShadow()) {
			g.setColor(getShadowColor());
			g.drawText(textX + 2, textY + 2, text);
		}
		g.setColor(getForeground());
		g.drawText(textX, textY, text);
		
		g.setAntiAlias(antiAlias);
	}
	
	private void drawImage(IGraphics g, float drawX, float drawY) {
		if (image == null)
			return;
		
		g.drawImage(image,
				drawX - image.getWidth(g) / 2,
				drawY - image.getHeight(g) / 2);
	}
	
	private void calcDimensions(IGraphics g) {
		float w = 2 * getHorizontalGap();
		float h = 2 * getVerticleGap();
		
		if (text != null && !text.equals("")) {
			w += g.getTextWidth(text);
			h += g.getTextHeight();
		}
		
		
		if (image != null) {		
			w = Math.max(w, image.getWidth(g) + 2 * getHorizontalGap());
			h += image.getHeight(g);
			if (text != null && !text.equals(""))
				h += getVerticleGap();
		}
		
		setSize(w, h);
	}

}
