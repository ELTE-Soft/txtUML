package txtuml.core;

public class LogInstruction extends Instruction {
    public LogInstruction(String msg) {
        message = msg;
    }

    public String getMessage() {
    	return message;
    }
    
    String message;
}
