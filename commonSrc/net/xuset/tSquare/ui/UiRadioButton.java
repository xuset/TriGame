package net.xuset.tSquare.ui;

import net.xuset.tSquare.imaging.IFont;
import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.TsColor;
import net.xuset.tSquare.imaging.TsFont;
import net.xuset.tSquare.imaging.TsTypeFace;

public class UiRadioButton extends UiComponent {
	private boolean isSelected = false;
	private String text;
	private IFont font = new TsFont("Arial", 12, TsTypeFace.PLAIN);
	private float buttonSize = 15.0f;
	private TsColor buttonBackgroundColor = TsColor.gray;
	private TsColor buttonUnSelectedColor = TsColor.lightGray;
	private TsColor buttonSelectedColor = new TsColor(0, 220, 220);
	
	public UiRadioButton() {
		this("");
	}

	public UiRadioButton(String text) {
		super(0, 0, 0, 0);
		this.text = text;
	}
	
	public void setButtonSize(float buttonSize) { this.buttonSize = buttonSize; }
	public float getButtonSize() { return buttonSize; }
	
	public IFont getFont() { return font; }
	public void setFont(IFont font) { this.font = font; }
	
	public void setText(String text) { this.text = text; }
	public String getText() { return text; }
	
	public boolean isSelected() { return isSelected; }
	void setSelected(boolean isSelected) { this.isSelected = isSelected; }
	
	@Override
	public void draw(IGraphics g) {
		calcSize(g);
		super.draw(g);
		if (!isVisible())
			return;
		
		drawButton(g);
		drawText(g);
	}
	
	private void drawText(IGraphics g) {
		g.setFont(font);
		g.setColor(getForeground());
		float middle = getY() + getHeight() / 2 + g.getTextHeight() / 4;
		float left = buttonSize * 2;
		g.drawText(left, middle, text);
	}
	
	private void drawButton(IGraphics g) {
		float gutter = 3.0f;
		float middle = getY() + getHeight() / 2 - buttonSize / 2;
		g.setColor(buttonBackgroundColor);
		g.fillOval(getX(), middle, buttonSize, buttonSize);
		g.setColor(isSelected ? buttonSelectedColor : buttonUnSelectedColor);
		g.fillOval(getX() + gutter, middle + gutter,
				buttonSize - gutter * 2, buttonSize - gutter * 2);
	}
	
	private void calcSize(IGraphics g) {
		float w = buttonSize * 2 + g.getTextWidth(text);
		float h = Math.max(buttonSize, g.getTextHeight());
		setSize(w, h);
	}

}
