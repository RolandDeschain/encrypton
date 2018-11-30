package org.simonworks.smartsequences.api;

import java.math.BigInteger;

public interface Sequence {
	
	/**
	 * 
	 * @return il set dei caratteri gestiti da questa Sequence
	 */
	String getCharacterSet();
	
	/**
	 * 
	 * @return la prima String(Builder) gestita dalla sequenza
	 */
	StringBuilder getFirst();
	
	/**
	 * 
	 * @return la prossima String(Builder) in sequenza
	 */
	StringBuilder get();
	
	/**
	 * 
	 * @return l'ultima String(Builder) gestita dalla sequenza
	 */
	StringBuilder getLast();
	
	/**
	 * Avanza l'indice corrente della sequenza di una unita
	 */
	void increment();
	
	/**
	 * Avanza l'indice corrente di "amount" unita
	 */
	void increment(long amount);
	
	/**
	 * Riporta indietro l'indice corrente della sequenza di una unita
	 */
	void decrement();
	
	/**
	 * Riporta indietro l'indice corrente di "amount" unita
	 */
	void decrement(long amount);
	
	/**
	 * Avanza l'indice fino ad un determinato valore 
	 */
	void synchWithValue(String value);
	
	/**
	 * 
	 * @return il numero di delegati che partecipano alla determina dei valori di questa sequence
	 */
	int countDelegates();
	
	/**
	 * 
	 * @return il numero di possibili combinazioni che possono essere ottenute da questa sequence
	 */
	BigInteger countPermutations();
	
}
