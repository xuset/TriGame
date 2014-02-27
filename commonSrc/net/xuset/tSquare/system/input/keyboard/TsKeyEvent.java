package net.xuset.tSquare.system.input.keyboard;

public class TsKeyEvent {
	public final KeyAction action;
	public final char key;
	public final int keyId;
	
	public TsKeyEvent(KeyAction action, char key, int keyId) {
		this.action = action;
		this.key = key;
		this.keyId = keyId;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TsKeyEvent) {
			TsKeyEvent e = (TsKeyEvent) obj;
			return e.action == action && e.keyId == keyId && e.key == key;
		}
		return super.equals(obj);
	}
}
