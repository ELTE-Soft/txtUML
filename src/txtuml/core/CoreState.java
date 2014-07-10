package txtuml.core;

public class CoreState extends CoreNamedObject {
	private CoreState(String name, CoreMethod entry, CoreMethod exit, CoreStateMachine stateMachine, boolean initial) {
		super(name);
		this.entry = entry;
		this.exit = exit;
		this.initial = initial;
		this.stateMachine = stateMachine;
	}
	public CoreState(String name, boolean initial) {
		this(name, null, null, null, initial);
	}
	public CoreState(String name, CoreMethod entry, CoreMethod exit) {
		this(name, entry, exit, null);
	}
	public CoreState(String name, CoreMethod entry, CoreMethod exit, CoreStateMachine stateMachine) {
		this(name, entry, exit, stateMachine, false);
	}
    
    public CoreMethod getEntry() {
    	return entry;
    }
    public CoreMethod getExit() {
    	return exit;
    }
    public CoreStateMachine getStateMachine() {
    	return stateMachine;
    }
    public boolean isInitial() {
    	return initial;
    }
    
    private boolean initial;
	private CoreMethod entry;
	private CoreMethod exit;
	private CoreStateMachine stateMachine;
}
