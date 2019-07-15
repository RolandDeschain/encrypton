package org.simonworks.smartsequences.commons;

import org.simonworks.smartsequences.api.ProxySequenceGroup;
import org.simonworks.smartsequences.api.BoundedNumbersSequenceGroup;

public class DatesSequenceGroup extends ProxySequenceGroup {
	
	public DatesSequenceGroup() {
		super(
				new BoundedNumbersSequenceGroup(4, 0, 9999),
				new BoundedNumbersSequenceGroup(2, 1, 13),
				new BoundedNumbersSequenceGroup(2, 1, 29));
	}
	
	public static void main(String[] args) {
		DatesSequenceGroup dsg = new DatesSequenceGroup();
		
		for( int i = 0; i<= 1000; i++) {
			System.out.println( dsg.next() );
		}
	}

}
