package hu.elte.txtuml.validation;

import static hu.elte.txtuml.validation.sequencediagram.ValidationErrors.INVALID_LIFELINE_DECLARATION;
import static hu.elte.txtuml.validation.sequencediagram.ValidationErrors.INVALID_POSITION;
import static hu.elte.txtuml.validation.sequencediagram.ValidationErrors.SEND_EXPECTED;
import static hu.elte.txtuml.validation.sequencediagram.ValidationErrors.INVALID_ACTION_CALL;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.Arrays;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.hamcrest.Description;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import hu.elte.txtuml.validation.common.ValidationProblem;
import hu.elte.txtuml.validation.sequencediagram.ValidationErrors;
import hu.elte.txtuml.validation.sequencediagram.visitors.SequenceDiagramVisitor;

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

	private void assertErrors(String fileName, ValidationErrors... errorTypes) throws IOException {
		CompilationUnit compilationUnit = prepareAST(fileName);
		compilationUnit.accept(new SequenceDiagramVisitor(mockCollector));
		Arrays.asList(errorTypes).forEach(errorType -> verify(mockCollector).report(is(errorType)));
		checkNoOtherErrorRaised();
	}

	private static ValidationProblem is(ValidationErrors type) {
		return argThat(new ArgumentMatcher<ValidationProblem>() {

			@Override
			public void describeTo(Description description) {
				description.appendText("Error of type " + type + " required.");
			}

			@Override
			public boolean matches(Object argument) {
				return ((ValidationProblem) argument).getID() == type.ordinal();
			}

		});
	}

}
