package txtuml.core;

import java.util.Vector;

import txtuml.core.instructions.Instruction;

public class CoreMethod extends CoreNamedObject {
    public CoreMethod(String name) {
        super(name);
        instructions = new Vector<Instruction>();
    }
    
    public void addInstruction(Instruction i) {
        instructions.add(i);
    }

	public Vector<Instruction> getInstructions() {
		return instructions;
	}

    public void setSelf(CoreInstance i) {
        self = i;
    }
    
    public CoreInstance getSelf() {
    	return self;
    }

    private CoreInstance self;
    private Vector<Instruction> instructions;
}
