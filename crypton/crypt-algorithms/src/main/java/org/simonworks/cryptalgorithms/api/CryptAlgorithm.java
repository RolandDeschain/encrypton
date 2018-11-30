package org.simonworks.cryptalgorithms.api;

/**
 * Crypt algorithms are used to crypt values. Each algorithm use its own behaviour to determine a result value from a input one.
 * 
 * @author simone.stranieri
 *
 */
public interface CryptAlgorithm {
	
	/**
	 * Crypt input value. The value's "family" is indicated by container.
	 * 
	 * @param container Value's "family"
	 * @param value Value to be crypted
	 * @return
	 */
	String crypt(String container, String value);

}
