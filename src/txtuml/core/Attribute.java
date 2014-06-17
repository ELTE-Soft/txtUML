package txtuml.core;

public class Attribute {
    public Attribute(String attrName, DataType attrType) {
        name = attrName;
        type = attrType;
    }
    
    public String getName() {
        return name;
    }
    
    public DataType getType() {
        return type;
    }
    
	String name;
	DataType type;
}
