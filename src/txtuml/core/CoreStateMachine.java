package txtuml.core;

import java.util.Vector;

public class CoreStateMachine {
    public CoreStateMachine() {
        states = new Vector<CoreState>();
        transitions = new Vector<CoreTransition>();
    }
    
    public void addState(CoreState st) {
        states.add(st);
    }

    public void addTransition(CoreTransition tr) {
        transitions.add(tr);
    }
    
    public Vector<CoreTransition> getTransitions() {
    	return transitions;
    }
    
    public CoreState getState(String stateName) {
        for(CoreState s : states) {
            if(s.getName().equals(stateName)) {
                return s;
            }
        }
        return null;
    }

    public Vector<CoreState> getStates() {
        return states;
    }
    
    public CoreState getInitialState() {
    	return initialState;
    }

    public void setInitialState(CoreState s) {
    	initialState = s;
    }
    
	private Vector<CoreState> states;
	private CoreState initialState;
	private Vector<CoreTransition> transitions;
}
