package txtuml.core;

public class SendInstruction extends Instruction {
    public SendInstruction(Event ev, Instance rec) {
        event = ev;
        receiver = rec;
    }
    
    public Event getEvent() {
    	return event;
    }
    
    public Instance getReceiver() {
    	return receiver;
    }
    
    Event event;
    Instance receiver;
}
