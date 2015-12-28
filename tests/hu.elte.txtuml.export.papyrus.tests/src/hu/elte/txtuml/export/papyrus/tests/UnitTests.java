package hu.elte.txtuml.export.papyrus.tests;

import hu.elte.txtuml.export.papyrus.api.tests.DiagramElementsModifierTest;
import hu.elte.txtuml.export.papyrus.elementsarrangers.txtumllayout.tests.LayoutTransformerTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ DiagramManagerTest.class, PapyrusModelCreatorTest.class,
		ProjectUtilsTest.class, DiagramElementsModifierTest.class, LayoutTransformerTest.class })
public class UnitTests {

}
