package txtuml.core;

public class Transition {
    public Transition(Event e, State f, State t, Method a) {
        trigger = e;
        from = f;
        to = t;
        action = a;
    }
    
    public State getFrom() {
    	return from;
    }

    public State getTo() {
    	return to;
    }

    public Event getTrigger() {
    	return trigger;
    }

    public Method getAction() {
    	return action;
    }

	Event trigger;
	State from;
	State to;
	Method action;
}
