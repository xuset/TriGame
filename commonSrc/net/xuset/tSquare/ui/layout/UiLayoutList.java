package net.xuset.tSquare.ui.layout;

import java.util.ArrayList;
import java.util.List;

import net.xuset.tSquare.math.point.IPointR;
import net.xuset.tSquare.ui.Alignment;
import net.xuset.tSquare.ui.Axis;
import net.xuset.tSquare.ui.UiComponent;
import net.xuset.tSquare.util.Observer;
import net.xuset.tSquare.util.Observer.Change;

public abstract class UiLayoutList implements UiLayout {
	
	private final List<UiComponent> components;
	private final Observer<IPointR> dimensionObserver = new Observer<IPointR>();
	private Axis axis = Axis.X_AXIS;
	private Alignment alignmentX = Alignment.FRONT, alignmentY = Alignment.FRONT;
	
	protected UiLayoutList(List<UiComponent> components) {
		this.components = components;
	}

	protected UiLayoutList() {
		this(new ArrayList<UiComponent>(2));
	}

	@Override
	public void add(UiComponent c) {
		components.add(c);
		organize();
	}

	@Override
	public void remove(UiComponent c) {
		components.remove(c);
		organize();
	}

	@Override
	public int getComponentCount() {
		return components.size();
	}

	@Override
	public UiComponent getComponent(int i) {
		return components.get(i);
	}

	@Override
	public void clearComponents() {
		components.clear();
		organize();
	}

	@Override
	public void setOrientation(Axis a) {
		axis = a;
		organize();
	}

	@Override
	public Axis getOrientation() {
		return axis;
	}

	@Override
	public void setAlignment(Axis axis, Alignment a) {
		switch(axis) {
		case X_AXIS:
			alignmentX = a;
			break;
		default:
			alignmentY = a;
			break;
		}
		organize();
	}

	@Override
	public Alignment getAlignment(Axis axis) {
		switch(axis) {
		case X_AXIS:
			return alignmentX;
		default:
			return alignmentY;
		}
	}
	
	@Override
	public void watchDimensions(Change<IPointR> oc) {
		dimensionObserver.watch(oc);
	}

	@Override
	public void unwatchDimensions(Change<IPointR> oc) {
		dimensionObserver.unwatch(oc);
	}
	
	protected void setDimension(IPointR d) {
		dimensionObserver.notifyWatchers(d);
	}
	
	protected Axis getOppositeOrientation() {
		switch(getOrientation()) {
		case X_AXIS:
			return Axis.Y_AXIS;
		case Y_AXIS:
			return Axis.X_AXIS;
		}
		return Axis.X_AXIS;
	}

}
