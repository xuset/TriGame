package net.xuset.triGame.intro.join;

import net.xuset.tSquare.ui.Axis;
import net.xuset.tSquare.ui.UiForm;
import net.xuset.tSquare.ui.UiLabel;
import net.xuset.triGame.game.GameInfo;
import net.xuset.triGame.intro.IntroForm;

public class IntroJoin implements IntroForm {
	
	private final UiForm mainForm = new UiForm();
	private final UiLabel lblSearching = new UiLabel("Searching for games...");
	private final UiForm frmGameList = new UiForm();
	
	public IntroJoin() {
		mainForm.getLayout().setOrientation(Axis.Y_AXIS);
		mainForm.getLayout().add(lblSearching);
		mainForm.getLayout().add(frmGameList);
	}

	@Override
	public GameInfo getCreatedGameInfo() {
		return null;
	}

	@Override
	public UiForm getForm() {
		return mainForm;
	}

}
