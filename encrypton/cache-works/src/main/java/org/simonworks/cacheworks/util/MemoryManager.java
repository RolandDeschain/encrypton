package org.simonworks.cacheworks.util;

public class MemoryManager {
	
	private static final int MEGA_BYTES = 1024 * 1024;
	
	private MemoryManager() {
		
	}
	
	public static double usedMemory() {
		// Getting the runtime reference from system
		Runtime runtime = Runtime.getRuntime();
		long used = (runtime.totalMemory() - runtime.freeMemory()) / MEGA_BYTES;
		return (used / ((double) runtime.maxMemory() / MEGA_BYTES)) * 100;
	}

}
