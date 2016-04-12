package hu.elte.txtuml.export.papyrus;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import hu.elte.txtuml.export.papyrus.elementsarrangers.txtumllayout.LayoutTransformerTest;

@RunWith(Suite.class)
@SuiteClasses({ DiagramManagerUnitTest.class, LayoutTransformerTest.class })
public class UnitTests {
}
