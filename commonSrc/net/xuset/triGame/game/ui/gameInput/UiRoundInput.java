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
	private static final String touchRequestText = "Tap to start round";
	private static final String keyboardRequestText = "\'Enter\' to start round";
	private static final String waitText = "Waiting for other players";
	
	private long timeMouseClick = 0l;
	private boolean newRoundRequestable = false;
	private boolean shouldRespondToMouse = false;
	private String roundRequestText = keyboardRequestText;
	
	public UiRoundInput() {
		super();
		setFont(new TsFont("Arial", 35, TsTypeFace.BOLD));
		setDrawShadow(true);
		setForeground(TsColor.lightGray);
	}
	
	public void setEnableTouchMode(boolean touchOn) {
		shouldRespondToMouse = touchOn;
		roundRequestText = touchOn ? touchRequestText : keyboardRequestText;
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
		
		if (shouldRespondToMouse && e.action == MouseAction.RELEASE)
			timeMouseClick = System.currentTimeMillis();
			
	}

	@Override
	public boolean newRoundRequested() {
		return wasLabelClicked();
	}

	@Override
	public void setNewRoundRequestable(boolean requestable) {
		newRoundRequestable = requestable;
		if (requestable)
			setText(roundRequestText);
	}
	
	public void displayWaitText() {
		setText(waitText);
	}
	
	private boolean wasLabelClicked() {
		return timeMouseClick + maxClickDelay > System.currentTimeMillis();
	}
	
	
}
