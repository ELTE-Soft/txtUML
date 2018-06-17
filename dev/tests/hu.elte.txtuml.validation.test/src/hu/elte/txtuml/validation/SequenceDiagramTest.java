package hu.elte.txtuml.validation;

import static hu.elte.txtuml.validation.sequencediagram.SequenceErrors.INVALID_ACTION_CALL;
import static hu.elte.txtuml.validation.sequencediagram.SequenceErrors.INVALID_LIFELINE_DECLARATION;
import static hu.elte.txtuml.validation.sequencediagram.SequenceErrors.INVALID_POSITION;
import static hu.elte.txtuml.validation.sequencediagram.SequenceErrors.SEND_EXPECTED;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.Arrays;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.junit.Test;

import hu.elte.txtuml.validation.sequencediagram.SequenceErrors;
import hu.elte.txtuml.validation.sequencediagram.visitors.SequenceDiagramVisitor;

/**
 * Test cases for sequence diagram validation.
 */
public class SequenceDiagramTest extends ValidationTestBase {

	private static final String VALIDATION_EXAMPLES_PACKAGE = "/hu/elte/txtuml/examples/validation/sequencediagram/";

	@Override
	protected String getValidationExamplesPackage() {
		return VALIDATION_EXAMPLES_PACKAGE;
	}

	@Test
	public void testEmptyRunMethod() throws Exception {
		assertErrors("EmptyRun.java", SEND_EXPECTED);
	}

	@Test
	public void testMissingSend() throws Exception {
		assertErrors("MissingSend.java", SEND_EXPECTED);
	}

	@Test
	public void testInvalidLifeline() throws Exception {
		assertErrors("InvalidLifelineDeclaration.java", INVALID_LIFELINE_DECLARATION, SEND_EXPECTED);
	}

	@Test
	public void testInvalidPosition() throws Exception {
		assertErrors("InvalidPosition.java", INVALID_POSITION, SEND_EXPECTED);
	}

	@Test
	public void testInvalidActionCall() throws Exception {
		assertErrors("InvalidActionCall.java", INVALID_ACTION_CALL);
	}

	@Test
	public void testValidDiagram() throws Exception {
		assertValid("ValidDiagram.java");
	}

	private void assertValid(String fileName) throws IOException {
		assertErrors(fileName);
	}

	private void assertErrors(String fileName, SequenceErrors... errorTypes) throws IOException {
		CompilationUnit compilationUnit = prepareAST(fileName);
		compilationUnit.accept(new SequenceDiagramVisitor(mockCollector));
		Arrays.asList(errorTypes).forEach(errorType -> verify(mockCollector).report(is(errorType)));
		checkNoOtherErrorRaised();
	}

}
