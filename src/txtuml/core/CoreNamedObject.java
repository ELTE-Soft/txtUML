package txtuml.core;

public abstract class CoreNamedObject {
	public CoreNamedObject(String name) {
		this.name = name;
	}
	
    public String getName() {
        return name;
    }
 
    private final String name;
}
