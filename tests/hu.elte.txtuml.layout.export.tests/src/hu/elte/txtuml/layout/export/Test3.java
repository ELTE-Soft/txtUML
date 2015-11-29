package hu.elte.txtuml.layout.export;

import hu.elte.txtuml.api.layout.Diagram;
import hu.elte.txtuml.api.layout.North;
import hu.elte.txtuml.api.layout.Priority;
import hu.elte.txtuml.api.layout.West;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.Model;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;
import hu.elte.txtuml.layout.export.DiagramType;
import hu.elte.txtuml.layout.export.Model3.C1.Init;
import hu.elte.txtuml.layout.export.Model3.C1.S1;
import hu.elte.txtuml.layout.export.Model3.C1.S2;
import hu.elte.txtuml.layout.export.Model3.C1.T1;
import hu.elte.txtuml.layout.export.Model3.C1.T2;
import hu.elte.txtuml.layout.visualizer.statements.StatementType;

import org.junit.Test;

class Model3 extends Model {

	class Sig extends Signal {}
	
	class C1 extends ModelClass {
		
		class Init extends Initial {}
		class S1 extends State {}
		class S2 extends State {}
		
		@From(Init.class) @To(S1.class)
		class T1 extends Transition {}

		@From(S1.class) @To(S2.class) @Trigger(Sig.class)
		class T2 extends Transition {}
		
	}
	
}

class Diagram3 extends Diagram {

	@West(val = S1.class, from = S2.class)
	@North(val = Init.class, from = { S1.class, S2.class })
	@Priority(val = T1.class, prior = 10)
	class L extends Layout {
	}

}

public class Test3 extends BaseTest {

	@Test
	public void run() {
		node(Init.class);
		node(S1.class);
		node(S2.class);
		
		type = DiagramType.StateMachine;
		
		directedLink(T1.class, Init.class, S1.class);
		directedLink(T2.class, Init.class, S1.class);
		
		statement(StatementType.west, S1.class, S2.class);
		statement(StatementType.north, Init.class, S1.class);		
		statement(StatementType.north, Init.class, S2.class);
		statement(StatementType.priority, T1.class, 10);
		
		testForSuccess(Diagram3.class);		
	}
	
}
