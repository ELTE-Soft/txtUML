package txtuml.core.instructions;


public class LogInstruction extends Instruction {
    public LogInstruction(String msg) {
        message = msg;
    }

    public String getMessage() {
    	return message;
    }
    
    private String message;
}
