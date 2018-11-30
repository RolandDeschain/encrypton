package org.simonworks.smartsequences.commons.er.h2;

import java.util.HashMap;
import java.util.Map;

import it.oasi.crypter.engine.AbstractCrypterEngine;

public class CaesarCache implements CryptCache {

	static Map<Character, Character> chars = new HashMap<Character, Character>();
	
	static {
		
		chars.put('A', 'B');
		chars.put('B', 'C');
		chars.put('C', 'D');
		chars.put('D', 'E');
		chars.put('E', 'F');
		chars.put('F', 'G');
		chars.put('G', 'H');
		chars.put('H', 'I');
		chars.put('I', 'L');
		chars.put('L', 'M');
		chars.put('M', 'N');
		chars.put('N', 'O');
		chars.put('O', 'P');
		chars.put('P', 'Q');
		chars.put('Q', 'R');
		chars.put('R', 'S');
		chars.put('S', 'T');
		chars.put('T', 'U');
		chars.put('U', 'V');
		chars.put('V', 'Z');
		chars.put('Z', 'A');
		
		chars.put('J', 'K');
		chars.put('K', 'W');
		chars.put('W', 'X');
		chars.put('X', 'Y');
		chars.put('Y', 'J');

		
		
		chars.put('0', '1');
		chars.put('1', '2');
		chars.put('2', '3');
		chars.put('3', '4');
		chars.put('4', '5');
		chars.put('5', '6');
		chars.put('6', '7');
		chars.put('7', '8');
		chars.put('8', '9');
		chars.put('9', '0');
		
		

		
	}
	
	
	@Override
	public void init() {
		// make nothing
	}

	@Override
	public void store(String container, String source, String crypted) {
		// make nothing
	}

	@Override
	public void update(String container, String source, String crypted) {
		// make nothing
		
	}

	@Override
	public String recover(String container, String source) {	
		
		if (AbstractCrypterEngine.SEQUENCES_VALUES.equals(container)) {
			return "";
		}
		
		if (source == null) {
			return null;
		}
		char[] characthers = source.toUpperCase().toCharArray();
		
		StringBuilder builder = new StringBuilder();
		
		for (char ch : characthers) {
			Character newChar = chars.get(ch);
			if (newChar == null) {
				builder.append(ch);
			} else {
				builder.append(newChar);
			}
		}
		return builder.toString();
	}

	@Override
	public void clearContainer(String container) {
		// make nothing
	}

	@Override
	public void clear() {
		// make nothing
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean contains(String container, String source) {
		// TODO Stub di metodo generato automaticamente
		return false;
	}

}
