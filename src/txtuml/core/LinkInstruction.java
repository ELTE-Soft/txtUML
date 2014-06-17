package txtuml.core;

public class LinkInstruction extends Instruction {
    public LinkInstruction(Instance left, Instance right, Association assoc) {
        leftInstance = left;
        rightInstance = right;
        association = assoc;
    }
    
    public Instance getLeftInstance() {
    	return leftInstance;
    }

    public Instance getRightInstance() {
    	return rightInstance;
    }

    public Association getAssociation() {
    	return association;
    }

    Instance leftInstance;
    Instance rightInstance;
    Association association;
}
