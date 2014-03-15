package net.xuset.triGame.game.ui.arsenal;

import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.ui.Alignment;
import net.xuset.tSquare.ui.Axis;
import net.xuset.tSquare.ui.UiComponent;
import net.xuset.tSquare.ui.UiForm;
import net.xuset.tSquare.ui.UiLabel;
import net.xuset.tSquare.ui.layout.UiQueueLayout;

public class ItemForm extends ArsenalSubForm implements ItemMetaSetter{
	private final UiLabel lblName = new UiLabel("");
	private final UiLabel lblDescription = new UiLabel("");
	private final UiForm frmContainer = new UiForm();
	
	private InfoUpdate newUpdate = null;
	
	public ItemForm(String formName) {
		super(formName);
		UiQueueLayout layout = new UiQueueLayout(0, 0, this);
		setLayout(layout);
		frmContainer.setLayout(new UiQueueLayout(0, 0, frmContainer));
		layout.setOrientation(Axis.Y_AXIS);
		layout.setAlignment(Axis.X_AXIS, Alignment.CENTER);
		layout.add(lblName);
		layout.add(lblDescription);
		layout.add(frmContainer);
		
	}
	
	@Override
	public void onForcedFocusLost() {
		lblName.setText("");
		lblDescription.setText("");
	}

	@Override
	public void setDisplayInfo(String name, String description) {
		if (!lblName.getText().equals(name) ||
				lblDescription.getText().equals(description)) {
			
			newUpdate = new InfoUpdate(name, description);
		}
	}
	
	@Override
	public void draw(IGraphics g) {
		if (newUpdate != null) {
			lblName.setText(newUpdate.name);
			lblDescription.setText(newUpdate.description);
			newUpdate = null;
		}
		
		super.draw(g);
	}
	
	public void addItem(UiComponent item) {
		frmContainer.getLayout().add(item);
	}
	
	private static class InfoUpdate {
		private final String name, description;
		
		public InfoUpdate(String name, String description) {
			this.name = name;
			this.description = description;
		}
	}

}
