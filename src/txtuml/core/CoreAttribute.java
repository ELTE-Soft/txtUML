package txtuml.core;

public class CoreAttribute extends CoreNamedObject {
    public CoreAttribute(String attrName, CoreDataType attrType) {
    	super(attrName);
        type = attrType;
    }
        
    public CoreDataType getType() {
        return type;
    }
    
	private CoreDataType type;
}
