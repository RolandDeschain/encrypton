package it.oasi.crypter.engine;

/**
 * Questo oggetto si occupa di criptare una String in un formato non reversibile
 * 
 * @author simone.stranieri
 *
 */
public interface CrypterEngine {
	
	/**
	 * Cripta la String in input e la restituisce in output
	 * 
	 * @param line La String da criptare
	 * @return Il formato criptato della String in input
	 */
	String crypt(String line);
	
	/**
	 * @return Una descrizione del tipo di algoritmo che sara' applicato
	 */
	String getDescription();
	
	/**
	 * Salva lo stato di avanzamento delle sequenze usando la cache
	 */
	void storeSequencesStatus();
	
	/**
	 * Recupera lo stato di avanzamento delle sequenze usando la cache
	 */
	void loadSequencesStatus();
	
}
