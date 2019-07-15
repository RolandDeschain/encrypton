package org.simonworks.smartsequences.commons;

import org.simonworks.smartsequences.api.CharsSequence;
import org.simonworks.smartsequences.api.NumbersSequence;
import org.simonworks.smartsequences.api.Sequence;
import org.simonworks.smartsequences.api.SequenceGroup;

public class ItalianIdentityCardNumberSequenceGroup implements SequenceGroup {
	
	private Sequence engine;
	private static final int REFERENCED_VALUE_LENGTH = 9;

	@Override
	public int getReferencedValueLength() {
		return REFERENCED_VALUE_LENGTH;
	}

	public ItalianIdentityCardNumberSequenceGroup() {
		super();
		Sequence a = new CharsSequence();
		Sequence b = new CharsSequence( a );
		Sequence c = new NumbersSequence( b );
		Sequence d = new NumbersSequence( c );
		Sequence e = new NumbersSequence( d );
		Sequence f = new NumbersSequence( e );
		Sequence g = new NumbersSequence( f );
		Sequence h = new NumbersSequence( g );
		engine = new NumbersSequence( h );
	}
	
	@Override
	public Sequence getEngine() {
		return engine;
	}
	
}
