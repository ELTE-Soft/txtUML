package txtuml.core.instructions;

import txtuml.core.CoreClass;
import txtuml.core.CoreInstance;

public class DeleteInstruction extends CreateInstruction {
    public DeleteInstruction(CoreClass c, CoreInstance ref) {
        super(c, ref);
    }
}