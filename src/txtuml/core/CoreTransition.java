package txtuml.core;

public class CoreTransition {
    public CoreTransition(CoreEvent e, CoreState f, CoreState t, CoreMethod a) {
        trigger = e;
        from = f;
        to = t;
        action = a;
    }
    
    public CoreState getFrom() {
    	return from;
    }

    public CoreState getTo() {
    	return to;
    }

    public CoreEvent getTrigger() {
    	return trigger;
    }

    public CoreMethod getAction() {
    	return action;
    }

	private CoreEvent trigger;
	private CoreState from;
	private CoreState to;
	private CoreMethod action;
}
