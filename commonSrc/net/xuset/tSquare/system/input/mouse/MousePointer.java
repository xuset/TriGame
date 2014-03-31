package net.xuset.tSquare.system.input.mouse;

public class MousePointer {
	private final int id;
	
	boolean isPressed = true;
	float x, y;
	
	public float getX() { return x; }
	
	public float getY() { return y; }
	
	public boolean isPressed() { return isPressed; }
	
	public int getId() { return id; }
	
	public MousePointer(int id, float x, float y) {
		this.id = id;
		this.x = x;
		this.y = y;
	}
}
