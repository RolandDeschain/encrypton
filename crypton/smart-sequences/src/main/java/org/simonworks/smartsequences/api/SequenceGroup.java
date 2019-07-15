package org.simonworks.smartsequences.api;

public interface SequenceGroup {
	
	int getReferencedValueLength();
	
	Sequence getEngine();
	
	default String next() {
			StringBuilder result = this.getEngine().get();
			this.getEngine().increment();
			return result.toString();
	}
	
	default void reset() {
		this.getEngine().reset();
	}
	
}
