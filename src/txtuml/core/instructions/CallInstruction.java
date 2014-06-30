package txtuml.core.instructions;

import txtuml.core.CoreMethod;
import txtuml.core.CoreInstance;

public class CallInstruction extends Instruction {
    public CallInstruction(CoreInstance obj, CoreMethod m) {
        object = obj;
        method = m;
    }
    
    public CoreInstance getObject() {
    	return object;
    }

    public CoreMethod getMethod() {
    	return method;
    }
    
    private CoreInstance object;
    private CoreMethod method;
}
