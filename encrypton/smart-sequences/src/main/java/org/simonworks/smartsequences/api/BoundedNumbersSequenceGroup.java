package org.simonworks.smartsequences.api;

public class BoundedNumbersSequenceGroup extends NumberSequencesGroup {
	
	private final int lowerLimit;
	private final int upperLimit;
	
	public BoundedNumbersSequenceGroup(int nDelegates, int lowerLimit, int upperLimit) {
		super( nDelegates );
		this.lowerLimit = lowerLimit;
		this.upperLimit = upperLimit;
	}
	
	@Override
	public String next() {
		String result = super.next();
		if( this.isLowerBoundExceeded(result) ) {
			return next();
		}
		if( this.isUpperBoundExceeded(result) ) {
			super.reset();
		}
		return result;
	}
	
	public boolean isUpperBoundExceeded(String s) {
		int asInt = Integer.parseInt( s );
		return asInt >= this.upperLimit - 1;
	}
	
	public boolean isLowerBoundExceeded(String s) {
		int asInt = Integer.parseInt( s );
		return asInt < this.lowerLimit;
	}
	
}
