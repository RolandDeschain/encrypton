package it.oasi.crypter.engine.sequence;

import it.oasi.crypter.engine.sequence.CharSequence;
import it.oasi.crypter.engine.sequence.NumberSequence;
import it.oasi.crypter.engine.sequence.Sequence;
import it.oasi.crypter.engine.sequence.SequenceGroup;

public class PatenteNumberSequenceGroup extends SequenceGroup {
	
	private Sequence engine;
	private static final int REFERENCED_VALUE_LENGTH = 10;

	@Override
	public int getReferencedValueLength() {
		return REFERENCED_VALUE_LENGTH;
	}

	public PatenteNumberSequenceGroup() {
		super();
		Sequence a = new CharSequence();
		Sequence b = new CharSequence( a );
		Sequence c = new NumberSequence( b );
		Sequence d = new NumberSequence( c );
		Sequence e = new NumberSequence( d );
		Sequence f = new NumberSequence( e );
		Sequence g = new NumberSequence( f );
		Sequence h = new NumberSequence( g );
		Sequence i = new NumberSequence( h );
		engine = new NumberSequence( i );
	}

	@Override
	public Sequence getEngine() {
		return engine;
	}

}
