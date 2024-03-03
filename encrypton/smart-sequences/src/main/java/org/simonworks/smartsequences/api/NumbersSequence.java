package org.simonworks.smartsequences.api;

public class NumbersSequence extends ProxySequence {
	
	private static final String NUMBERS = "0123456789";
	
	public NumbersSequence() {
		super();
	}
	
	public NumbersSequence(Sequence delegate) {
		super(delegate);
	}
	
	@Override
	public String getCharacterSet() {
		return NUMBERS;
	}
	
}
