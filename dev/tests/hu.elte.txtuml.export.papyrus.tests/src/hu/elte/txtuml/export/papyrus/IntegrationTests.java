package hu.elte.txtuml.export.papyrus;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import hu.elte.txtuml.export.papyrus.diagrams.clazz.impl.ClassDiagramNotationManagerImpl;
import hu.elte.txtuml.export.papyrus.diagrams.clazz.impl.ClassDiagramNotationManagerImplTest;

@RunWith(Suite.class)
@SuiteClasses({PapyrusModelCreatorTest.class, ProjectUtilsTest.class, ClassDiagramNotationManagerImplTest.class})

public class IntegrationTests {
}
