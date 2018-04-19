package hu.elte.txtuml.validation.common;

import static java.lang.Character.isLetter;
import static java.lang.Character.toLowerCase;
import static java.lang.Character.toUpperCase;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

import hu.elte.txtuml.utils.Logger;

/**
 * Can load messages from a properties file.
 */
public class MessageLoader {

	/**
	 * Can be null.
	 */
	private Properties properties;

	public MessageLoader(String propertyFileFullyQualifiedName) {
		ResourceBundle resourceBundle;
		try {
			resourceBundle = ResourceBundle.getBundle(propertyFileFullyQualifiedName);
		} catch (MissingResourceException e) {
			Logger.sys.error("Could not load property file " + propertyFileFullyQualifiedName);
			return;
		}

		properties = new Properties();

		Enumeration<String> keys = resourceBundle.getKeys();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			properties.put(key, resourceBundle.getString(key));
		}
	}

	/**
	 * In case of an unknown key, an empty string is returned.
	 */
	public String getMessage(String key) {
		String result = getMessageNoDefault(key);
		return result == null ? "" : result;
	}

	/**
	 * In case of an unknown key, an empty string is returned.
	 * <p>
	 * Removes key-value pair from memory (not from file).
	 */
	public String getAndRemoveMessage(String key) {
		String result = getAndRemoveMessageNoDefault(key);
		return result == null ? "" : result;
	}

	/**
	 * In case of an unknown key, {@code null} is returned.
	 */
	public String getMessageNoDefault(String key) {
		if (properties == null) {
			return null;
		}
		return properties.getProperty(key);
	}

	/**
	 * In case of an unknown key, {@code null} is returned.
	 * <p>
	 * Removes key-value pair from memory (not from file).
	 */
	public String getAndRemoveMessageNoDefault(String key) {
		String result = getMessageNoDefault(key);
		if (result != null) {
			properties.remove(key);
			if (properties.isEmpty()) {
				properties = null;
			}
		}
		return result;
	}

	/**
	 * Fills the static, non-final String fields of the given class with loaded
	 * messages (the field names are considered to be the keys). Found messages
	 * are removed from this loader, in case of not found messages, empty
	 * strings are loaded to the fields.
	 */
	public void fillFields(Class<?> cls) {
		for (Field f : cls.getFields()) {
			int modifiers = f.getModifiers();
			if (Modifier.isStatic(modifiers) && !Modifier.isFinal(modifiers) && f.getType() == String.class) {
				try {
					f.setAccessible(true);
					f.set(null, getAndRemoveMessage(f.getName()));
				} catch (SecurityException e) {
					Logger.sys.info("User message " + f.getName() + " cannot be loaded.");
				} catch (IllegalAccessException e) {
					// Cannot happen.
				}
			}
		}
	}

	/**
	 * Given key is converted to camel case with a capital letter at the
	 * beginning and the suffix text is appended to its end (suffix is not
	 * converted). Any non-letter characters from key are considered as word
	 * separators and are skipped.
	 */
	public static String convertToCamelCase(String key, String suffix) {
		StringBuilder builder = new StringBuilder();
		final char[] chars = key.toCharArray();

		boolean wordStart = true;
		for (char c : chars) {
			if (isLetter(c)) {
				if (wordStart) {
					builder.append(toUpperCase(c));
					wordStart = false;
				} else {
					builder.append(toLowerCase(c));
				}
			} else {
				wordStart = true;
			}
		}
		return builder.append(suffix).toString();
	}

}