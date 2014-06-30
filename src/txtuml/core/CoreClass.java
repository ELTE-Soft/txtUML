package txtuml.core;

import java.util.Vector;

public class CoreClass extends CoreNamedObject {
	public CoreClass(String name) {
		super(name);
		attributes = new Vector<CoreAttribute>();
        methods = new Vector<CoreMethod>();
	}
    
    public void addMethod(String methodName) {
        methods.add(new CoreMethod(methodName));
    }

    public CoreMethod getMethod(String methodName) {
        for(CoreMethod m : methods) {
            if(m.getName().equals(methodName)) {
                return m;
            }
        }
        return null;
    }

    public Vector<CoreMethod> getMethods() {
        return methods;
    }

    public void addAttribute(CoreAttribute attr) {
        attributes.add(attr);
    }

    public Vector<CoreAttribute> getAttributes() {
        return attributes;
    }
    
    public void setStateMachine(CoreStateMachine stm) {
        stateMachine = stm;
    }

    public CoreStateMachine getStateMachine() {
        return stateMachine;
    }
    
	private Vector<CoreAttribute> attributes;
	private Vector<CoreMethod> methods;
	private CoreStateMachine stateMachine;
}

