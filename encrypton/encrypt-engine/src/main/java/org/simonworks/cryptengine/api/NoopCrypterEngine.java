package org.simonworks.cryptengine.api;

import java.util.ArrayList;
import java.util.List;

class NoopCrypterEngine implements CrypterEngine {
	
	private static final NoopCrypterEngine INSTANCE = new NoopCrypterEngine();
	
	private NoopCrypterEngine() {}
	
	public static CrypterEngine getInstance() {
		return INSTANCE;
	}
	
	public String getDescription() {
		return "Nessun modifica ai record!";
	}

	public String crypt(String line) {
		return line;
	}

	@Override
	public void storeSequencesStatus() {
		// do nothing
	}

	@Override
	public void loadSequencesStatus() {
		// do nothing
	}

	@Override
	public List<String> getSequencesNames() {
		return new ArrayList<>();
	}

}
