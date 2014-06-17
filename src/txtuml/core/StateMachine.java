package txtuml.core;

import java.util.Vector;

public class StateMachine {
    public StateMachine() {
        states = new Vector<State>();
        transitions = new Vector<Transition>();
    }
    
    public void addState(State st) {
        states.add(st);
    }

    public void addTransition(Transition tr) {
        transitions.add(tr);
    }
    
    public Vector<Transition> getTransitions() {
    	return transitions;
    }
    
    public State getState(String stateName) {
        for(State s : states) {
            if(s.name.equals(stateName)) {
                return s;
            }
        }
        return null;
    }

    public Vector<State> getStates() {
        return states;
    }
    
    public State getInitialState() {
    	return initialState;
    }

    public void setInitialState(State s) {
    	initialState = s;
    }
    
	public Vector<State> states;
	State initialState;
	Vector<Transition> transitions;
}
