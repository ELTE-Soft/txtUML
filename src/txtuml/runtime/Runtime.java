package txtuml.runtime;

import java.util.Vector;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import txtuml.api.Association;
import txtuml.api.ModelClass;
import txtuml.api.State;
import txtuml.api.Transition;
import txtuml.utils.InstanceCreator;

public class Runtime {
	public static Runtime getInstance() {
		if(instance == null) {
			instance = new Runtime();
		}
		return instance;
	}
	public void addAssociation(Association assoc) {
		associations.add(assoc);
	}
	public Object selectOne(ModelClass start, Class assoc, String phrase) {
		for(Association a : associations) {
			if(a.getClass() == assoc) {
				Field startField = null, phraseField = null;
				Field[] fs = assoc.getDeclaredFields();
				for(Field f : fs) {
					String fName = f.getName();
					if(fName == phrase) {
						phraseField = f;
					} else if(fName != "this$0") {
						startField = f;
					}
				}
				if(startField != null && phraseField != null) {
					try {
						startField.setAccessible(true);
						if(startField.get(a) == start) {
							phraseField.setAccessible(true);
							return phraseField.get(a);
						}
					} catch(IllegalAccessException e) {
					}
				}
			}
		}
		return null;
	}

	public void send(Object event, ModelClass receiver) {
		receiver.send(event);
	}

	public void log(String message) {
        System.out.println(message);
    }

	public void callExternal(Class c, String methodName) {
        try {
            Method m = c.getMethod(methodName);
            if(m != null) {
                    m.invoke(null); // Works for static methods only!
            } else {
                System.out.println("Action.callExternal: " +  c.getSimpleName() + "." + methodName + " not found.");
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

	Runtime() {
		associations = new Vector<Association>();
	}
	static Runtime instance;
	Vector<Association> associations;
}
