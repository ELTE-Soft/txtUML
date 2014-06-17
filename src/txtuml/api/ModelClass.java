package txtuml.api;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.Thread;
import java.util.concurrent.LinkedBlockingQueue;
import txtuml.importer.Importer;

public class ModelClass {
    public ModelClass() {
        identifier = "inst_" + hashCode();
        Importer.createInstance(this);
        currentState = null;
        thread = null;
        if(!Importer.instructionImport()) {
			Class[] states = getClass().getDeclaredClasses();
			for(Method state : getClass().getDeclaredMethods()) {
				if(state.getAnnotation(Initial.class) != null) {
					currentState = state;
					thread = new ModelClassThread(this);
					break;
				}
			}
        }
    }

    public final String getIdentifier() {
        return identifier;
    }
    
    public void send(Object o) {
    	if(thread != null) {
    		thread.send(o);
    	}
    }
    
    public void processEvent(Object event) {
    	Method[] methods = getClass().getDeclaredMethods();
  	    try {
	      for(Method transition : methods) {
	          Annotation annot = transition.getAnnotation(Transition.class);
	          Class[] paramTypes = transition.getParameterTypes();
	          if(currentState != null && annot != null
	             && ((Transition)annot).from().equals(currentState.getName())
	             && paramTypes.length == 1) {
	              transition.setAccessible(true);
	              transition.invoke(this,event);
	              for(Method state : methods) {
	                  if(state.getAnnotation(State.class) != null
	                     && ((Transition)annot).to().equals(state.getName())) {
	                      state.setAccessible(true);
	                      state.invoke(this);
	                      currentState = state;
	                      break;
	                  }
	              }
	              break;
	          }
	      }
	  } catch(Exception e) {
	      e.printStackTrace();
	  }

    }
    
    String identifier;
    public Method currentState;
    ModelClassThread thread;
}

class ModelClassThread extends Thread {
    public ModelClassThread(ModelClass p) {
    	parent = p;
    	mailbox = new LinkedBlockingQueue<Object>();
        start();
    }

    public void send(Object o) {
        try {
            mailbox.put(o);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while(true) {
            try {
                Object event = mailbox.take();
                parent.processEvent(event);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
	
    LinkedBlockingQueue<Object> mailbox;
    ModelClass parent;
}