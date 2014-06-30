package txtuml.core.instructions;

import txtuml.core.CoreAssociation;
import txtuml.core.CoreInstance;

public class SelectOneInstruction extends Instruction {
    public SelectOneInstruction(CoreInstance st, CoreAssociation assoc, String ph, CoreInstance res) {
        start = st;
        association = assoc;
        phrase = ph;
        result = res;
    }
    
    public CoreInstance getStart() {
    	return start;
    }
    
    public CoreAssociation getAssociation() {
    	return association;
    }
    
    public String getPhrase() {
    	return phrase;
    }
    
    public CoreInstance getResult() {
    	return result;
    }
    
    private CoreInstance start;
    private CoreAssociation association;
    private String phrase;
    private CoreInstance result;
}
