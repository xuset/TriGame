package net.xuset.tSquare.ui.layout;

import net.xuset.tSquare.ui.Axis;
import net.xuset.tSquare.ui.UiComponent;

public class DimensionFinder {
	public static float sum(UiLayout layout, Axis axis) {
		float sum = 0.0f;
		for (int i = 0; i < layout.getComponentCount(); i++){
			UiComponent c = layout.getComponent(i);
			sum += getDimension(c, axis);
		}
		return sum;
	}
	
	public static float min(UiLayout layout, Axis axis) {
		if (layout.getComponentCount() == 0)
			return 0.0f;
		
		float min = getDimension(layout.getComponent(0), axis);

		for (int i = 0; i < layout.getComponentCount(); i++){
			UiComponent c = layout.getComponent(i);
			float dim = getDimension(c, axis);
			if (dim < min)
				min = dim;
		}
		
		return min;
	}
	
	public static float max(UiLayout layout, Axis axis) {
		if (layout.getComponentCount() == 0)
			return 0.0f;
		
		float max = getDimension(layout.getComponent(0), axis);

		for (int i = 0; i < layout.getComponentCount(); i++){
			UiComponent c = layout.getComponent(i);
			float dim = getDimension(c, axis);
			if (dim > max)
				max = dim;
		}
		
		return max;
	}
	
	private static float getDimension(UiComponent c, Axis a) {
		switch (a) {
		case X_AXIS:
			return c.getWidth();
		case Y_AXIS:
			return c.getHeight();
		}
		return 0.0f;
	}
}
