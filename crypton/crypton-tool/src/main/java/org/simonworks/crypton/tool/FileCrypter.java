package it.oasi.crypter;

import it.oasi.crypter.engine.CrypterEngine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Avvia il tool
 *
 */
public class FileCrypter {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileCrypter.class);

	private int processedLines = 0;
	private int lineNumber = Integer.MAX_VALUE;
	private CrypterEngine crypterEngine;
	private File fileName;
	private File outputFile;
	private SystemOutProgressBar progressBar;
	
	private int commitInterval;

	/**
	 * Costruisce il crypter
	 * 
	 * @param crypterEngine
	 *            l'engine da utilizzare
	 * @param inputFile
	 *            il nome del file da leggere
	 * @param outputFile
	 *            il nome del file su cui scrivere
	 * @param useCache
	 *            se deve essre usata la cache interna
	 */
	public FileCrypter(CrypterEngine crypterEngine, File inputFile, File outputFile, int commitInterval) {
		this.crypterEngine = crypterEngine;
		this.fileName = inputFile;
		this.outputFile = outputFile;
		this.commitInterval = commitInterval;
	}

	public void saveCacheStatus() {
		this.crypterEngine.storeSequencesStatus();
	}

	public void start() throws IOException {

		LOGGER.info(this.crypterEngine.getDescription());

		this.crypterEngine.loadSequencesStatus();

		try {
			File input = this.fileName;

			try {
				LOGGER.info("Reading line numbers...");
				lineNumber = getLineNumber(input);
			} catch (Exception e) {
				LOGGER.error("Cannot get line number. Using default one {}", Integer.MAX_VALUE, e);
				lineNumber = Integer.MAX_VALUE;
			}
			
			LOGGER.info("Lines to be read <{}>", lineNumber);
			
			LOGGER.info("Input file " + input.getAbsolutePath());

			FileReader fileReader = new FileReader(input);
			BufferedReader reader = new BufferedReader(fileReader);

			File output = outputFile;
			LOGGER.info("Output file " + output.getAbsolutePath());

			FileWriter out = new FileWriter(output);
			BufferedWriter writer = new BufferedWriter(out);

			// new PrintWriter(output, "UTF-8");

			Runtime runtime = Runtime.getRuntime();
			long maxMemory = runtime.maxMemory();

			LOGGER.info("Running with max memory usage " + maxMemory / 1048576 + " MB");
			LOGGER.info("Mono thread mode");
			String line;
			
			progressBar = new SystemOutProgressBar(lineNumber, System.currentTimeMillis());
			
			List<String> lines = new ArrayList<String>();
			int count = 0;
			while ((line = reader.readLine()) != null) {
				if( StringUtils.isBlank(line) ) {
					break;
				}
				
				lines.add( this.crypterEngine.crypt(line) );
				count++;
				if(count == commitInterval) {
					writeLines(writer, lines);
					count = 0;
					lines.clear();
				}
			}
			
			writeLines(writer, lines);
			
			reader.close();
			reader = null;
			fileReader.close();
			fileReader = null;
			writer.close();
			writer = null;
			out.close();
			out = null;
		} 
		finally {
			saveCacheStatus();
		}
		
	}

	private int getLineNumber(File input) throws IOException {
		LineNumberReader lnr = new LineNumberReader(new FileReader(input));
		lnr.skip(Long.MAX_VALUE);
		int result = lnr.getLineNumber();
		lnr.close();
		return result;
	}

	@SuppressWarnings("unused")
	private void processLines(BufferedWriter writer, ExecutorService executor, List<CryptLineTask> tasks) throws InterruptedException, ExecutionException, IOException {
		List<Future<String>> results = executor.invokeAll(tasks);

		for (Future<String> lineResult : results) {
			writeLine(writer, lineResult.get());
		}

		tasks.clear();
	}

	private void writeLines(BufferedWriter writer, List<String> lines) throws IOException {
		StringBuilder sb = new StringBuilder();
		for (String cryptedLine : lines) {
			sb.append(cryptedLine).append("\n");
			processedLines++;
			progressBar.onLineProcess(processedLines);
		}
		writer.write(sb.toString());
		writer.flush();
	}
	
	private void writeLine(BufferedWriter writer, String cryptedLine) throws IOException {
		writer.write(cryptedLine);
		processedLines++;
		progressBar.onLineProcess(processedLines);
	}

	/**
	 * Print the progress
	 * 
	 * @param scale
	 *            la scala della progress bar
	 */
	public void printProgress(int scale) {
		progressBar.onLineProcess(processedLines);
	}

	/**
	 * Il task che esegue l'encryption della riga
	 * 
	 * @author g.maestro
	 *
	 */
	public static class CryptLineTask implements Callable<String> {

		private String line;
		private CrypterEngine crypterEngine;

		/**
		 * 
		 * @param line
		 *            la riga da cryptare
		 * @param engine
		 *            l'engine utilizzato per l'encryption
		 */
		public CryptLineTask(String line, CrypterEngine engine) {
			this.line = line;
			this.crypterEngine = engine;
		}

		@Override
		public String call() throws Exception {
			return crypterEngine.crypt(line);
		}

	}

	/**
	 * <p>
	 * Il factory utilizzato per la crezione dei thread dell'encrypter
	 * </p>
	 * 
	 * @author g.maestro
	 *
	 */
	@SuppressWarnings("unused")
	private static final class LineCrypterThreadFactory implements ThreadFactory {
		private int number = 0;

		@Override
		public Thread newThread(Runnable r) {
			return new Thread(r, "Line Encrypter " + ++number);
		}
	}

}