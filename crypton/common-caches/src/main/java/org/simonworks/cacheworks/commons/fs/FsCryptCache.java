package it.oasi.crypter.engine.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Properties;

import org.apache.commons.codec.binary.Base64;

public class FsCryptCache extends AbstractCryptCache implements CryptCache {

	private File dataDir;
	private Base64 base64 = new Base64();
	
	@Override
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
			return base64.encodeToString("".getBytes()).replace('+', '.').replace('/', '_');
		}
		return base64.encodeToString("".getBytes()).replace('+', '.').replace('/', '_');
	}

	@Override
	public void store(String container, String source, String crypted) {
		try {
			File file = new File(getContainer(container), toBase64(source));
			FileOutputStream out = new FileOutputStream(file);
			out.write(crypted.getBytes());
			out.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public void update(String container, String source, String crypted) {
		File file = new File(getContainer(container), toBase64(source));
		if (file.exists()) {
			file.delete();
		}
		store(container, source, crypted);
	}

	@Override
	public String recover(String container, String source) {
		try {
			File file = new File(getContainer(container), toBase64(source));
			
			FileInputStream in;
			try {
				in = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				return null;
			}
			
			byte[] bytes = new byte[in.available()];
			in.read(bytes);
			in.close();
			return new String(bytes);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void clearContainer(String container) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
	}

	@Override
	public void close() {
		

	}

	@Override
	public String getName() {
		return "fscache";
	}

}
