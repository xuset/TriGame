package net.xuset.tSquare.system.input.mouse;

public class TsMouseEvent {
	public final MouseAction action;
	public final MouseButton button;
	public final int x;
	public final int y;
	
	public TsMouseEvent(MouseAction action, MouseButton button, int x, int y) {
		this.action = action;
		this.button = button;
		this.x = x;
		this.y = y;
	}
}
