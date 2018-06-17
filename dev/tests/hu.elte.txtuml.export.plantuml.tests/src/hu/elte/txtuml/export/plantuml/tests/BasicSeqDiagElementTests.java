package hu.elte.txtuml.export.plantuml.tests;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class BasicSeqDiagElementTests extends PlantUmlExportTestBase {

	@Test
	public void testLifelines() {
		List<String> expected = new ArrayList<>();
		expected.add("@startuml");
		expected.add("participant lifeline1");
		expected.add("participant lifeline3");
		expected.add("participant lifeline2");
		expected.add("@enduml");

		assertOutput("SequenceBasic", expected);
	}

	@Test
	public void testMessaging() {
		List<String> expected = new ArrayList<>();
		expected.add("@startuml");
		expected.add("participant lifeline1");
		expected.add("participant lifeline3");
		expected.add("participant lifeline2");
		expected.add("lifeline1->lifeline2 : " + project.getName() + ".testmodel.TestSig");
		expected.add("lifeline2->lifeline3 : " + project.getName() + ".testmodel.TestSig");
		expected.add("lifeline3->lifeline2 : " + project.getName() + ".testmodel.TestSig");
		expected.add("lifeline2->lifeline1 : " + project.getName() + ".testmodel.TestSig");
		expected.add("@enduml");

		assertOutput("SequenceMessaging", expected);
	}

}
