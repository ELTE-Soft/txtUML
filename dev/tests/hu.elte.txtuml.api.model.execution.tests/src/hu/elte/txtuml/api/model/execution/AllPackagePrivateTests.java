package hu.elte.txtuml.api.model.execution;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import hu.elte.txtuml.api.model.CollectionTests;
import hu.elte.txtuml.api.model.DataTypeTests;

@RunWith(Suite.class)
@SuiteClasses({ CollectionTests.class, DataTypeTests.class })
public class AllPackagePrivateTests {
}
