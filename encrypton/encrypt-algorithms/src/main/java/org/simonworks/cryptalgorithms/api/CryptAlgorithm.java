package org.simonworks.cryptalgorithms.api;

/**
 * Encrypt algorithms are used to encrypt values. Each algorithm use its own behaviour to determine a result value from a input one.
 * 
 * @author simone.stranieri
 *
 */
public interface CryptAlgorithm {
	
	/**
	 * Encrypt input value. The value's "family" is indicated by container.
	 * 
	 * @param container Value's "family"
	 * @param value Value to be encrypted
	 * @return The encrypted value
	 */
	String crypt(String container, String value);

}
