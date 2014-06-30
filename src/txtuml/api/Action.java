package txtuml.api;

import txtuml.importer.Importer;
import txtuml.utils.InstanceCreator;

public class Action {
	public static <T extends ModelClass> ModelObject<T> create(Class<T> classType) {
		T obj = InstanceCreator.createInstance(classType, 1);
		if (!Importer.createInstance(obj)) {
			obj.startThread();
		}
		return new ModelObject<T>(obj);
	}
	
	public static void assign(ModelType left, ModelType right) {
		// TODO Action.assign
	}

	public static void assign(ModelType left, String right) {
		// TODO Action.assign
	}
	
    public static void link(Class<? extends Association> assocClass,
    		String leftPhrase,  ModelObject<?> leftObject,
    		String rightPhrase, ModelObject<?> rightObject) {
    	ModelClass leftObj = leftObject.getObject(), rightObj = rightObject.getObject();
        if(Importer.link(assocClass, leftPhrase, leftObj, rightPhrase, rightObj)) {
            return;
        }
        Runtime.link(assocClass, leftPhrase, leftObj, rightPhrase, rightObj);
    }

    public static void unLink(Class<? extends Association> assocClass,
    		String leftPhrase,  ModelObject<?> leftObject,
    		String rightPhrase, ModelObject<?> rightObject) {
    	ModelClass leftObj = leftObject.getObject(), rightObj = rightObject.getObject();
    	// no validation (Association class should be already validated)
        if(Importer.unLink(assocClass, leftPhrase, leftObj, rightPhrase, rightObj)) {
            return;
        }
        Runtime.unLink(assocClass, leftPhrase, leftObj, rightPhrase, rightObj);
    }
    
    public static void call(ModelObject<?> object, String methodName) {
        ModelClass obj = object.getObject();
    	if(Importer.call(obj,methodName)) {
            return;
        }        
    	Runtime.call(obj, methodName);
    }

	@SuppressWarnings("unchecked") // unchecked cast from ModelClass to T (runtime check)
	public static <T extends ModelClass> ModelObject<T> selectOne(ModelObject<?> startObject, Class<? extends Association> assocClass, String phrase) {
		ModelClass startObj = startObject.getObject();
		ModelClass importResult	= Importer.selectOne(startObj, assocClass, phrase);
        if(importResult != null) {
            return (ModelObject<T>) ((T)importResult).self();
		}
		return (ModelObject<T>) ((T)Runtime.selectOne(startObj, assocClass, phrase)).self();
	}
	
	public static void send(ModelObject<?> receiverObject, Class<? extends Event> event) {
		ModelClass receiverObj = receiverObject.getObject();
		if(Importer.send(receiverObj, event)) {
			return;
		}
		Runtime.send(receiverObj, event);
	}

	public static void delete(ModelObject<?> object) {
		ModelClass obj = object.getObject();		
    	if(Importer.delete(object.getObject())) {
    		return;
    	}
    	Runtime.delete(obj);
    }
    	
    public static void callExternal(Class<?> c, String methodName) {
		if(Importer.callExternal(c, methodName)) {
			return;
		}
		Runtime.callExternal(c,methodName);        
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
	
	static void runtimeErrorLog(String message) { // api log
		if (Importer.runtimeErrorLog(message)) {
			return;
		}
		Runtime.runtimeErrorLog(message);
	}
}