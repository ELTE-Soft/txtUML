package hu.elte.txtuml.export.papyrus.tests;

import hu.elte.txtuml.export.papyrus.elementsarrangers.tests.DiagramElementsArrangerTest;
import hu.elte.txtuml.export.papyrus.elementsarrangers.txtumllayout.tests.LayoutTransformerTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ DiagramManagerTest.class, PapyrusModelCreatorTest.class,
		ProjectUtilsTest.class, DiagramElementsArrangerTest.class, LayoutTransformerTest.class })
public class AllTests {

}
