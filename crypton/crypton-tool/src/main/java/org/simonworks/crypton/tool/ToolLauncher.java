package it.oasi.crypter;

import it.oasi.crypter.engine.CrypterEngine;
import it.oasi.crypter.engine.CrypterEngineFactory;
import it.oasi.crypter.engine.cache.CryptCache;
import it.oasi.crypter.engine.cache.CryptCacheFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToolLauncher {

	private static final Logger LOGGER = LoggerFactory.getLogger(ToolLauncher.class);

	private static long start = 0;

	private static final String CRYPT_TOOL = "crypt-tool";
	private static final String DESCRIPTION = "OASI FLOWS CRYPT TOOL e' un tool che permette di cryptare le informazioni sensibili di un file\n";

	/**
	 * Il main del programma
	 * 
	 * @param args
	 *            gli argomenti
	 * @throws Exception
	 */
	public static void main(String[] args) {

		Options options = options();
		CommandLineParser defaultParser = new DefaultParser();
		String fileModeOption = null;
		String cacheType = null;
		String inputFile = null;
		String outputFile = null;
		int commitInterval = 1;

		CommandLine line = null;
		try {
			line = defaultParser.parse(options, args);

			for (Option currentOption : options.getOptions()) {
				if (line.hasOption("h")) {
					printHelp(options, CRYPT_TOOL);
				}

				if (currentOption.isRequired() && StringUtils.isEmpty(line.getOptionValue(currentOption.getOpt()))) {
					LOGGER.info(String.format("Parametro obbligatorio [%s] non fornito", currentOption.getOpt()));
					printHelp(options, CRYPT_TOOL);
				}

				fileModeOption = line.getOptionValue("e");

				if (!CrypterEngineFactory.INSTANCE.listEngines().contains(fileModeOption)) {
					LOGGER.info(String.format("Engine [%s] non valido ", fileModeOption));
					printHelp(options, CRYPT_TOOL);
				}

				cacheType = line.getOptionValue("c");

				if (!CryptCacheFactory.INSTANCE.listCaches().contains(cacheType)) {
					LOGGER.info(String.format("Cache [%s] non valida ", cacheType));
					printHelp(options, CRYPT_TOOL);
				}

				inputFile = line.getOptionValue("if");

				if (!new File(inputFile).exists()) {
					LOGGER.info(String.format("File [%s] non esistente ", inputFile));
					printHelp(options, CRYPT_TOOL);
				}

				if (line.hasOption("of")) {
					outputFile = line.getOptionValue("of");
				} else {
					outputFile = inputFile + ".crypt";
				}
				
				if (line.hasOption("ci")) {
					String ci = line.getOptionValue("ci");
					try {
						commitInterval = Integer.parseInt(ci);
					} catch(Exception e) {
						LOGGER.error("Malformed ci (commit interval) value <{}>. Default value <{}> will be used", ci, commitInterval);
					}
				}

			}

		} catch (ParseException e) {
			LOGGER.error(e.getMessage(),e);
			printHelp(options, CRYPT_TOOL);
		}
		
		LOGGER.info("Commit interval is <{}>", commitInterval);

		LOGGER.info(String.format("Esecuzione con i parametri %s", Arrays.toString(args)));

		boolean useCache = !line.hasOption("dc");
		boolean useFirstLevelCache = false;
		if(!useCache) {
			LOGGER.warn("Cache disabilitata");
			cacheType = CryptCacheFactory.NOOP_CACHE;
		} else {
			LOGGER.info("Cache attivata <{}>", cacheType);
			if(line.hasOption("flc")) {
				LOGGER.info("Cache di primo livello in memory attivata");
				useFirstLevelCache = true;
			} else {
				LOGGER.info("Cache di primo livello in memory disabilitata");
			}
		}
		
		CryptCache cryptCache = CryptCacheFactory.INSTANCE.getCache(cacheType, useFirstLevelCache);
		cryptCache.init();
		
		CrypterEngine crypterEngine = CrypterEngineFactory.INSTANCE.getInstance(fileModeOption, cryptCache);

		boolean directoryMode = false;
		
		if (new File(inputFile).isDirectory()) {
			directoryMode = true;
			
			if (outputFile == null) {
				LOGGER.error("Se il file di input e' una directory si deve definire la directory di output");
				System.exit(-1);
			}
			
			if (new File(outputFile).exists() && !new File(outputFile).isDirectory()) {
				LOGGER.error("Se il file di input e' una directory lo deve essere anche il path di output");
				System.exit(-1);
			}
			
			
			if (!new File(outputFile).exists() ) {
				new File(outputFile).mkdir();
			}
		}
		
		try {
			crypterEngine.loadSequencesStatus();
			if (directoryMode) {
				for (File toProcess : new File(inputFile).listFiles() ) {
					doFile(toProcess.getAbsolutePath(), new File(outputFile , toProcess.getName()+ ".crypted" ).getAbsolutePath(),   commitInterval, crypterEngine);
				}
			} else {
				doFile(inputFile, outputFile, commitInterval, crypterEngine);
			}
		} catch(Exception e) {
			LOGGER.error("Cannot process file! ", e);
		} finally {
			if (cryptCache != null) {
				crypterEngine.storeSequencesStatus();
				cryptCache.close();
			}
		}
	}

	private static void doFile(String fileName, String outputFile, int commitInterval, CrypterEngine crypterEngine) throws Exception {
		FileCrypter fileCrypter = new FileCrypter(crypterEngine, new File(fileName), new File(outputFile), commitInterval);
		try {
			start = System.currentTimeMillis();
			fileCrypter.start();
			LOGGER.info("File "+fileName+" Executed in  " + elapsedTimeSinceStart() + " seconds");
		} catch (IOException e) {
			LOGGER.error("Errore duante il processo", e);
		}
	}

	protected static void printHelp(Options options, String command) {
		LOGGER.info(DESCRIPTION);
		HelpFormatter helpFormatter = new HelpFormatter();
		helpFormatter.setWidth(180);
		helpFormatter.printHelp(command, options);
		System.exit(0);
	}

	private static Options options() {
		Options options = new Options();

		options.addOption("e", "engine", true, "Il nome del motore da utilizzare, valori validi " + CrypterEngineFactory.INSTANCE.listEngines());
		options.getOption("e").setRequired(true);

		options.addOption("c", "cache", true, "Il tipo di cache da utilizzare, valori validi " + CryptCacheFactory.INSTANCE.listCaches());
		options.getOption("c").setRequired(true);

		options.addOption("if", "input-file", true, "Il nome del file di input, se viene passato i lpath di una directory saranno processati tutti i file in essa contenuti.");
		options.getOption("if").setRequired(true);

		options.addOption("of", "output-file", true, "Il nome del file di output. Se il parametro di input e' una cartella lo deve essere anche l'output");
		options.getOption("of").setRequired(false);

		options.addOption("h", "help", false, "Stampa questa guida");

		options.addOption("dc", "disablecache", false, "Se impostato disabilita totalmente la funzione di caching delle chiavi");
		
		options.addOption("flc", "first-level-cache", false, "Se impostato abilita una cache di primo livello \"in-memory\". Non ha effetto se l'opzione \"dc\" e' abilitata ");
		
		options.addOption("ci", "commit-interval", true, "Determina il numero di righe del file da leggere in input, prima di effettuare una scrittura in output.");

		for (Option opt : options.getOptions()) {
			if (opt.isRequired()) {
				opt.setDescription(opt.getDescription() + " (*)");
			}
		}

		return options;
	}

	/**
	 * 
	 * @return the elapsed time in seconds
	 */
	private static long elapsedTimeSinceStart() {
		return (System.currentTimeMillis() - start) / 1000;
	}

}
