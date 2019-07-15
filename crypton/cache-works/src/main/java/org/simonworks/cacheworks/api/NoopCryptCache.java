package org.simonworks.cacheworks.api;

class NoopCryptCache implements CryptCache {

	public boolean contains(String container, String source) {
		return false;
	}

	public void store(String container, String source, String crypted) {
		//doNothing
	}
	
	public String recover(String container, String source) {
		return "";
	}
	
	@Override
	public void clear() {
		//Nothing to do
	}
	
	@Override
	public void close() {
		clear();
	}

	@Override
	public void clearContainer(String container) {
		//Nothing to do
	}

	@Override
	public void update(String container, String source, String crypted) {
		// TODO Stub di metodo generato automaticamente
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {		
		return "noopcache";
	} 

}
