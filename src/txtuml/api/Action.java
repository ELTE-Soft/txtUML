package txtuml.api;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import txtuml.importer.Importer;
import txtuml.runtime.Runtime;
import txtuml.utils.InstanceCreator;

public class Action {

	public static void assign(ModelType right, ModelType left) {
		// TODO
	}

	public static void assign(ModelType right, String left) {
		// TODO		
	}
	
    public static void link(Class assocClass, String leftPhrase, ModelClass leftObj, String rightPhrase, ModelClass rightObj) {
        if(Importer.link(assocClass, leftPhrase, leftObj, rightPhrase, rightObj)) {
            return;
        }

		Association assoc = null;
        try {
			assoc = (Association)InstanceCreator.createInstance(assocClass,3);
			Field left = assocClass.getDeclaredField(leftPhrase);
			left.setAccessible(true);
			left.set(assoc,leftObj);
			Field right = assoc.getClass().getDeclaredField(rightPhrase);
			right.setAccessible(true);
			right.set(assoc,rightObj);
        } catch(Exception e) {
            e.printStackTrace();
        }
        Runtime.getInstance().addAssociation(assoc);
    }

    public static void call(ModelClass obj, String methodName) {
        if(Importer.call(obj,methodName)) {
            return;
        }
        
        try {
            Method m = obj.getClass().getDeclaredMethod(methodName);
            if(m != null) {
				m.setAccessible(true);
                m.invoke(obj);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

	public static Object selectOne(ModelClass start, Association assoc, String phrase) {
		ModelClass importResult = Importer.selectOne(start,assoc,phrase);
        if(importResult != null) {
            return importResult;
		}
		
		return Runtime.getInstance().selectOne(start,assoc.getClass(),phrase);
	}
	
	public static void send(Object signal, ModelClass receiver) {
		if(Importer.send(signal, receiver)) {
			return;
		}
		
		Runtime.getInstance().send(signal, receiver);
	}
    
	public static void log(String message) {
		if(Importer.log(message)) {
			return;
		}
		
		Runtime.getInstance().log(message);
	}
    
    public static void callExternal(Class c, String methodName) {
		if(Importer.callExternal(c,methodName)) {
			return;
		}
		
		Runtime.getInstance().callExternal(c,methodName);        
    }
}
