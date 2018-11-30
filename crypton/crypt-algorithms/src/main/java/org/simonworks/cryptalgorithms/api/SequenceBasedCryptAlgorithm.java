package it.oasi.crypter.engine.algorithm;

import it.oasi.crypter.engine.sequence.SequenceGroup;


/**
 * As its name says, a {@link SequenceBasedCryptAlgorithm} is based on a sequence to get crypted version of a input value.
 * 
 * 
 * @author simone.stranieri
 *
 */
public abstract class SequenceBasedCryptAlgorithm implements CryptAlgorithm {
	
	private SequenceGroup sequence;
	
	public SequenceBasedCryptAlgorithm(SequenceGroup sequence) {
		this.sequence = sequence;
	}

	@Override
	public String crypt(String container, String value) {
		String crypted = sequence.next();
		notify(container, value, crypted);
		return crypted;
	}
	
	protected SequenceGroup getSequence() {
		return sequence;
	}
	
	public abstract void notify(String container, String sourceValue, String cryptedValue);

}
