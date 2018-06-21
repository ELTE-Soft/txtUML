package hu.elte.txtuml.seqdiag.tests;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class SequenceDiagramFragmentsTests extends PlantUmlExportTestBase {

	@Test
	public void testOptFragment() {
		List<String> expected = new ArrayList<>();
		expected.add("@startuml");
		expected.add("participant lifeline1");
		expected.add("participant lifeline3");
		expected.add("participant lifeline2");
		expected.add("opt true");
		expected.add("end");
		expected.add("@enduml");

		assertOutput("OPTFragment", expected);
	}

	@Test
	public void testAltFragment() {
		List<String> expected = new ArrayList<>();
		expected.add("@startuml");
		expected.add("participant lifeline1");
		expected.add("participant lifeline3");
		expected.add("participant lifeline2");
		expected.add("alt true");
		expected.add("else true && true");
		expected.add("else");
		expected.add("end");
		expected.add("@enduml");

		assertOutput("ALTFragment", expected);
	}

	@Test
	public void testLoopFragment() {
		List<String> expected = new ArrayList<>();
		expected.add("@startuml");
		expected.add("participant lifeline1");
		expected.add("participant lifeline3");
		expected.add("participant lifeline2");
		expected.add("loop from i=1 to i < 1 by i+1");
		expected.add("end");
		expected.add("loop while i == 2");
		expected.add("end");
		expected.add("loop while i == 2");
		expected.add("end");
		expected.add("@enduml");

		assertOutput("LoopFragment", expected);
	}

	@Test
	public void testParFragment() {
		List<String> expected = new ArrayList<>();
		expected.add("@startuml");
		expected.add("participant lifeline1");
		expected.add("participant lifeline3");
		expected.add("participant lifeline2");
		expected.add("par");
		expected.add("else");
		expected.add("lifeline1->lifeline2 : " + project.getName() + ".testmodel.TestSig");
		expected.add("end");
		expected.add("@enduml");

		assertOutput("ParFragment", expected);
	}

}
