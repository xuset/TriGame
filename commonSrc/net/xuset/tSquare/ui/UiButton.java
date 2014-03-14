package net.xuset.tSquare.ui;

import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.TsColor;
import net.xuset.tSquare.imaging.TsFont;
import net.xuset.tSquare.system.input.mouse.MouseAction;
import net.xuset.tSquare.system.input.mouse.TsMouseEvent;

public class UiButton extends UiComponent {
	private String text = "";
	private TsFont font = new TsFont("Arial", 15);
	private boolean isEnabled = true;
	private int borderSize = 4;
	private long clickTime = 0;
	
	public UiButton() {
		super(0, 0, 80, 40);
		setBackground(TsColor.lightGray);
		setForeground(TsColor.black);
	}
	
	public UiButton(String text) {
		this();
		this.text = text;
	}
	
	public void setFont(TsFont f) { font = f; }
	public TsFont getFont() { return font; }
	public void setText(String t) { text = t; }
	public String getText() { return text; }
	public void setOutlineSize(int s) { borderSize = s; }
	public int getOutlineSize() { return borderSize; }
	public void setEnabled(boolean isEnabled) { this.isEnabled = isEnabled; }
	public boolean isEnabled() { return isEnabled; }
	
	protected boolean isDrawingClick() { 
		return clickTime + 100 > System.currentTimeMillis(); 
	}

	@Override
	public void draw(IGraphics g) {
		if (!isVisible())
			return;
		
		super.draw(g);
		g.setFont(font);
		calcDimensions(g);
		
		int borderColor = getBackground().darker();
		if (isDrawingClick() && isEnabled)
			borderColor = TsColor.rgb(0, 190, 190);
		g.setColor(borderColor);
		g.fillRoundedRect(getX(), getY(), getWidth(), getHeight(), 6, 6);

		if(!isDrawingClick() && isEnabled)
			g.setColor(getBackground());
		else
			g.setColor(getBackground().darker());
		
		float inX = getX() + borderSize, inY = getY() + borderSize;
		float inW = getWidth() - 2 * borderSize, inH = getHeight() - 2 * borderSize;
		g.fillRoundedRect(inX, inY, inW, inH, 12, 12);
		
		g.setColor(getForeground());
		float textWidth = g.getTextWidth(text);
		float textX = getX() + getWidth() / 2 - textWidth / 2;
		float textY = getY() + getHeight() / 2 + g.getTextHeight() / 4;
		g.drawText(textX, textY, text);
	}
	
	@Override
	public void recieveMouseEvent(TsMouseEvent e, float x, float y) {
		super.recieveMouseEvent(e, x, y);
		if (e.action == MouseAction.PRESS || e.action == MouseAction.DRAG) {
			clickTime = System.currentTimeMillis();
		}
	}
	
	protected void calcDimensions(IGraphics g) {
		float w = 20.0f + getOutlineSize(), h = 10.0f + getOutlineSize();
		if (text != null && !text.equals("")) {
			w += g.getTextWidth(text);
			h += g.getTextHeight();
		}
		
		setSize(w, h);
	}

}
