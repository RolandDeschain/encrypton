package org.simonworks.smartsequences.api;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BoundedNumbersSequenceGroupTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger( BoundedNumbersSequenceGroupTest.class ); 

	@Test
	public void test0_4() {
		LOGGER.info( "Prints from 0 to 4" );
		String curr = null;
		BoundedNumbersSequenceGroup s = new BoundedNumbersSequenceGroup(1, 0, 5);
		while( !s.isUpperBoundExceeded( curr = s.next()) ) {
			LOGGER.info( curr );
		}
		LOGGER.info( curr );
		Assert.assertEquals("4", curr);
		Assert.assertTrue(s.isUpperBoundExceeded(curr));
	}
	@Test
	public void test0_5() {
		LOGGER.info( "Prints from 0 to 5" );
		String curr = null;
		BoundedNumbersSequenceGroup s = new BoundedNumbersSequenceGroup(1, 0, 6);
		while( !s.isUpperBoundExceeded( curr = s.next()) ) {
			LOGGER.info( curr );
		}
		LOGGER.info( curr );
		Assert.assertEquals("5", curr);
		Assert.assertTrue(s.isUpperBoundExceeded(curr));
	}
	
	@Test
	public void test0_9() {
		LOGGER.info( "Prints from 0 to 9" );
		String curr = null;
		BoundedNumbersSequenceGroup s = new BoundedNumbersSequenceGroup(2, 0, 10);
		while( !s.isUpperBoundExceeded( curr = s.next()) ) {
			LOGGER.info( curr );
		}
		LOGGER.info( curr );
		Assert.assertEquals("09", curr);
		Assert.assertTrue(s.isUpperBoundExceeded(curr));
	}

	@Test
	public void test0_99() {
		LOGGER.info( "Prints from 0 to 9" );
		String curr = null;
		BoundedNumbersSequenceGroup s = new BoundedNumbersSequenceGroup(2, 0, 99);
		while( !s.isUpperBoundExceeded( curr = s.next()) ) {
			LOGGER.info( curr );
		}
		LOGGER.info( curr );
		Assert.assertEquals("98", curr);
		Assert.assertTrue(s.isUpperBoundExceeded(curr));
	}
	
	@Test
	public void test0_8() {
		LOGGER.info( "Prints from 0 to 8" );
		String curr = null;
		BoundedNumbersSequenceGroup s = new BoundedNumbersSequenceGroup(1, 0, 9);
		while( !s.isUpperBoundExceeded( curr = s.next()) ) {
			LOGGER.info( curr );
		}
		LOGGER.info( curr );
		Assert.assertEquals("8", curr);
		Assert.assertTrue(s.isUpperBoundExceeded(curr));
	}
	
	@Test
	public void test3_8() {
		LOGGER.info( "Prints from 3 to 8" );
		String curr = null;
		BoundedNumbersSequenceGroup s = new BoundedNumbersSequenceGroup(1, 3, 9);
		curr = s.next();
		LOGGER.info( curr );
		Assert.assertEquals("3", curr);
		while( !s.isUpperBoundExceeded( curr = s.next() ) ) {
			LOGGER.info( curr );
		}
		LOGGER.info( curr );
		Assert.assertEquals("8", curr);
		Assert.assertTrue(s.isUpperBoundExceeded(curr));
	}

}
