package txtuml.core;

public class CallInstruction extends Instruction {
    public CallInstruction(Instance obj, Method m) {
        object = obj;
        method = m;
    }
    
    public Instance getObject() {
    	return object;
    }

    public Method getMethod() {
    	return method;
    }
    
    Instance object;
    Method method;
}
