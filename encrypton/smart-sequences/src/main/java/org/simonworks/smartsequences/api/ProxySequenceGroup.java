package org.simonworks.smartsequences.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Sequences group that proxies other sequences
 * 
 * @author SimonWorks
 *
 */
public class ProxySequenceGroup implements SequenceGroup {
	
	private List<SequenceGroup> delegates;
	
	public ProxySequenceGroup(SequenceGroup ... delegates) {
		super();
		if( delegates != null ) {
			this.delegates = Arrays.asList(delegates);
		}
		
	}

	@Override
	public int getReferencedValueLength() {
		int result = 0;
		if( delegates != null ) {
			for (SequenceGroup sequenceGroup : delegates) {
				result += sequenceGroup.getReferencedValueLength();
			}
		}
		return result;
	}

	@Override
	public Sequence getEngine() {
		return null;
	}
	
	@Override
	public String next() {
		StringBuilder result = new StringBuilder();
		if( delegates != null ) {
			for (SequenceGroup sequenceGroup : delegates) {
				result.append( sequenceGroup.next() );
			}
		}
		return result.toString();
	}
	
	
}
