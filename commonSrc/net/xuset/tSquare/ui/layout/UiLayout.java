package net.xuset.tSquare.ui.layout;

import net.xuset.tSquare.math.point.IPointR;
import net.xuset.tSquare.ui.Alignment;
import net.xuset.tSquare.ui.Axis;
import net.xuset.tSquare.ui.UiComponent;
import net.xuset.tSquare.util.Observer;

public interface UiLayout {
	void watchDimensions(Observer.Change<IPointR> oc);
	void unwatchDimensions(Observer.Change<IPointR> oc);
	void add(UiComponent c);
	void remove(UiComponent c);
	int getComponentCount();
	UiComponent getComponent(int i);
	void clearComponents();
	void setOrientation(Axis a);
	Axis getOrientation();
	void setAlignment(Axis axis, Alignment a);
	Alignment getAlignment(Axis axis);
	float getWidth();
	float getHeight();
	void organize();
}
