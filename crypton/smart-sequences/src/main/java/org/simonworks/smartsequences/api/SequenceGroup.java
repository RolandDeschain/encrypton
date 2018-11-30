package it.oasi.crypter.engine.sequence;

public abstract class SequenceGroup {
	
	
	
	public abstract int getReferencedValueLength();
	
	public abstract Sequence getEngine();
	
	public String next() {
			StringBuilder result = this.getEngine().get();
			this.getEngine().increment();
			return result.toString();
	}
	
	
	

	
	

}
