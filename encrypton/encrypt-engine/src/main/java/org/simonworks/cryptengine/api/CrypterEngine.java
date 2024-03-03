package org.simonworks.cryptengine.api;

import java.util.List;

/**
 * Crypting interface for Strings
 * 
 * @author simone.stranieri
 *
 */
public interface CrypterEngine {
	
	/**
	 * Crypts input String and return the crypted one
	 * 
	 * @param line The String to crypt
	 * @return Crypted String
	 */
	String crypt(String line);
	
	/**
	 * @return A short description about this crypter logics
	 */
	String getDescription();
	
	/**
	 * Saves actual status of internal sequences used for values' substitution.
	 */
	void storeSequencesStatus();
	
	/**
	 * Recovers status of internal sequences used for values'  substitution.
	 */
	void loadSequencesStatus();
	
	/**
	 * Each CrypterEngine implementation must indicate which sequenced should be stored/recovered during stop/restart phases
	 */
	List<String> getSequencesNames();
}
