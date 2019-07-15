package org.simonworks.smartsequences.api;

import java.math.BigInteger;

import org.apache.commons.lang3.StringUtils;

class EmptySequence implements Sequence {
	
	private static final String SEQUENCE_END = "Sequence exausted! All valued assigned";
	private static final EmptySequence myself = new EmptySequence();
	
	private EmptySequence() {}
	
	public static final Sequence getInstance() {
		return myself;
	}

	@Override
	public StringBuilder getFirst() {
		return get();
	}
	
	public StringBuilder get() {
		return new StringBuilder();
	}
	
	@Override
	public StringBuilder getLast() {
		return get();
	}
	
	@Override
	public void decrement() {
		throw new SequenceException( SEQUENCE_END );
	}
	
	@Override
	public void decrement(long amount) {
		throw new SequenceException( SEQUENCE_END );
	}

	public void increment() {
		throw new SequenceException( SEQUENCE_END );
	}
	
	public void increment(long amount) {
		throw new SequenceException( SEQUENCE_END );
	}
	
	@Override
	public void synchWithValue(String value) {
		/**
		 * Nothing to do here :-)
		 */
	}
	
	@Override
	public int countDelegates() {
		return 0;
	}
	
	@Override
	public BigInteger countPermutations() {
		return BigInteger.ONE;
	}

	@Override
	public String getCharacterSet() {
		return StringUtils.EMPTY;
	}
	
	@Override
	public void reset() {
		/**
		 * Do nothing
		 */
	}
	
	@Override
	public boolean shouldReset() {
		return true;
	}
	
}
