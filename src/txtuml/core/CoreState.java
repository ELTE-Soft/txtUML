package txtuml.core;

public class CoreState extends CoreNamedObject {
	public CoreState(String name, CoreMethod m) {
		super(name);
		action = m;
	}
    
    public CoreMethod getAction() {
    	return action;
    }
    
	private CoreMethod action;
}
