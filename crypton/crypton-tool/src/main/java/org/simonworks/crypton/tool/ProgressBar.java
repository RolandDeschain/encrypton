package org.simonworks.crypton.tool;

/**
 * Una classe astratta che implementa una progress bar
 *
 */
public abstract class ProgressBar {

	protected final int lineNumber;

	/**
	 * Costruisce la barra di avanzamento
	 * 
	 * @param lineNumber
	 *            il numero di linee massimo
	 */
	public ProgressBar(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	
	/**
	 * Notifica il numero di linea processato
	 * @param processedLine
	 */
	public abstract void onLineProcess(int processedLine);
	
	/**
	 * Notifica il numero di linea processato
	 * @param processedLine
	 */
	public abstract void onManyLineProcess(int processedLine);
	
}
