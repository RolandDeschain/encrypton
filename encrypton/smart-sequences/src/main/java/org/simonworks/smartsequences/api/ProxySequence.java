package org.simonworks.smartsequences.api;

import java.math.BigInteger;

import org.apache.commons.lang3.StringUtils;

public abstract class ProxySequence implements Sequence {
	
	public static final int MIN_VALUE = 0;
	private final Sequence delegate;
	private int currentIndex = 0;
	
	private final Incrementer incrementer;
	
	private static final Object mutex = new Object();
	
	public ProxySequence() {
		this( EmptySequence.getInstance(), true );
	}
	
	public ProxySequence(Sequence delegate) {
		this(delegate, true);
	}
	
	public ProxySequence(boolean synchOps) {
		this( EmptySequence.getInstance(), synchOps );
	}
	
	public ProxySequence(Sequence delegate, boolean synchOps) {
		this.delegate = delegate;
		
		if(synchOps) {
			incrementer = new SynchIncrementer();
		} else {
			incrementer = new AsynchIncrementer();
		}
	}
	
	@Override
	public int countDelegates() {
		return delegate.countDelegates() + 1;
	}
	
	@Override
	public BigInteger countPermutations() {
		return delegate.countPermutations().multiply( BigInteger.valueOf( getCharacterSet().length() ) );
	}
	
	@Override
	public StringBuilder getFirst() {
		StringBuilder result = delegate.getFirst();
		String myContribution = String.valueOf( getCharacterSet().charAt( 0 ));
		return result.append( myContribution) ;
	}
	
	@Override
	public StringBuilder get() {
			StringBuilder result = delegate.get();
			return result.append( actual() );
	}
	
	@Override
	public StringBuilder getLast() {
		StringBuilder result = delegate.getLast();
		String myContribution = String.valueOf( getCharacterSet().charAt( getCharacterSet().length() - 1 ));
		return result.append( myContribution) ;
	}

	private String actual() {
		return String.valueOf( this.getCharacterSet().charAt( this.currentIndex ) ); 
	}
	
	@Override
	public void increment() {
		incrementer.doIncrement();
	}
	
	@Override
	public void decrement() {
		incrementer.doDecrement();
	}
	
	@Override
	public void increment(long amount) {
		if( this.getCharacterSet().length() >= amount ) {

			// If the number of values handled by this sequence is higher than amount, directly incrementing to that value
			for( int i = 0; i < amount; i++ ) {
				increment();
			}
		} else {
			// Incrementing delegate assuming that each delegate's increment is equal to a full increment cycle of this sequence
			int delegateAmount = (int) ( amount / this.getCharacterSet().length() );
			delegate.increment( delegateAmount );
			
			// handling rest
			long myAmount = amount % this.getCharacterSet().length();
			increment( myAmount );
		}
	}
	
	@Override
	public void decrement(long amount) {
		
	}
	
	@Override
	public void synchWithValue(String value) {
		if( StringUtils.isNotEmpty(value) ) {
			String newValue = String.valueOf( value.charAt( value.length() - 1 ) );
			String actual = actual();
			int diff = newValue.compareTo(actual);
			increment( diff );	
			delegate.synchWithValue( value.substring(0, value.length() - 1) );
		}
	}
	
	Sequence getDelegate() {
		return delegate;
	}
	
	public void reset() {
		this.currentIndex = 0;
		this.delegate.reset();
	}
	
	@Override
	public boolean shouldReset() {
		return this.currentIndex == this.getCharacterSet().length();
	}
	
	private interface Incrementer {
		void doIncrement();
		void doDecrement();
	}
	
	private final class SynchIncrementer implements Incrementer {
		
		@Override
		public void doIncrement() {
			synchronized(mutex) {
				ProxySequence.this.currentIndex++;	
			}
			
			if( shouldReset() ) {
				synchronized(mutex) {
					ProxySequence.this.currentIndex = 0;
				}
				delegate.increment();
			}
		}
		
		@Override
		public void doDecrement() {
			// Do Nothing
		}
	}
	
	private final class AsynchIncrementer implements Incrementer {
		
		@Override
		public void doIncrement() {
			ProxySequence.this.currentIndex++;	
			
			if( shouldReset() ) {
				ProxySequence.this.currentIndex = 0;
				delegate.increment();
			}
		}
		
		@Override
		public void doDecrement() {
			// Do Nothing
		}
	}
}
