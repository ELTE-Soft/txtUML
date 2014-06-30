package txtuml.core.instructions;

import txtuml.core.CoreClass;
import txtuml.core.CoreInstance;

public class CreateInstruction extends Instruction {
    public CreateInstruction(CoreClass c, CoreInstance ref) {
        type = c;
        reference = ref;
    }
    
    public CoreClass getType() {
		return type;
	}

    public CoreInstance getReference() {
		return reference;
	}
    
    private CoreClass type;
    private CoreInstance reference;
}
