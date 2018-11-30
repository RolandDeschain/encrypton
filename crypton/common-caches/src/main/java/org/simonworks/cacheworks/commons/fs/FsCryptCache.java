package org.simonworks.cacheworks.commons.fs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Properties;

import org.simonworks.cacheworks.api.AbstractCryptCache;
import org.simonworks.cacheworks.api.CryptCache;

public class FsCryptCache extends AbstractCryptCache implements CryptCache {

	private File dataDir;

	public boolean contains(String container, String source) {
		
		/**
		 * TODO To be implemented
		 */
		
		return false;
	}

	@Override
	public void init() {
		Properties configuration = getConfiguration();

		dataDir = new File(configuration.getProperty("fs.dataDir"));

		if (dataDir.exists() && !dataDir.isDirectory()) {
			throw new IllegalArgumentException(dataDir.getAbsolutePath() + " non e' una directory");
		}

		if (!dataDir.exists()) {
			dataDir.mkdir();
		}

	}

	private File getContainer(String container) {

		File result = new File(dataDir,container);

		if (!result.exists()) {
			result.mkdir();
		}

		return result;

	}
	
	
	private String toBase64(String toEncode) {
		if (toEncode == null) {
			return Base64.getEncoder().encodeToString("".getBytes()).replace('+', '.').replace('/', '_');
		}
		return Base64.getEncoder().encodeToString("".getBytes()).replace('+', '.').replace('/', '_');
	}

	@Override
	public void store(String container, String source, String crypted) {
		File file = new File(getContainer(container), toBase64(source));
		try (FileOutputStream out = new FileOutputStream(file);){
			
			out.write(crypted.getBytes());
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public void update(String container, String source, String crypted) {
		Path path = Paths.get(this.dataDir.getPath(), container, toBase64(source));
		try {
			Files.delete(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		store(container, source, crypted);
	}

	@Override
	public String recover(String container, String source) {
		File file = new File(getContainer(container), toBase64(source));
		try (FileInputStream in = new FileInputStream(file); ) {
			byte[] bytes = new byte[in.available()];
			while( in.read(bytes) > 0 );
			return new String(bytes);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void clearContainer(String container) {
		/**
		 * To be implemented
		 */
	}

	@Override
	public void clear() {
		/**
		 * To be implemented
		 */
	}

	@Override
	public void close() {
		/**
		 * Nothing to do here
		 */
	}

	@Override
	public String getName() {
		return "fscache";
	}

}
