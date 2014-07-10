package txtuml.core.instructions;

import txtuml.core.CoreSignal;
import txtuml.core.CoreInstance;

public class SendInstruction extends Instruction {
    public SendInstruction(CoreSignal ev, CoreInstance rec) {
        event = ev;
        receiver = rec;
    }
    
    public CoreSignal getEvent() {
    	return event;
    }
    
    public CoreInstance getReceiver() {
    	return receiver;
    }
    
    private CoreSignal event;
    private CoreInstance receiver;
}
