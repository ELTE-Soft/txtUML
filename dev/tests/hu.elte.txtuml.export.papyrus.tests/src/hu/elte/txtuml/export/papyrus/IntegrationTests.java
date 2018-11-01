package hu.elte.txtuml.export.papyrus;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import hu.elte.txtuml.export.papyrus.diagrams.clazz.impl.ClassDiagramNotationManagerImplTest;

@RunWith(Suite.class)
@SuiteClasses({PapyrusModelCreatorTest.class, ClassDiagramNotationManagerImplTest.class})

public class IntegrationTests {
}
