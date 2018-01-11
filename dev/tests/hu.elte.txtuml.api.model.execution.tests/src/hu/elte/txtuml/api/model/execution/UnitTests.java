package hu.elte.txtuml.api.model.execution;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import hu.elte.txtuml.api.model.execution.seqdiag.SequenceDiagramTests;
import hu.elte.txtuml.api.model.execution.unittests.AssociationEndErrorTests;
import hu.elte.txtuml.api.model.execution.unittests.AssociationEndTests;
import hu.elte.txtuml.api.model.execution.unittests.CompositionTests;
import hu.elte.txtuml.api.model.execution.unittests.DeletionErrorTests;
import hu.elte.txtuml.api.model.execution.unittests.OtherErrorTests;
import hu.elte.txtuml.api.model.execution.unittests.ShutdownTests;
import hu.elte.txtuml.api.model.execution.unittests.StateMachineErrorTests;
import hu.elte.txtuml.api.model.execution.unittests.StateMachineTests;

@RunWith(Suite.class)
@SuiteClasses({ AllPackagePrivateTests.class, AssociationEndTests.class, CompositionTests.class,
		StateMachineTests.class, DeletionErrorTests.class, StateMachineErrorTests.class, AssociationEndErrorTests.class,
		OtherErrorTests.class, ShutdownTests.class, SequenceDiagramTests.class })
public class UnitTests {
}
