package it.oasi.crypter.engine.sequence;

public class NumberSequencesGroup extends SequenceGroup {
	
	private Sequence engine;
	private int nDelegates;

	@Override
	public int getReferencedValueLength() {
		return nDelegates;
	}

	public NumberSequencesGroup(int nDelegates) {
		this.nDelegates = nDelegates;
		engine = new NumberSequence( EmptySequence.getInstance() );
		for( int i = 0; i < nDelegates-1; i++ ) {
			engine = new NumberSequence( engine );
		}
	}

	@Override
	public Sequence getEngine() {
		return engine;
	}
}
