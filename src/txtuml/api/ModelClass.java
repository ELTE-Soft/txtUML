package txtuml.api;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.Thread;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import txtuml.utils.InstanceCreator;

public class ModelClass {

	protected ModelClass() {
		identifier = "inst_" + hashCode();
		self = null;
		currentState = null;
		thread = null;
	}

	protected final ModelObject<? extends ModelClass> self() {
		if (self == null) {
			Action.runtimeErrorLog("self() method used on ModelClass object having no ModelObject container");
		}
		return self;
	}

	public final String getIdentifier() {
		return identifier;
	}

	void send(Class<? extends Event> eventClass) {
		if (thread != null) {
			thread.send(eventClass);
		}
	}

	void startThread() {
		currentState = getInitialState();
		if (currentState != null) {
			thread = new ModelClassThread(this);
		}
	}

	void finishThread() {
		if (thread != null) {
			thread.interrupt();
			thread = null;
		}
	}

	void setSelf(ModelObject<? extends ModelClass> self) {
		this.self = self;
	}

	private void processEvent(Class<? extends Event> eventClass) {
		Event event = InstanceCreator.createInstance(eventClass, 3);
		Method[] methods = getClass().getDeclaredMethods();
		for (Method transition : methods) {
			Annotation annot = transition.getAnnotation(Transition.class);
			Class<?>[] paramTypes = transition.getParameterTypes();
			if (currentState != null
					&& annot != null
					&& ((Transition) annot).from().equals(
							currentState.getName())
					&& paramTypes.length == 1
					&& paramTypes[0].equals(eventClass)) {
				transition.setAccessible(true);
				try {
					transition.invoke(this, event);
				} catch (IllegalAccessException | IllegalArgumentException e) {
					Action.runtimeErrorLog("Error: " + this.getClass().getName() + "."	+ transition.getName() + " transition cannot be called due to: " + e.getMessage());
					return;
				} catch (InvocationTargetException e) {
					Action.runtimeErrorLog("Exception: " + this.getClass().getName() + "." + transition.getName() + " threw exception " + e.getTargetException().toString());
					return;
				}
				for (Method state : methods) {
					if (state.getAnnotation(State.class) != null
							&& ((Transition) annot).to().equals(
									state.getName())) {
						state.setAccessible(true);
						try {
							state.invoke(this);
						} catch (IllegalAccessException	| IllegalArgumentException e) {
							Action.runtimeErrorLog("Error: " + this.getClass().getName() + "." + state.getName() + " state cannot be called due to: " + e.getMessage());
							return;
						} catch (InvocationTargetException e) {
							Action.runtimeErrorLog("Exception: " + this.getClass().getName() + "." + state.getName() + " threw exception " + e.getTargetException().toString());
							return;
						}
						currentState = state;
						break;
					}
				}
				break;
			}
		}
	}

	private class ModelClassThread extends Thread {
		ModelClassThread(ModelClass p) {
			parent = p;
			mailbox = new LinkedBlockingQueue<Class<? extends Event>>();
			start();
		}

		void send(Class<? extends Event> eventClass) {
			try {
				mailbox.put(eventClass);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void run() {
			// TODO stop loop after everything is finished
			try {
				while (true) {
					Class<? extends Event> eventClass = mailbox.take();
					parent.processEvent(eventClass);
				}
			} catch (InterruptedException e) {
				// do nothing (finish thread)
			}
		}

		private LinkedBlockingQueue<Class<? extends Event>> mailbox;
		private ModelClass parent;
	}

	private Method getInitialState() {
		synchronized(initialStates) {
	        if (initialStates.containsKey(getClass())) {
	    		return initialStates.get(getClass());
	        }        
			for (Method method : getClass().getDeclaredMethods()) {
				if (method.isAnnotationPresent(Initial.class)) {
			        initialStates.put(getClass(), method);
			        return method;
				} 
			}
	        initialStates.put(getClass(), null);
	        return null;
		}
	}

	private final String identifier;
	private Method currentState;
	private ModelObject<? extends ModelClass> self;
	private ModelClassThread thread;
	private static Map<Class<? extends ModelClass>, Method> initialStates = new HashMap<>();
}