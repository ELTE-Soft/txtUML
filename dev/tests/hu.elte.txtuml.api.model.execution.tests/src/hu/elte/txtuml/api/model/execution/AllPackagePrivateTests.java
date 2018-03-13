package hu.elte.txtuml.api.model.execution;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import hu.elte.txtuml.api.model.CollectionTest;
import hu.elte.txtuml.api.model.CompositionTest;
import hu.elte.txtuml.api.model.ModelExecutorThreadTest;

@RunWith(Suite.class)
@SuiteClasses({ ModelExecutorThreadTest.class, CompositionTest.class, CollectionTest.class })
public class AllPackagePrivateTests {
}
