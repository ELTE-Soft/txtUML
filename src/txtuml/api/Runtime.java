package txtuml.api;

import java.util.Vector;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import txtuml.api.Association;
import txtuml.api.ModelClass;
import txtuml.utils.InstanceCreator;

public final class Runtime {
	
	public static final class Settings {
		public static void setUserOutStream(PrintStream userOutStream) {
			Settings.userOutStream = userOutStream;
		}
		public static void setUserErrorStream(PrintStream userErrorStream) {
			Settings.userErrorStream = userErrorStream;
		}
		public static void setRuntimeOutStream(PrintStream runtimeOutStream) {
			Settings.runtimeOutStream = runtimeOutStream;
		}
		public static void setRuntimeErrorStream(PrintStream runtimeErrorStream) {
			Settings.runtimeErrorStream = runtimeErrorStream;
		}
		private static PrintStream userOutStream = System.out;
		private static PrintStream userErrorStream = System.err;
		private static PrintStream runtimeOutStream = System.out;
		private static PrintStream runtimeErrorStream = System.err;		
	}
	
	static void link(Class<? extends Association> assocClass, String leftPhrase,  ModelClass leftObj, String rightPhrase, ModelClass rightObj) {
        // TODO check multiplicity
		Association assoc = null;
        try {
			assoc = (Association)InstanceCreator.createInstance(assocClass,3);
			Field left = assocClass.getDeclaredField(leftPhrase);
			left.setAccessible(true);
			left.set(assoc,leftObj);
			Field right = assocClass.getDeclaredField(rightPhrase);
			right.setAccessible(true);
			right.set(assoc,rightObj);
        } catch (Exception e) {
        	// TODO exception handling
        	e.printStackTrace();
		}
		associations.add(assoc);
	}

	static void unLink(Class<? extends Association> assocClass, String leftPhrase,  ModelClass leftObj, String rightPhrase, ModelClass rightObj) {
		for (Association a : associations) {
	        try {
				if (a.getClass().equals(assocClass)) {
		        	Field left = assocClass.getDeclaredField(leftPhrase);
					left.setAccessible(true);
					Field right = assocClass.getDeclaredField(rightPhrase);
					right.setAccessible(true);
					if (left.get(a).equals(leftObj) && right.get(a).equals(rightObj)) {
						associations.remove(a);
						break;
					}
				}
	        } catch (Exception e) {
	        	// TODO exception handling
	        	e.printStackTrace();
			}
		}
	}
	
	static void delete(ModelClass obj) {
		if (obj == null) return;
		for (Association assoc : associations) {
			Field[] fs = assoc.getClass().getDeclaredFields();
			for(Field f : fs) {
				f.setAccessible(true);
				try {
					if (f.get(assoc) == obj) {
						Action.runtimeErrorLog("Error: object is not allowed to be deleted because of existing associations.");
					}
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
					// this should not be possible to happen
				}
			}
		}
	    obj.finishThread();
	    obj.self().deleteObject();
	}
	
	static Object selectOne(ModelClass start, Class<? extends Association> assocClass, String phrase) {
		Field startField = null, phraseField = null;
		Field[] fs = assocClass.getDeclaredFields();
		for(Field f : fs) {
			String fName = f.getName();
			if(fName == phrase) {
				phraseField = f;
			} else if(fName != "this$0") {
				startField = f;
			}
		}
		if(startField != null && phraseField != null) {
			
			for(Association a : associations) {
				if(a.getClass() == assocClass) {
					try {
						startField.setAccessible(true);
						if(startField.get(a) == start) {
							phraseField.setAccessible(true);
							return phraseField.get(a);
						}
					} catch(IllegalArgumentException | IllegalAccessException e) {
						// TODO exception handling
					}
				}
			}
		}
		return null;
	}
	
	static void log(String message) { // user log
        Settings.userOutStream.println(message);
    }

	static void logError(String message) { // user log
        Settings.userErrorStream.println(message);
	}
	
	static void runtimeLog(String message) { // api log
        Settings.runtimeOutStream.println(message);
	}
	
	static void runtimeErrorLog(String message) { // api log
        Settings.runtimeErrorStream.println(message);
	}	
	
	static void call(ModelClass obj, String methodName) {
	    try {
	        Method m = obj.getClass().getDeclaredMethod(methodName);
			m.setAccessible(true);
	        m.invoke(obj);
	    } catch (NoSuchMethodException e) {
	    	Action.runtimeErrorLog("Error:" + obj.getClass().getName() + "." + methodName + " not found");
		} catch (IllegalAccessException | IllegalArgumentException e) {
			Action.runtimeErrorLog("Error:" + obj.getClass().getName() + "." + methodName + " cannot be called due to: " + e.getMessage());
		} catch (InvocationTargetException e) {
			Action.runtimeErrorLog("Exception:" + obj.getClass().getName() + "." + methodName + " threw " + e.getTargetException().toString());
		}
	}
	
	static void callExternal(Class<?> c, String methodName) {
        try {
            Method m = c.getMethod(methodName);
            m.invoke(null); // TODO Works for static methods only
	    } catch (NoSuchMethodException e) {
	    	Action.runtimeErrorLog("Error:" + c.getName() + "." + methodName + " not found");
		} catch (IllegalAccessException | IllegalArgumentException e) {
			Action.runtimeErrorLog("Error:" + c.getName() + "." + methodName + " cannot be called due to: " + e.getMessage());
		} catch (InvocationTargetException e) {
			Action.runtimeErrorLog("Exception:" + c.getName() + "." + methodName + " threw " + e.getTargetException().toString());
		}
    }
	
	static void send(ModelClass receiverObj, Class<? extends Event> event) {
		receiverObj.send(event);
	}
	
	private static Vector<Association> associations = new Vector<Association>();
}
