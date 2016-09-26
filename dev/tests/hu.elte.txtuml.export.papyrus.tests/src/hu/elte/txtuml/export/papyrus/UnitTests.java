package hu.elte.txtuml.export.papyrus;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import hu.elte.txtuml.export.papyrus.diagrams.clazz.impl.ClassDiagramElementsManagerTest;
import hu.elte.txtuml.export.papyrus.diagrams.clazz.impl.ClassDiagramElementsProviderImplTest;
import hu.elte.txtuml.export.papyrus.diagrams.statemachine.impl.StateMachineDiagramElementsManagerTest;
import hu.elte.txtuml.export.papyrus.diagrams.statemachine.impl.StateMachineDiagramElementsProviderImplTest;
import hu.elte.txtuml.export.papyrus.elementsarrangers.txtumllayout.LayoutTransformerTest;

@RunWith(Suite.class)
@SuiteClasses({ DiagramManagerUnitTest.class, LayoutTransformerTest.class, ClassDiagramElementsManagerTest.class,
		StateMachineDiagramElementsManagerTest.class, ClassDiagramElementsProviderImplTest.class,
		StateMachineDiagramElementsProviderImplTest.class })
public class UnitTests {
}
