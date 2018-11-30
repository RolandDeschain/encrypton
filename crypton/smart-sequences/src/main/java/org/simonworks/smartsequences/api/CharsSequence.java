package org.simonworks.smartsequences.api;

public class CharsSequence extends ProxySequence {
	
	private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	public CharsSequence() {
		super();
	}
	
	public CharsSequence(Sequence delegate) {
		super(delegate);
	}
	
	@Override
	public String getCharacterSet() {
		return CHARS;
	}
	
}
