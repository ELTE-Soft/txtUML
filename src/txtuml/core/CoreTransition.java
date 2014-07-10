package txtuml.core;

public class CoreTransition {
    public CoreTransition(CoreSignal s, CoreState f, CoreState t, CoreMethod e, CoreMethod g) {
        trigger = s;
        from = f;
        to = t;
        effect = e;
        guard = g;
    }
    
    public CoreState getFrom() {
    	return from;
    }

    public CoreState getTo() {
    	return to;
    }

    public CoreSignal getTrigger() {
    	return trigger;
    }

    public CoreMethod getEffect() {
    	return effect;
    }

    public CoreMethod getGuard() {
    	return guard;
    }
    
	private CoreSignal trigger;
	private CoreState from;
	private CoreState to;
	private CoreMethod effect;
	private CoreMethod guard;
}
