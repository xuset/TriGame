package net.xuset.triGame.game.input;

import net.xuset.tSquare.game.GameDrawable;
import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.system.input.mouse.IMouseListener;
import net.xuset.tSquare.system.input.mouse.MouseAction;
import net.xuset.tSquare.system.input.mouse.TsMouseEvent;
import net.xuset.tSquare.util.Observer;
import net.xuset.triGame.game.Draw;
import net.xuset.triGame.game.ui.gameInput.IRoundInput;

public class TouchRoundInput implements IRoundInput, GameDrawable {
	private boolean drawRoundRequest = false;
	private boolean roundRequested = false;
	
	public TouchRoundInput(IMouseListener mouse) {
		mouse.watch(new MouseObserver());
	}

	@Override
	public void draw(IGraphics g) {
		if (drawRoundRequest)
			Draw.drawTapToStart(g);
	}

	@Override
	public boolean newRoundRequested() {
		return roundRequested;
	}

	@Override
	public void setNewRoundRequestable(boolean requestable) {
		drawRoundRequest = requestable;
		if (!requestable)
			roundRequested = false;
	}
	
	private class MouseObserver implements Observer.Change<TsMouseEvent> {

		@Override
		public void observeChange(TsMouseEvent t) {
			if (drawRoundRequest && t.action == MouseAction.PRESS && t.y < 50)
				roundRequested = true;
				
		}
		
	}

}
