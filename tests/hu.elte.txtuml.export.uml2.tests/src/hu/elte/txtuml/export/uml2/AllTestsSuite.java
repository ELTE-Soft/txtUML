package hu.elte.txtuml.export.uml2;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import hu.elte.txtuml.export.uml2.transform.ActionImporterTest;
import hu.elte.txtuml.export.uml2.transform.AssociationImporterTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({ ActionImporterTest.class, AssociationImporterTest.class} )
public final class AllTestsSuite {

}
