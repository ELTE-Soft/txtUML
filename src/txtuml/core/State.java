package txtuml.core;

public class State {
	public State(String n, Method m) {
		name = n;
		action = m;
	}
    
    public String getName() {
        return name;
    }
    
    public Method getAction() {
    	return action;
    }
    
	public String name;
	public Method action;
}
