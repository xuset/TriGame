package net.xuset.tSquare.ui.layout;

import net.xuset.tSquare.ui.UiForm;

public class UiMainForm extends UiForm {
	float w = 0.0f, h = 0.0f;
	
	@Override
	public void setSize(float w, float h) {
		this.w = w;
		this.h = h;
	}

	@Override
	public float getWidth() {
		return w;
	}
	
	@Override
	public float getHeight() {
		return h;
	}

	
}
