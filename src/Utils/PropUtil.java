package Utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropUtil {
	private static Properties properties = null;
	
	

	public PropUtil(String path) throws FileNotFoundException {
		initialize(path);
	}

	private void initialize(String path) throws FileNotFoundException {

		InputStream is = new BufferedInputStream(new FileInputStream(path));
		if (is == null) {

			return;
		}
		properties = new Properties();
		try {
			properties.load(is);
		} catch (IOException e) {
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * get specified key in config files
	 * 
	 * @param key
	 *            the key name to get value
	 */
	public String get(String key) {
		String keyValue = null;
		if (properties.containsKey(key)) {
			keyValue = (String) properties.get(key);
		}
		return keyValue;
	}

	public void set(String key, String value) {
		properties.setProperty(key, value);
	}
}
