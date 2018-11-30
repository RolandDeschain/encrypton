package org.simonworks.smartsequences.sequence;

import java.math.BigInteger;

import org.apache.commons.lang3.StringUtils;

class EmptySequence implements Sequence {
	
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
		throw new SequenceException( "Sequence exausted! All valued assigned" );
	}
	
	@Override
	public void decrement(long amount) {
		throw new SequenceException( "Sequence exausted! All valued assigned" );
	}

	public void increment() {
		throw new SequenceException( "Sequence exausted! All valued assigned" );
	}
	
	public void increment(long amount) {
		throw new SequenceException( "Sequence exausted! All valued assigned" );
	}
	
	@Override
	public void synchWithValue(String value) {
//		throw new RuntimeException( "Sequence exausted! All valued assigned" );
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
	
}
