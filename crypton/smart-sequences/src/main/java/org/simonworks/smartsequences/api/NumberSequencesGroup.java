package org.simonworks.smartsequences.api;

public class NumberSequencesGroup implements SequenceGroup {
	
	private Sequence engine;
	private int nDelegates;

	@Override
	public int getReferencedValueLength() {
		return nDelegates;
	}

	public NumberSequencesGroup(int nDelegates) {
		this.nDelegates = nDelegates;
		engine = new NumbersSequence( EmptySequence.getInstance() );
		for( int i = 0; i < nDelegates-1; i++ ) {
			engine = new NumbersSequence( engine );
		}
	}

	@Override
	public Sequence getEngine() {
		return engine;
	}
}
