package it.oasi.crypter.engine.sequence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CodiceFiscaleSequenceGroup extends SequenceGroup {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CodiceFiscaleSequenceGroup.class);
	
	private Sequence engine;
	private static final int REFERENCED_VALUE_LENGTH = 16;

	@Override
	public int getReferencedValueLength() {
		return REFERENCED_VALUE_LENGTH;
	}

	public CodiceFiscaleSequenceGroup() {		
		super();
		Sequence a = new CharSequence();
		Sequence b = new CharSequence(		a	);
		Sequence c = new CharSequence(		b	);
		Sequence d = new CharSequence(		c	);
		Sequence e = new CharSequence(		d	);
		Sequence f = new CharSequence(		e	);
		Sequence g = new NumberSequence(	f	);
		Sequence h = new NumberSequence(	g	);
		Sequence i = new CharSequence(		h	);
		Sequence j = new NumberSequence(	i	);
		Sequence k = new NumberSequence(	j	);
		Sequence l = new CharSequence(		k	);
		Sequence m = new NumberSequence(	l	);
		Sequence n = new NumberSequence(	m	);
		Sequence o = new NumberSequence(	n	);
		engine = new CharSequence(			o	);
	}
	
	@Override
	public Sequence getEngine() {
		return engine;
	}
	
	public static void main(String[] args) {
		String last =  "BTTFNC77E03H223R";
		
		SequenceGroup ps = new CodiceFiscaleSequenceGroup();
		ps.getEngine().synchWithValue(last);
		LOGGER.info( ps.next() );
		LOGGER.info( ps.next() );
	}

}
