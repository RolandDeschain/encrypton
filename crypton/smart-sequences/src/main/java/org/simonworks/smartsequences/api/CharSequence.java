package it.oasi.crypter.engine.sequence;

public class CharSequence extends ProxySequence {
	
	private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	public CharSequence() {
		super();
	}
	
	public CharSequence(Sequence delegate) {
		super(delegate);
	}
	
	@Override
	public String getCharacterSet() {
		return CHARS;
	}
	
}
