package it.oasi.crypter.engine.sequence;

import org.apache.commons.lang3.StringUtils;

public class CharSequencesGroup extends SequenceGroup {
	
	private Sequence engine;
	private int generatedValuesLength;
	
	@Override
	public int getReferencedValueLength() {
		return this.generatedValuesLength;
	}

	public CharSequencesGroup(int nDelegates, int generatedValuesLength) {
		this.generatedValuesLength = generatedValuesLength;
		engine = new CharSequence( EmptySequence.getInstance() );
		for( int i = 0; i < nDelegates-1; i++ ) {
			engine = new CharSequence( engine );
		}
	}
	
	@Override
	public String next() {
		String next = super.next();
		return StringUtils.rightPad(
				next, 
				this.generatedValuesLength, 
				' ');
	}
	
	@Override
	public Sequence getEngine() {
		return engine;
	}

}
