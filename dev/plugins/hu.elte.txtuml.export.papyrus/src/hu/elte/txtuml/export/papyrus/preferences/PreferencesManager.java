package hu.elte.txtuml.export.papyrus.preferences;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jface.preference.IPreferenceStore;

import hu.elte.txtuml.export.papyrus.Activator;

/**
 * Handles the preferences that are saved by eclipse
 */
@SuppressWarnings("javadoc")
public class PreferencesManager{
	
	
	private static IPreferenceStore store = Activator.getDefault().getPreferenceStore() ;
	private static Map<String, Object> fieldsWithDefaultValues;
	private static final String STRING_DELIMITER = "__#010#__"; 
	
	public static final String TXTUML_VISUALIZE_TXTUML_MODEL = "txtUML Visualize txtUML Model";
	public static final String TXTUML_VISUALIZE_TXTUML_PROJECT = "txtUML Visualize txtUML Project";
	public static final String TXTUML_VISUALIZE_DESTINATION_FOLDER = "txtUML Visualize Destination Folder";
	public static final String TXTUML_VISUALIZE_TXTUML_LAYOUT = "txtUML Visualize txtUML Layout";
	public static final String TXTUML_VISUALIZE_TXTUML_LAYOUT_PROJECTS = "txtUML Visualize txtUML Layout Projects";
	public static final String GENERATE_STATEMACHINES_AUTOMATICALLY = "generate StateMachines Automatically";
	
	static{
		fieldsWithDefaultValues = new HashMap<String, Object>();
		fieldsWithDefaultValues.put(TXTUML_VISUALIZE_TXTUML_MODEL, "");
		fieldsWithDefaultValues.put(TXTUML_VISUALIZE_DESTINATION_FOLDER, "gen");
	}
	
	/**
	 * Sets the default values for each preference
	 */
	public static void setDefaults(){
		Iterator<Map.Entry<String, Object>> it = fieldsWithDefaultValues.entrySet().iterator();
		while (it.hasNext()) {
	        Map.Entry<String, Object> pairs = it.next();
	        
	        String Key =  pairs.getKey();
	        Object Value = pairs.getValue();
	        
	        if(Value instanceof Integer){
	        	store.setDefault(Key, (Integer) Value);
	        }else if(Value instanceof Boolean){
	        	store.setDefault(Key, (Boolean) Value);
	        }else if(Value instanceof Double){
	        	store.setDefault(Key, (Double) Value);
	        }else if(Value instanceof Float){
	        	store.setDefault(Key, (Float) Value);
	        }else if(Value instanceof Long){
	        	store.setDefault(Key, (Long) Value);
	        }else if(Value instanceof String){
	        	store.setDefault(Key, (String) Value);
	        }
	    }
	}
	
	/**
	 * Sets all preferences to the default
	 */
	public static void resetDefaults(){
		Iterator<Entry<String, Object>> it = fieldsWithDefaultValues.entrySet().iterator();
		while (it.hasNext()) {
	        Map.Entry<String, Object> pairs = it.next();
	        store.setToDefault(pairs.getKey());
	    }
	}
	
	/**
	 * Sets the values for preferences
	 * @param mp - A map with Preference-Value keys
	 */
	@SuppressWarnings("unchecked")
	public static void setValues(Map<String, Object> mp){
		Iterator<Map.Entry<String, Object>> it = mp.entrySet().iterator();
		
		while (it.hasNext()) {
	        Map.Entry<String, Object> pairs = it.next();
	        
	        String Key =  pairs.getKey();
	        Object Value = pairs.getValue();
	        
	        if(Value instanceof Integer){
	        	setValue(Key, (Integer) Value);
	        }else if(Value instanceof Boolean){
	        	setValue(Key, (Boolean) Value);
	        }else if(Value instanceof Double){
	        	setValue(Key, (Double) Value);
	        }else if(Value instanceof Float){
	        	setValue(Key, (Float) Value);
	        }else if(Value instanceof Long){
	        	setValue(Key, (Long) Value);
	        }else if(Value instanceof String){
	        	setValue(Key, (String) Value);
	        }else if(Value instanceof Collection<?>){
	        	setValue(Key, (Collection<String>) Value);
	        }
	    }
	}
	
	/**
	 * Sets the value of a preference
	 * @param name - The preference
	 * @param value - The value
	 */
	public static void setValue(String name, boolean value){
		store.setValue(name, value);
	}
	
	/**
	 * Sets the value of a preference
	 * @param name - The preference
	 * @param value - The value
	 */
	public static void setValue(String name, double value){
		store.setValue(name, value);
	}
	
	/**
	 * Sets the value of a preference
	 * @param name - The preference
	 * @param value - The value
	 */
	public static void setValue(String name, float value){
		store.setValue(name, value);
	}
	
	/**
	 * Sets the value of a preference
	 * @param name - The preference
	 * @param value - The value
	 */
	public static void setValue(String name, int value){
		store.setValue(name, value);
	}
	
	/**
	 * Sets the value of a preference
	 * @param name - The preference
	 * @param value - The value
	 */
	public static void setValue(String name, long value){
		store.setValue(name, value);
	}
	
	/**
	 * Sets the value of a preference
	 * @param name - The preference
	 * @param value - The value
	 */
	public static void setValue(String name, String value){
		store.setValue(name, value);
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
		return store.getString(name);
	}
	
	/**
	 * Gets the value of a preference
	 * @param name - The preference
	 */
	public static boolean getBoolean(String name){
		return store.getBoolean(name);
	}

	/**
	 * Gets the value of a preference
	 * @param name - The preference
	 */
	public static int getInt(String name){
		return store.getInt(name);
	}
	
	/**
	 * Gets the value of a preference
	 * @param name - The preference
	 */
	public static double getDouble(String name){
		return store.getDouble(name);
	}
	
	/**
	 * Gets the value of a preference
	 * @param name - The preference
	 */
	public static float getFloat(String name){
		return store.getFloat(name);
	}

	/**
	 * Gets the value of a preference
	 * @param name - The preference
	 */
	public static long getLong(String name){
		return store.getLong(name);
	}
	
	public static Collection<String> getStrings(String name){
		String storedString = store.getString(name);
		String[] values = storedString.split(STRING_DELIMITER);
		return Arrays.asList(values);
	}
}
