package hu.elte.txtuml.export.papyrus.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import hu.elte.txtuml.export.papyrus.api.tests.DiagramElementsModifierTest;
import hu.elte.txtuml.export.papyrus.elementsarrangers.txtumllayout.tests.LayoutTransformerTest;

@RunWith(Suite.class)
@SuiteClasses({ DiagramManagerTest.class, PapyrusModelCreatorTest.class,
		ProjectUtilsTest.class, DiagramElementsModifierTest.class, LayoutTransformerTest.class })
public class UnitTests {

}
