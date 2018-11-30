package it.oasi.crypter;

import it.oasi.crypter.engine.util.MemoryManager;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SystemOutProgressBar extends ProgressBar {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SystemOutProgressBar.class);
	
	private static final int PRINT_EVERY_NUM_LINES = 100000;
	private final long start;
	private long newStart = 0;
	private long newProcessedLine = 0;

	public SystemOutProgressBar(int lineNumber, long start) {
		super(lineNumber);
		this.start = start;
		this.newStart = start;
	}

	@Override
	public void onLineProcess(int processedLine) {
		newProcessedLine++;
		if (processedLine % PRINT_EVERY_NUM_LINES == 0 || processedLine == lineNumber) {
			printStatistics(processedLine);
			newProcessedLine = 0;
			newStart = System.currentTimeMillis();
		}
	}

	@Override
	public void onManyLineProcess(int processedLine) {
		printStatistics(processedLine);
	}

	private void printStatistics(int processedLine) {
		NumberFormat format = new DecimalFormat("###,###,###,###");
		long throughput = processedLine / elapsedTimeSinceStart();
		long throputLastLines = newProcessedLine / elapsedTimeSinceStart(newStart);
		String thoughput = String.format("%1$6s", format.format(throughput)) ;
		String throputLastLinesString = String.format("%1$6s",format.format(throputLastLines));
		
		
		
		String processedLines = String.format("%1$12s", format.format(processedLine)) ;
		String lastprocessedLines = String.format("%1$12s", format.format(newProcessedLine)) ;
		String progressBar = progressBar(lineNumber, processedLine, 20);
		LOGGER.info(progressBar + "Written lines " + processedLines + ", throughput <since begin " + thoughput + " l/sec> <last "+ lastprocessedLines + " l "+throputLastLinesString+" l/sec>, <mem " + format.format(MemoryManager.usedMemory()) + "%>" );
	}
	
	/**
	 * Restituisce una progress bar
	 * 
	 * @param lineNumber
	 *            il numero di righe totali
	 * @param processedLines
	 *            il numero di righr processate
	 * @param scaleVar
	 *            la scala di riferimento
	 * @return
	 */
	private String progressBar(int lineNumber, int processedLines, int scaleVar) {
		double scale = scaleVar;

		if (lineNumber == Integer.MAX_VALUE) {
			return "";
		}

		StringBuilder builder = new StringBuilder();
		builder.append("[");

		double percentage = ((double) processedLines / (double) lineNumber);

		int currentProgress = (int) (( percentage * scale) );

		int i = 0;
		for (int j = i; j < currentProgress; j++) {
			builder.append("#");
			i++;
		}

		for (int j = i; j < scale; j++) {
			builder.append("=");
		}

		builder.append("] ");
		builder.append(String.format("%1$4s", new DecimalFormat("###%").format(percentage)));
		builder.append(", ");
		return builder.toString();
	}

	/**
	 * 
	 * @return the elapsed time in seconds
	 */
	private long elapsedTimeSinceStart() {
		long result = (System.currentTimeMillis() - start) / 1000;
		if(result == 0){
			return 1;
		}
		return result;
	}
	
	
	private long elapsedTimeSinceStart(long start) {
		long result = (System.currentTimeMillis() - start) / 1000;
		if(result == 0){
			return 1;
		}
		return result;
	}
}
