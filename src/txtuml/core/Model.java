package txtuml.core;

import java.util.Vector;

public class Model {
	public Model(String n) {
        name = n;
        classes = new Vector<Class>();
		associations = new Vector<Association>();
		methods = new Vector<Method>();
		events = new Vector<Event>();
	}

    public String getName() {
        return name;
    }

	public void addClassName(String className) {
		classes.add(new Class(className));
	}

    public Class getClass(String className) {
        for(Class c : classes) {
            if(c.getName().equals(className)) {
                return c;
            }
        }
        return null;
    }
    
    public Vector<Class> getClasses() {
        return classes;
    }

    public void addAssociation(Association a) {
        associations.add(a);
    }
    
    public Association getAssociation(String assocName) {
        for(Association a : associations) {
            if(a.getName().equals(assocName)) {
                return a;
            }
        }
        return null;
    }
    
    public Vector<Association> getAssociations() {
        return associations;
    }
    
    public void addMethod(Method m) {
        methods.add(m);
    }

    public void addEvent(Event e) {
        events.add(e);
    }

    public Event getEvent(String eventName) {
        for(Event e : events) {
            if(e.getName().equals(eventName)) {
                return e;
            }
        }
        return null;
    }

    public Vector<Event> getEvents() {
    	return events;
    }
    
    public void addMethodName(String methodName) {
        methods.add(new Method(methodName));
    }
    
    public Method getMethod(String methodName) {
        for(Method m : methods) {
            if(m.getName().equals(methodName)) {
                return m;
            }
        }
        System.out.println("Method " + methodName + " not found!");
        return null;
    }

	public Vector<Method> getMethods() {
		return methods;
	}
    
    String name;
    Vector<Class> classes;
	Vector<Association> associations;
    Vector<Method> methods;
    Vector<Event> events;
}
