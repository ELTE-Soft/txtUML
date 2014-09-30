package txtuml.api;

import java.util.Vector;
import java.io.PrintStream;
import java.lang.reflect.Field;

import txtuml.api.Association;
import txtuml.api.ModelClass;
import txtuml.utils.InstanceCreator;

public final class Runtime implements ModelElement {
	private Runtime() {}
	
	public static final class Settings implements ModelElement {
		private Settings() {}

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
		public static void setRuntimeLog(boolean newValue) {
			runtimeLog = newValue;
		}
		public static void setSimulationTimeMultiplier(long newMultiplier) {
			if (canChangeSimulationTimeMultiplier) {
				simulationTimeMultiplier = newMultiplier;
			} else {
				Action.runtimeErrorLog("Error: Simulation time multiplier can only be changed before any time-related event takes place in the model simulation");
			}
		}
		public static long getSimulationTimeMultiplier() {
			return simulationTimeMultiplier;
		}
		public static void lockSimulationTimeMultiplier() {
			canChangeSimulationTimeMultiplier = false;
		}
		static boolean runtimeLog() {
			return runtimeLog;
		}
		private static PrintStream userOutStream = System.out;
		private static PrintStream userErrorStream = System.err;
		private static PrintStream runtimeOutStream = System.out;
		private static PrintStream runtimeErrorStream = System.err;
		private static long simulationTimeMultiplier = 1;
		private static boolean canChangeSimulationTimeMultiplier = true;
		private static boolean runtimeLog = false;
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
		if (obj == null) {
			return;
		}
		synchronized(obj) {
			for (Association assoc : associations) {
				Field[] fs = assoc.getClass().getDeclaredFields();
				for(Field f : fs) {
					f.setAccessible(true);
					try {
						if (f.get(assoc) == obj) {
							Action.runtimeErrorLog("Error: object is not allowed to be deleted because of existing associations.");
						}
					} catch (IllegalArgumentException | IllegalAccessException e) {
						// this should not be possible to happen
						e.printStackTrace();
					}
				}
			}
		    obj.finishThread();
		    // TODO how to delete object?
		}
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
        synchronized(Settings.userOutStream) {
        	Settings.userOutStream.println(message);
        }
    }

	static void logError(String message) { // user log
        synchronized(Settings.userErrorStream) {
        	Settings.userErrorStream.println(message);
        }
	}
	
	static void runtimeLog(String message) { // api log
        synchronized(Settings.runtimeOutStream) {
        	Settings.runtimeOutStream.println(message);
        }
	}

	static void runtimeFormattedLog(String format, Object... args) { // api log
        synchronized(Settings.runtimeOutStream) {
			Settings.runtimeOutStream.format(format, args);
        }
	}
	
	static void runtimeErrorLog(String message) { // api log
        synchronized(Settings.runtimeErrorStream) {
        	Settings.runtimeErrorStream.println(message);
        }
	}
	
	static void send(ModelClass receiverObj, Signal event) {
		receiverObj.send(event);
	}
	
	private static Vector<Association> associations = new Vector<Association>();
}
