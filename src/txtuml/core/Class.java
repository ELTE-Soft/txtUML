package txtuml.core;

import java.util.Vector;

public class Class {
	public Class(String n) {
		name = n;
        attributes = new Vector<Attribute>();
        methods = new Vector<Method>();
	}
    
    public String getName() {
        return name;
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
        return null;
    }

    public Vector<Method> getMethods() {
        return methods;
    }

    public void addAttribute(Attribute attr) {
        attributes.add(attr);
    }

    public Vector<Attribute> getAttributes() {
        return attributes;
    }
    
    public void setStateMachine(StateMachine stm) {
        stateMachine = stm;
    }

    public StateMachine getStateMachine() {
        return stateMachine;
    }
    
	String name;
	Vector<Attribute> attributes;
	Vector<Method> methods;
	StateMachine stateMachine;
}

