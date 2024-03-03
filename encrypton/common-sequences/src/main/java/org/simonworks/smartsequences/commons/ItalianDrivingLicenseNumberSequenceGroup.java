package org.simonworks.smartsequences.commons;

import org.simonworks.smartsequences.api.CharsSequence;
import org.simonworks.smartsequences.api.NumbersSequence;
import org.simonworks.smartsequences.api.Sequence;
import org.simonworks.smartsequences.api.SequenceGroup;

public class ItalianDrivingLicenseNumberSequenceGroup implements SequenceGroup {
	
	private final Sequence engine;
	private static final int REFERENCED_VALUE_LENGTH = 10;

	@Override
	public int getReferencedValueLength() {
		return REFERENCED_VALUE_LENGTH;
	}

	public ItalianDrivingLicenseNumberSequenceGroup() {
		super();
		Sequence a = new CharsSequence();
		Sequence b = new CharsSequence( a );
		Sequence c = new NumbersSequence( b );
		Sequence d = new NumbersSequence( c );
		Sequence e = new NumbersSequence( d );
		Sequence f = new NumbersSequence( e );
		Sequence g = new NumbersSequence( f );
		Sequence h = new NumbersSequence( g );
		Sequence i = new NumbersSequence( h );
		engine = new NumbersSequence( i );
	}

	@Override
	public Sequence getEngine() {
		return engine;
	}

}
