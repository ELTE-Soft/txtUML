package txtuml.core;

import java.util.Vector;

public class CoreModel extends CoreNamedObject {
	public CoreModel(String name) {
        super(name);
        classes = new Vector<CoreClass>();
		associations = new Vector<CoreAssociation>();
		methods = new Vector<CoreMethod>();
		events = new Vector<CoreEvent>();
	}

	public void addClassName(String className) {
		classes.add(new CoreClass(className));
	}

    public CoreClass getClass(String className) {
        for(CoreClass c : classes) {
            if(c.getName().equals(className)) {
                return c;
            }
        }
        return null;
    }
    
    public Vector<CoreClass> getClasses() {
        return classes;
    }

    public void addAssociation(CoreAssociation a) {
        associations.add(a);
    }
    
    public CoreAssociation getAssociation(String assocName) {
        for(CoreAssociation a : associations) {
            if(a.getName().equals(assocName)) {
                return a;
            }
        }
        return null;
    }
    
    public Vector<CoreAssociation> getAssociations() {
        return associations;
    }
    
    public void addMethod(CoreMethod m) {
        methods.add(m);
    }

    public void addEvent(CoreEvent e) {
        events.add(e);
    }

    public CoreEvent getEvent(String eventName) {
        for(CoreEvent e : events) {
            if(e.getName().equals(eventName)) {
                return e;
            }
        }
        return null;
    }

    public Vector<CoreEvent> getEvents() {
    	return events;
    }
    
    public void addMethodName(String methodName) {
        methods.add(new CoreMethod(methodName));
    }
    
    public CoreMethod getMethod(String methodName) {
        for(CoreMethod m : methods) {
            if(m.getName().equals(methodName)) {
                return m;
            }
        }
        System.out.println("Method " + methodName + " not found!");
        return null;
    }

	public Vector<CoreMethod> getMethods() {
		return methods;
	}
    
    private Vector<CoreClass> classes;
    private Vector<CoreAssociation> associations;
    private Vector<CoreMethod> methods;
    private Vector<CoreEvent> events;
}
