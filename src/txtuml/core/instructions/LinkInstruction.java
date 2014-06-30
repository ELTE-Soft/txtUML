package txtuml.core.instructions;

import txtuml.core.CoreAssociation;
import txtuml.core.CoreInstance;

public class LinkInstruction extends Instruction {
    public LinkInstruction(CoreInstance left, CoreInstance right, CoreAssociation assoc) {
        leftInstance = left;
        rightInstance = right;
        association = assoc;
    }
    
    public CoreInstance getLeftInstance() {
    	return leftInstance;
    }

    public CoreInstance getRightInstance() {
    	return rightInstance;
    }

    public CoreAssociation getAssociation() {
    	return association;
    }

    private CoreInstance leftInstance;
    private CoreInstance rightInstance;
    private CoreAssociation association;
}
