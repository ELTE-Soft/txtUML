package txtuml.core;

import java.util.Vector;

public class Method {
    public Method(String n) {
        name = n;
        instructions = new Vector<Instruction>();
    }
    
    public String getName() {
        return name;
    }
    
    public void addInstruction(Instruction i) {
        instructions.add(i);
    }

	public Vector<Instruction> getInstructions() {
		return instructions;
	}

    public void setSelf(Instance i) {
        self = i;
    }
    
    public Instance getSelf() {
    	return self;
    }

    String name;
    Instance self;
    Vector<Instruction> instructions;
}
