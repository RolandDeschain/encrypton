package org.simonworks.smartsequences.api;

import java.math.BigInteger;

public abstract class ProxySequence implements Sequence {
	
	public static final int MIN_VALUE = 0;
	private Sequence delegate;
	private int currentIndex = 0;
	
	private Incrementer incrementer;
	
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
			/**
			 * Se questa sequence gestisce piu' valori di amount
			 * avanzo direttamente fino a quel valore
			 */
			for( int i = 0; i < amount; i++ ) {
				increment();
			}
		} else {
			/**
			 * Faccio avanzare il mio delegato assumento che ogni suo avanzamento equivale ad un mio avanzamento di
			 * tante posizioni quanti sono i valori che posso gestire.
			 */
			int delegateAmount = (int) ( amount / this.getCharacterSet().length() );
			delegate.increment( delegateAmount );
			
			/**
			 * Avanzo dell'eventuale resto
			 */
			long myAmount = amount % this.getCharacterSet().length();
			increment( myAmount );
		}
	}
	
	@Override
	public void decrement(long amount) {
		
	}
	
	@Override
	public void synchWithValue(String value) {
		String newValue = String.valueOf( value.charAt( value.length() - 1 ) );
		String actual = actual();
		int diff = newValue.compareTo(actual);
		increment( diff );	
		delegate.synchWithValue( value.substring(0, value.length() - 1) );
	}
	
	Sequence getDelegate() {
		return delegate;
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
			
			if(ProxySequence.this.currentIndex == ProxySequence.this.getCharacterSet().length()) {
				synchronized(mutex) {
					ProxySequence.this.currentIndex = 0;
				}
				delegate.increment();
			}
		}
		
		@Override
		public void doDecrement() {
			/**
			 * Do nothing
			 */
		}
	}
	
	private final class AsynchIncrementer implements Incrementer {
		
		@Override
		public void doIncrement() {
			ProxySequence.this.currentIndex++;	
			
			if(ProxySequence.this.currentIndex == ProxySequence.this.getCharacterSet().length()) {
				ProxySequence.this.currentIndex = 0;
				delegate.increment();
			}
		}
		
		@Override
		public void doDecrement() {
			/**
			 * Do nothing
			 */
		}
	}
}
