package it.oasi.crypter.engine;

import it.oasi.crypter.engine.cache.CryptCache;
import it.oasi.crypter.engine.cache.CryptCacheFactory;
import it.oasi.crypter.engine.sequence.SequenceGroup;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractCrypterEngine implements CrypterEngine {
	
	public static final String SEQUENCES_VALUES = "SEQUENCES_VALUES";

	private CryptCache cryptCache;
	private static final Logger LOG = LoggerFactory.getLogger(CrypterEngine.class);

	public AbstractCrypterEngine() {
		this(CryptCacheFactory.INSTANCE.getCache(CryptCacheFactory.NOOP_CACHE));
	}

	public AbstractCrypterEngine(CryptCache cryptCache) {
		this.cryptCache = cryptCache;
	}


	protected void store(Replacing replacing) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("store({})", replacing);
		}
		cryptCache.store(replacing.getFieldId(), replacing.getOldValue(), replacing.getNewValue().trim());
	}

	protected String recover(Replacing replacing) {
		String cryptedValue;
		String container = replacing.getFieldId();

		String toCryptValue = replacing.getOldValue();
		cryptedValue = cryptCache.recover(container, toCryptValue);
		
		if (LOG.isDebugEnabled()) {
			LOG.debug("recover({})=<{}>", replacing, cryptedValue);
		}

		return cryptedValue;
	}

	private void doAppend(StringBuilder result, int length, char filler, String newCandidate) {
		String finalvalue = StringUtils.rightPad(newCandidate, length, filler);
		if (LOG.isDebugEnabled()) {
			LOG.debug("Appending value <{}>", finalvalue);
		}
		result.append(finalvalue);
	}

	protected void append(StringBuilder result, int length, String newCandidate) {
		doAppend(result, length, ' ', newCandidate);
	}

	public CryptCache getCryptCache() {
		return cryptCache;
	}

	protected void setNewValue(SequenceGroup sequence, Replacing replacing) {
		synchronized (sequence) {
			String recovered = this.recover(replacing);
			if (recovered != null) {
				replacing.setNewValue(recovered);
			} else {
				replacing.setNewValue(sequence.next());
				this.store(replacing);
			}
		}
	}
	
}
