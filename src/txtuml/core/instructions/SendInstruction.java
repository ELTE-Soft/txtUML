package txtuml.core.instructions;

import txtuml.core.CoreEvent;
import txtuml.core.CoreInstance;

public class SendInstruction extends Instruction {
    public SendInstruction(CoreEvent ev, CoreInstance rec) {
        event = ev;
        receiver = rec;
    }
    
    public CoreEvent getEvent() {
    	return event;
    }
    
    public CoreInstance getReceiver() {
    	return receiver;
    }
    
    private CoreEvent event;
    private CoreInstance receiver;
}
