package it.oasi.crypter.engine.algorithm;

import it.oasi.crypter.engine.sequence.Sequence;
import it.oasi.crypter.engine.sequence.SequenceGroup;

/**
 * 
 * 
 * @author simone.stranieri
 *
 */
public class Mirror1CryptAlgorithm extends SequenceBasedCryptAlgorithm {

	public Mirror1CryptAlgorithm(SequenceGroup sequence) {
		super(sequence);
	}

	@Override
	public String crypt(String container, String value) {
		Sequence engine = getSequence().getEngine();
		String first = engine.getFirst().toString();
		int diff = value.compareTo( first );
		
		return String.valueOf(diff);
	}

	@Override
	public void notify(String container, String sourceValue, String cryptedValue) {
		// TODO Stub di metodo generato automaticamente
		
	}

}
