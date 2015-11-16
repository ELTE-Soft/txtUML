package hu.elte.txtuml.layout.export.tests;

import hu.elte.txtuml.api.layout.Diagram;
import hu.elte.txtuml.api.layout.North;
import hu.elte.txtuml.api.layout.Priority;
import hu.elte.txtuml.api.layout.West;
import hu.elte.txtuml.layout.export.DiagramType;
import hu.elte.txtuml.layout.export.tests.model3.C1.Init;
import hu.elte.txtuml.layout.export.tests.model3.C1.S1;
import hu.elte.txtuml.layout.export.tests.model3.C1.S2;
import hu.elte.txtuml.layout.export.tests.model3.C1.T1;
import hu.elte.txtuml.layout.export.tests.model3.C1.T2;
import hu.elte.txtuml.layout.visualizer.statements.StatementType;

import org.junit.Test;

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
