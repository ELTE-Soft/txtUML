package txtuml.core;

public class CoreAssociationEnd {
    public CoreAssociationEnd(CoreClass c, String p, CoreMultiplicity m) {
        participant = c;
        phrase = p;
        multiplicity = m;
    }
    
    public String getPhrase() {
        return phrase;
    }
    
    public CoreMultiplicity getMultiplicity() {
        return multiplicity;
    }
    
    public String getLowerBound() {
        switch(multiplicity) {
            case One: return "1";
            case MaybeOne: return "0";
            case Some: return "1";
            case Many: return "0";
        }
        return "0";
    }

    public String getUpperBound() {
        switch(multiplicity) {
            case One: return "1";
            case MaybeOne: return "1";
            case Some: return "-1";
            case Many: return "-1";
        }
        return "-1";
    }
    
    public CoreClass getParticipant() {
        return participant;
    }
    
	private CoreClass participant;
	private String phrase;
	private CoreMultiplicity multiplicity;
}
