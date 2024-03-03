package org.simonworks.smartsequences.api;

import org.apache.commons.lang3.StringUtils;

public class CharSequencesGroup implements SequenceGroup {
	
	private Sequence engine;
	private final int generatedValuesLength;
	
	@Override
	public int getReferencedValueLength() {
		return this.generatedValuesLength;
	}

	public CharSequencesGroup(int nDelegates, int generatedValuesLength) {
		this.generatedValuesLength = generatedValuesLength;
		engine = new CharsSequence( EmptySequence.getInstance() );
		for( int i = 0; i < nDelegates-1; i++ ) {
			engine = new CharsSequence( engine );
		}
	}
	
	@Override
	public String next() {
		String next = SequenceGroup.super.next();
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
