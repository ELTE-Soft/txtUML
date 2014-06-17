package txtuml.core;

public class CreateInstruction extends Instruction {
    public CreateInstruction(Class c, Instance ref) {
        type = c;
        reference = ref;
    }
    
    public Class getType() {
		return type;
	}

    public Instance getReference() {
		return reference;
	}
    
    Class type;
    Instance reference;
}
