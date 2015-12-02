package hu.elte.txtuml.api.model.assocends;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import hu.elte.txtuml.api.model.CompositionTest;

@RunWith(Suite.class)
@SuiteClasses({ QueryingAssociationEndsTest.class, CompositionTest.class })
public class AllAssociationEndTests {
}
