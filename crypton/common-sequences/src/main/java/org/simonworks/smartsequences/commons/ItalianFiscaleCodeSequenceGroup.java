package org.simonworks.smartsequences.commons;

import org.simonworks.smartsequences.api.CharsSequence;
import org.simonworks.smartsequences.api.NumbersSequence;
import org.simonworks.smartsequences.api.Sequence;
import org.simonworks.smartsequences.api.SequenceGroup;

public final class ItalianFiscaleCodeSequenceGroup implements SequenceGroup {
	
	/**
	 * private static final Logger LOGGER = LoggerFactory.getLogger(CodiceFiscaleSequenceGroup.class);
	 */
	
	private Sequence engine;
	private static final int REFERENCED_VALUE_LENGTH = 16;

	@Override
	public int getReferencedValueLength() {
		return REFERENCED_VALUE_LENGTH;
	}

	public ItalianFiscaleCodeSequenceGroup() {		
		super();
		Sequence a = new CharsSequence();
		Sequence b = new CharsSequence(		a	);
		Sequence c = new CharsSequence(		b	);
		Sequence d = new CharsSequence(		c	);
		Sequence e = new CharsSequence(		d	);
		Sequence f = new CharsSequence(		e	);
		Sequence g = new NumbersSequence(	f	);
		Sequence h = new NumbersSequence(	g	);
		Sequence i = new CharsSequence(		h	);
		Sequence j = new NumbersSequence(	i	);
		Sequence k = new NumbersSequence(	j	);
		Sequence l = new CharsSequence(		k	);
		Sequence m = new NumbersSequence(	l	);
		Sequence n = new NumbersSequence(	m	);
		Sequence o = new NumbersSequence(	n	);
		engine = new CharsSequence(			o	);
	}
	
	@Override
	public Sequence getEngine() {
		return engine;
	}
	
	public static void main(String[] args) {
		ItalianFiscaleCodeSequenceGroup seq = new ItalianFiscaleCodeSequenceGroup();
		System.out.println( seq.next() );
	}

}
