package org.simonworks.cryptalgorithms.api;

import org.apache.commons.lang3.StringUtils;
import org.simonworks.cacheworks.api.CryptCache;
import org.simonworks.cacheworks.api.CryptCacheFactory;
import org.simonworks.smartsequences.api.SequenceGroup;

public class CachedValuesSequenceBasedCryptAlgorithm extends SequenceBasedCryptAlgorithm {
	
	private CryptCache cache;

	public CachedValuesSequenceBasedCryptAlgorithm(CryptCache cache, SequenceGroup sequence) {
		super(sequence);
		this.cache = cache;
	}
	
	public CachedValuesSequenceBasedCryptAlgorithm(SequenceGroup sequence) {
		this(CryptCacheFactory.INSTANCE.getCache(CryptCacheFactory.NOOP_CACHE), sequence);
	}
	
	@Override
	public String crypt(String container, String value) {
		String result = cache.recover(container, value);
		if(StringUtils.isBlank(result)) {
			result = getSequence().next();
			notify(container, value, result);
		}
		return result;
	}

	@Override
	public void notify(String container, String sourceValue, String cryptedValue) {
		cache.store(container, sourceValue, cryptedValue);
	}

}
