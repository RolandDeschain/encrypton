package org.simonworks.crypter.example.sequences;

import org.simonworks.smartsequences.api.CharSequencesGroup;
import org.simonworks.smartsequences.api.NumberSequencesGroup;
import org.simonworks.smartsequences.api.SequenceGroup;
import org.simonworks.smartsequences.commons.DatesSequenceGroup;
import org.simonworks.smartsequences.commons.ItalianFiscaleCodeSequenceGroup;

public enum SingletonSequenceFactory {
	
	FISCAL_CODE_SEQUENCE 	( new ItalianFiscaleCodeSequenceGroup() ),
	VAT_SEQUENCE			( new NumberSequencesGroup( 11 ) ),
	PROVINCE_SEQUENCE		( new CharSequencesGroup( 2, 2 ) ),
	VAL_DATE_SEQUENCE		( new DatesSequenceGroup() ),
	INSERT_DATE_SEQUENCE	( new DatesSequenceGroup() ), 
	AGE_SEQUENCE			( new NumberSequencesGroup( 3 ) );
	
	private SequenceGroup sequence;
	
	private SingletonSequenceFactory(SequenceGroup sequence) {
		this.sequence = sequence;
	}
	
	public SequenceGroup get() {
		return sequence;
	}
	
	public String next() {
		return sequence.next();
	}
	
	public void synchWithValue(String newValue) {
		sequence.getEngine().synchWithValue( newValue.trim() );
	}
}
