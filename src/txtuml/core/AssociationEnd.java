package txtuml.core;

public class AssociationEnd {
    public AssociationEnd(Class c, String p, Multiplicity m) {
        participant = c;
        phrase = p;
        multiplicity = m;
    }
    
    public String getPhrase() {
        return phrase;
    }
    
    public Multiplicity getMultiplicity() {
        return multiplicity;
    }
    
    public String getLowerBound() {
        switch(multiplicity) {
            case One: return "1";
            case Many: return "0";
        }
        return "0";
    }

    public String getUpperBound() {
        switch(multiplicity) {
            case One: return "1";
            case Many: return "-1";
        }
        return "-1";
    }
    
    public Class getParticipant() {
        return participant;
    }
    
	Class participant;
	String phrase;
    Multiplicity multiplicity;
}
