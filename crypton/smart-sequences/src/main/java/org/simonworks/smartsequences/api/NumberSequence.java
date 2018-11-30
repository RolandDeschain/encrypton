package it.oasi.crypter.engine.sequence;

public class NumberSequence extends ProxySequence {
	
	private static final String NUMBERS = "0123456789";
	
	public NumberSequence() {
		super();
	}
	
	public NumberSequence(Sequence delegate) {
		super(delegate);
	}
	
	@Override
	public String getCharacterSet() {
		return NUMBERS;
	}
	
}
