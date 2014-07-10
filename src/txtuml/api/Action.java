package txtuml.api;

import txtuml.importer.Importer;
import txtuml.utils.InstanceCreator;

public class Action implements ModelElement {
	public static <T extends ModelClass> T create(Class<T> classType) {
		return InstanceCreator.createInstance(classType, 1);
	}

	public static void link(Class<? extends Association> assocClass,
    		String leftPhrase,  ModelClass leftObj,
    		String rightPhrase, ModelClass rightObj) {
        if(Importer.link(assocClass, leftPhrase, leftObj, rightPhrase, rightObj)) {
            return;
        }
        Runtime.link(assocClass, leftPhrase, leftObj, rightPhrase, rightObj);
    }

    public static void unLink(Class<? extends Association> assocClass,
    		String leftPhrase,  ModelClass leftObj,
    		String rightPhrase, ModelClass rightObj) {
        if(Importer.unLink(assocClass, leftPhrase, leftObj, rightPhrase, rightObj)) {
            return;
        }
        Runtime.unLink(assocClass, leftPhrase, leftObj, rightPhrase, rightObj);
    }

	@SuppressWarnings("unchecked") // unchecked cast from ModelClass to T
	public static <T extends ModelClass> T selectOne(ModelClass startObj, Class<? extends Association> assocClass, String phrase) {
        if(Importer.instructionImport()) {
            return (T) Importer.selectOne(startObj, assocClass, phrase);
		}
		return (T) Runtime.selectOne(startObj, assocClass, phrase);
	}
	
	public static void send(ModelClass receiverObj, Signal event) {
		if(Importer.send(receiverObj, event)) {
			return;
		}
		Runtime.send(receiverObj, event);
	}

	public static void delete(ModelClass obj) {
    	if(Importer.delete(obj)) {
    		return;
    	}
    	Runtime.delete(obj);
    }

	public static void log(String message) { // user log
		if(Importer.log(message)) {
			return;
		}
		Runtime.log(message);
	}
    	
	public static void logError(String message) { // user log
		if (Importer.logError(message)) {
			return;
		}
		Runtime.logError(message);
	}
	
	static void runtimeLog(String message) { // api log
		if (Importer.runtimeLog(message)) {
			return;
		}
		Runtime.runtimeLog(message);
	}

	static void runtimeFormattedLog(String format, Object... args) { // api log
		/*if (Importer.runtimeLog(message)) {
			return;
		}*/
		Runtime.runtimeFormattedLog(format, args);
	}
	
	static void runtimeErrorLog(String message) { // api log
		if (Importer.runtimeErrorLog(message)) {
			return;
		}
		Runtime.runtimeErrorLog(message);
	}
}