package tSquare.math;

import java.util.Random;

public class IdGenerator {
	private static IdGenerator instance = new IdGenerator();
	public static IdGenerator getInstance() { return instance; }
	
	Random random = new Random();
	public long getId() {
		return random.nextLong();
	}
}
