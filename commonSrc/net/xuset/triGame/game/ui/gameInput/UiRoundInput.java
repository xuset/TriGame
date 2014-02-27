package net.xuset.triGame.game.ui.gameInput;

import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.TsColor;
import net.xuset.tSquare.imaging.TsFont;
import net.xuset.tSquare.imaging.TsTypeFace;
import net.xuset.tSquare.system.input.mouse.MouseAction;
import net.xuset.tSquare.system.input.mouse.TsMouseEvent;
import net.xuset.tSquare.ui.UiLabel;

public class UiRoundInput extends UiLabel implements IRoundInput{
	private static final long maxClickDelay = 50;
	
	private long timeMouseClick = 0l;
	private boolean newRoundRequestable = false;
	private boolean shouldRespondToMouse = false;
	
	public UiRoundInput() {
		super("\'Enter\' to start new round");
		setFont(new TsFont("Arial", 30, TsTypeFace.BOLD));
		setDrawShadow(true);
		setForeground(TsColor.lightGray);
	}
	
	public void respondToMouse(boolean shouldRespondToMouse) {
		this.shouldRespondToMouse = shouldRespondToMouse;
	}

	@Override
	public void draw(IGraphics g) {
		if (!isVisible() || !newRoundRequestable)
			return;
		
		getBorder().setVisibility(wasLabelClicked());
		super.draw(g);
	}

	@Override
	protected void recieveMouseEvent(TsMouseEvent e, float x, float y) {
		super.recieveMouseEvent(e, x, y);
		
		if (shouldRespondToMouse && e.action == MouseAction.PRESS)
			timeMouseClick = System.currentTimeMillis();
			
	}

	@Override
	public boolean newRoundRequested() {
		return wasLabelClicked();
	}

	@Override
	public void setNewRoundRequestable(boolean requestable) {
		newRoundRequestable = requestable;
	}
	
	private boolean wasLabelClicked() {
		return timeMouseClick + maxClickDelay > System.currentTimeMillis();
	}
	
	
}
