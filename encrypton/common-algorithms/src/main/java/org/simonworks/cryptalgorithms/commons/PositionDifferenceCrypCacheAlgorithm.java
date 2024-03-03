package org.simonworks.cryptalgorithms.commons;

public class PositionDifferenceCrypCacheAlgorithm extends AbstractCrypCacheAlgorithm {

	@Override
	public void init() {
		// Do nothing here :-)
	}
	
	@Override
	public String recover(String container, String source) {
		// Source is the String to encrypt
		return null;
	}
	
	@Override
	public String getName() {
		return "s-diff";
	}

	@Override
	public boolean contains(String container, String source) {
		return false;
	}

}
