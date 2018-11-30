package it.oasi.crypter.engine.sequence;

import it.oasi.crypter.engine.sequence.CharSequence;
import it.oasi.crypter.engine.sequence.NumberSequence;
import it.oasi.crypter.engine.sequence.Sequence;
import it.oasi.crypter.engine.sequence.SequenceGroup;

public class CartaIdentitaNumberSequenceGroup extends SequenceGroup {
	
	private Sequence engine;
	private static final int REFERENCED_VALUE_LENGTH = 9;

	@Override
	public int getReferencedValueLength() {
		return REFERENCED_VALUE_LENGTH;
	}

	public CartaIdentitaNumberSequenceGroup() {
		super();
		Sequence a = new CharSequence();
		Sequence b = new CharSequence( a );
		Sequence c = new NumberSequence( b );
		Sequence d = new NumberSequence( c );
		Sequence e = new NumberSequence( d );
		Sequence f = new NumberSequence( e );
		Sequence g = new NumberSequence( f );
		Sequence h = new NumberSequence( g );
		engine = new NumberSequence( h );
	}
	
	@Override
	public Sequence getEngine() {
		return engine;
	}
	
}
