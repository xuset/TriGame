package net.xuset.tSquare.math;

import java.util.Random;

public class IdGenerator {
	private static IdGenerator instance = new IdGenerator();
	public static long getNext() { return instance.getId(); }
	
	Random random = new Random();
	public long getId() {
		return random.nextLong();
	}
}
