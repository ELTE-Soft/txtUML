package hu.elte.txtuml.export.papyrus.preferences;

import hu.elte.txtuml.export.papyrus.Activator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jface.preference.IPreferenceStore;

public class PreferencesManager{
	
	
	
	private IPreferenceStore store;
	private Map<String, Object> fieldsWithDefaultValues;
	
	public static String CLASS_DIAGRAM_PREF = "Class Diagram";
	public static String ACTIVITY_DIAGRAM_PREF = "Activity Diagram";
	public static String STATEMACHINE_DIAGRAM_PREF = "StateMachine Diagram";
	
	public static String CLASS_DIAGRAM_CONSTRAINT_PREF = "Class Diagram Constraint";
	public static String CLASS_DIAGRAM_SIGNAL_PREF = "Class Diagram Signal";
	public static String CLASS_DIAGRAM_COMMENT_PREF = "Class Diagram Comment";
	
	public static String STATEMACHINE_DIAGRAM_CONSTRAINT_PREF = "StateMachine Diagram Constraint";
	public static String STATEMACHINE_DIAGRAM_COMMENT_PREF = "StateMachine Diagram Comment";
	
	public static String TXTUML_VISUALIZE_TXTUML_MODEL = "txtUML Visualize txtUML Model";
	public static String TXTUML_VISUALIZE_TXTUML_PROJECT = "txtUML Visualize txtUML Project";
	public static String TXTUML_VISUALIZE_DESTINATION_FOLDER = "txtUML Visualize Destination Folder";
	
	public PreferencesManager() {
		store = Activator.getDefault().getPreferenceStore();
		fieldsWithDefaultValues = new HashMap<String, Object>();
		
		fieldsWithDefaultValues.put(CLASS_DIAGRAM_PREF, true);
		fieldsWithDefaultValues.put(ACTIVITY_DIAGRAM_PREF, false);
		fieldsWithDefaultValues.put(STATEMACHINE_DIAGRAM_PREF, true);
		
		fieldsWithDefaultValues.put(CLASS_DIAGRAM_CONSTRAINT_PREF, false);
		fieldsWithDefaultValues.put(CLASS_DIAGRAM_SIGNAL_PREF, false);
		fieldsWithDefaultValues.put(CLASS_DIAGRAM_COMMENT_PREF, true);
	    
		fieldsWithDefaultValues.put(STATEMACHINE_DIAGRAM_CONSTRAINT_PREF, false);
		fieldsWithDefaultValues.put(STATEMACHINE_DIAGRAM_COMMENT_PREF, true);
		
		fieldsWithDefaultValues.put(TXTUML_VISUALIZE_TXTUML_MODEL, "");
		fieldsWithDefaultValues.put(TXTUML_VISUALIZE_DESTINATION_FOLDER, "gen");
	}
	
	public void setDefaults(){
		Iterator<Map.Entry<String, Object>> it = fieldsWithDefaultValues.entrySet().iterator();
		while (it.hasNext()) {
	        Map.Entry<String, Object> pairs = (Map.Entry<String, Object>) it.next();
	        
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
	
	public void resetDefaults(){
		Iterator<Entry<String, Object>> it = fieldsWithDefaultValues.entrySet().iterator();
		while (it.hasNext()) {
	        Map.Entry<String, Object> pairs = (Map.Entry<String, Object>) it.next();
	        store.setToDefault(pairs.getKey());
	    }
	}
	
	
	public void setValues(Map<String, Object> mp){
		Iterator<Map.Entry<String, Object>> it = mp.entrySet().iterator();
		
		while (it.hasNext()) {
	        Map.Entry<String, Object> pairs = (Map.Entry<String, Object>) it.next();
	        
	        String Key =  pairs.getKey();
	        Object Value = pairs.getValue();
	        
	        if(Value instanceof Integer){
	        	this.setValue(Key, (Integer) Value);
	        }else if(Value instanceof Boolean){
	        	this.setValue(Key, (Boolean) Value);
	        }else if(Value instanceof Double){
	        	this.setValue(Key, (Double) Value);
	        }else if(Value instanceof Float){
	        	this.setValue(Key, (Float) Value);
	        }else if(Value instanceof Long){
	        	this.setValue(Key, (Long) Value);
	        }else if(Value instanceof String){
	        	this.setValue(Key, (String) Value);
	        }
	    }
	}
	
	public void setValue(String name, boolean value){
		store.setValue(name, value);
	}
	
	public void setValue(String name, double value){
		store.setValue(name, value);
	}
	
	public void setValue(String name, float value){
		store.setValue(name, value);
	}
	
	public void setValue(String name, int value){
		store.setValue(name, value);
	}
	
	public void setValue(String name, long value){
		store.setValue(name, value);
	}
	
	public void setValue(String name, String value){
		store.setValue(name, value);
	}
	
	public String getString(String name){
		return store.getString(name);
	}
	
	public boolean getBoolean(String name){
		return store.getBoolean(name);
	}

	public int getInt(String name){
		return store.getInt(name);
	}
	
	public double getDouble(String name){
		return store.getDouble(name);
	}
	
	public float getFloat(String name){
		return store.getFloat(name);
	}

	public long getLong(String name){
		return store.getLong(name);
	}
}
