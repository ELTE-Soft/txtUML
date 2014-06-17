package txtuml.core;

public class SelectOneInstruction extends Instruction {
    public SelectOneInstruction(Instance st, Association assoc, String ph, Instance res) {
        start = st;
        association = assoc;
        phrase = ph;
        result = res;
    }
    
    public Instance getStart() {
    	return start;
    }
    
    public Association getAssociation() {
    	return association;
    }
    
    public String getPhrase() {
    	return phrase;
    }
    
    public Instance getResult() {
    	return result;
    }
    
    Instance start;
    Association association;
    String phrase;
    Instance result;
}
