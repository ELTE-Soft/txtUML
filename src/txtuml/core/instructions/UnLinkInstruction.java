package txtuml.core.instructions;

import txtuml.core.CoreAssociation;
import txtuml.core.CoreInstance;

public class UnLinkInstruction extends LinkInstruction {
    public UnLinkInstruction(CoreInstance left, CoreInstance right, CoreAssociation assoc) {
        super(left, right, assoc);
    }	
}
