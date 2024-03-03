package org.simonworks.cryptalgorithms.api;

import java.util.Collection;

import org.apache.commons.lang3.StringUtils;

public abstract class BaseFieldCrypter {

	

	private final int offsetBase1;
	private final int len;
	
	
	public BaseFieldCrypter(int offsetBase1, int len) {
		super();
		this.offsetBase1 = offsetBase1;
		this.len = len;
	}
	
	private void crypt(char[] src, char[] dest) {
		
		int offset = offsetBase1-1;
		String plain = new String(src,offset,len);		
		String crypt = crypt(plain);
		char[] encrypted = StringUtils.rightPad(crypt,len,' ').toCharArray();		
		System.arraycopy(encrypted,0,dest,offset,encrypted.length);
	}
	
	public abstract String crypt(String plain);
	
	
	public static String apply(String line, Collection<BaseFieldCrypter> crypters) {
		char[] src = line.toCharArray();		
		char[] dest = line.toCharArray();
		for(BaseFieldCrypter crypter : crypters) crypter.crypt(src,dest);
		return new String(dest);
	}
	
	
}
