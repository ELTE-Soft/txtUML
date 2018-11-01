package hu.elte.txtuml.export.diagrams.common.preferences;

import java.util.Arrays;
import java.util.Collection;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;

/**
 * Handles the preferences that are saved by eclipse
 */
@SuppressWarnings("javadoc")
public class PreferencesManager{
	
	private static IEclipsePreferences prefs = InstanceScope.INSTANCE.getNode("hu.elte.txtuml.export.diagrams.common");
	private static final String STRING_DELIMITER = "__#010#__"; 
	
	public static final String TXTUML_VISUALIZE_TXTUML_MODEL = "txtUML Visualize txtUML Model";
	public static final String TXTUML_VISUALIZE_TXTUML_PROJECT = "txtUML Visualize txtUML Project";
	public static final String TXTUML_VISUALIZE_TXTUML_LAYOUT = "txtUML Visualize txtUML Layout";
	public static final String TXTUML_VISUALIZE_TXTUML_LAYOUT_PROJECTS = "txtUML Visualize txtUML Layout Projects";
	
	/**
	 * Sets the value of a preference
	 * @param name - The preference
	 * @param value - The value
	 */
	public static void setValue(String name, boolean value){
		prefs.putBoolean(name, value);
	}
	
	/**
	 * Sets the value of a preference
	 * @param name - The preference
	 * @param value - The value
	 */
	public static void setValue(String name, double value){
		prefs.putDouble(name, value);
	}
	
	/**
	 * Sets the value of a preference
	 * @param name - The preference
	 * @param value - The value
	 */
	public static void setValue(String name, float value){
		prefs.putFloat(name, value);
	}
	
	/**
	 * Sets the value of a preference
	 * @param name - The preference
	 * @param value - The value
	 */
	public static void setValue(String name, int value){
		prefs.putInt(name, value);
	}
	
	/**
	 * Sets the value of a preference
	 * @param name - The preference
	 * @param value - The value
	 */
	public static void setValue(String name, long value){
		prefs.putLong(name, value);
	}
	
	/**
	 * Sets the value of a preference
	 * @param name - The preference
	 * @param value - The value
	 */
	public static void setValue(String name, String value){
		prefs.put(name, value);
	}
	
	public static void setValue(String name, Collection<String> collection){
		String value = String.join(STRING_DELIMITER, collection);
		setValue(name, value);
	}
	
	/**
	 * Gets the value of a preference
	 * @param name - The preference
	 */
	public static String getString(String name){
		return prefs.get(name, "");
	}
	
	/**
	 * Gets the value of a preference
	 * @param name - The preference
	 */
	public static boolean getBoolean(String name){
		return prefs.getBoolean(name, false);
	}

	/**
	 * Gets the value of a preference
	 * @param name - The preference
	 */
	public static int getInt(String name){
		return prefs.getInt(name, 0);
	}
	
	/**
	 * Gets the value of a preference
	 * @param name - The preference
	 */
	public static double getDouble(String name){
		return prefs.getDouble(name, 0);
	}
	
	/**
	 * Gets the value of a preference
	 * @param name - The preference
	 */
	public static float getFloat(String name){
		return prefs.getFloat(name, 0);
	}

	/**
	 * Gets the value of a preference
	 * @param name - The preference
	 */
	public static long getLong(String name){
		return prefs.getLong(name, 0);
	}
	
	public static Collection<String> getStrings(String name){
		String storedString = getString(name);
		String[] values = storedString.split(STRING_DELIMITER);
		return Arrays.asList(values);
	}
}
