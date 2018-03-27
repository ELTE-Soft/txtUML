package hu.elte.txtuml.validation;

import static hu.elte.txtuml.validation.model.ModelErrors.INVALID_ATTRIBUTE_TYPE;
import static hu.elte.txtuml.validation.model.ModelErrors.INVALID_CHILDREN_ELEMENT;
import static hu.elte.txtuml.validation.model.ModelErrors.INVALID_DATA_TYPE_FIELD;
import static hu.elte.txtuml.validation.model.ModelErrors.INVALID_MODEL_CLASS_ELEMENT;
import static hu.elte.txtuml.validation.model.ModelErrors.INVALID_MODIFIER;
import static hu.elte.txtuml.validation.model.ModelErrors.INVALID_PARAMETER_TYPE;
import static hu.elte.txtuml.validation.model.ModelErrors.INVALID_SIGNAL_CONTENT;
import static hu.elte.txtuml.validation.model.ModelErrors.INVALID_TYPE_IN_MODEL;
import static hu.elte.txtuml.validation.model.ModelErrors.MISSING_TRANSITION_SOURCE;
import static hu.elte.txtuml.validation.model.ModelErrors.MISSING_TRANSITION_TARGET;
import static hu.elte.txtuml.validation.model.ModelErrors.MISSING_TRANSITION_TRIGGER;
import static hu.elte.txtuml.validation.model.ModelErrors.MUTABLE_DATA_TYPE_FIELD;
import static hu.elte.txtuml.validation.model.ModelErrors.STATE_METHOD_PARAMETERS;
import static hu.elte.txtuml.validation.model.ModelErrors.TRANSITION_METHOD_PARAMETERS;
import static hu.elte.txtuml.validation.model.ModelErrors.TRIGGER_ON_INITIAL_TRANSITION;
import static hu.elte.txtuml.validation.model.ModelErrors.UNKNOWN_CLASS_IN_STATE;
import static hu.elte.txtuml.validation.model.ModelErrors.UNKNOWN_STATE_METHOD;
import static hu.elte.txtuml.validation.model.ModelErrors.UNKNOWN_TRANSITION_METHOD;
import static hu.elte.txtuml.validation.model.ModelErrors.WRONG_COMPOSITION_ENDS;
import static hu.elte.txtuml.validation.model.ModelErrors.WRONG_NUMBER_OF_ASSOCIATION_ENDS;
import static hu.elte.txtuml.validation.model.ModelErrors.WRONG_TYPE_IN_ASSOCIATION;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.compiler.ReconcileContext;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.hamcrest.Description;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import hu.elte.txtuml.utils.jdt.SharedUtils;
import hu.elte.txtuml.validation.common.ProblemCollector;
import hu.elte.txtuml.validation.common.SourceInfo;
import hu.elte.txtuml.validation.model.JtxtUMLModelCompilationParticipant;
import hu.elte.txtuml.validation.model.ModelErrors;
import hu.elte.txtuml.validation.model.ModelValidationError;
import hu.elte.txtuml.validation.model.visitors.ModelVisitor;

public class ModelTest {

	/*
	 * This test suite relies on the repository having a given structure. The
	 * structure is encoded in these pathes:
	 */

	private static final String VALIDATION_EXAMPLES_PACKAGE = "/hu/elte/txtuml/examples/validation/";
	private static final String VALIDATION_EXAMPLES_ROOT = "../../../examples/tests/hu.elte.txtuml.examples.validation/src";
	private static final String API_SRC_LOC = "../../plugins/hu.elte.txtuml.api.model/src/";

	ProblemCollector mockCollector;

	@Before
	public void before() {
		mockCollector = mock(ModelProblemCollector.class);
		SourceInfo sourceInfo = mock(SourceInfo.class);
		when(mockCollector.getSourceInfo()).thenReturn(sourceInfo);
		when(sourceInfo.getOriginatingFileName()).thenReturn("");
		when(sourceInfo.getSourceLineNumber(anyInt())).thenReturn(0);
	}

	@Test
	public void testInvalidClass() throws Exception {
		CompilationUnit compilationUnit = prepareAST("InvalidClass.java");

		compilationUnit.accept(new ModelVisitor(mockCollector));

		verify(mockCollector).report(is(INVALID_TYPE_IN_MODEL));

		checkNoOtherErrorRaised();
	}

	@Test
	public void testInvalidParameterType() throws Exception {
		CompilationUnit compilationUnit = prepareAST("InvalidParameterType.java");

		compilationUnit.accept(new ModelVisitor(mockCollector));

		verify(mockCollector, times(4)).report(is(INVALID_PARAMETER_TYPE));

		checkNoOtherErrorRaised();
	}

	@Test
	public void testInvalidTypeInModel() throws Exception {
		CompilationUnit compilationUnit = prepareAST("InvalidTypeInModelClass.java");

		compilationUnit.accept(new ModelVisitor(mockCollector));

		verify(mockCollector).report(is(INVALID_ATTRIBUTE_TYPE));

		checkNoOtherErrorRaised();
	}

	@Test
	public void testInvalidModifier() throws Exception {
		CompilationUnit compilationUnit = prepareAST("InvalidModifier.java");

		compilationUnit.accept(new ModelVisitor(mockCollector));

		verify(mockCollector, times(2)).report(is(INVALID_MODIFIER));

		checkNoOtherErrorRaised();
	}

	@Test
	public void testExternalsAreOmitted() throws Exception {
		CompilationUnit compilationUnit = prepareAST("ExternalsAreOmitted.java");

		compilationUnit.accept(new ModelVisitor(mockCollector));

		checkNoOtherErrorRaised();
	}
	
	@Test
	public void testFieldType() throws Exception {
		CompilationUnit compilationUnit = prepareAST("InvalidFieldType.java");

		compilationUnit.accept(new ModelVisitor(mockCollector));

		verify(mockCollector, times(3)).report(is(INVALID_ATTRIBUTE_TYPE));

		checkNoOtherErrorRaised();
	}

	@Test
	public void testInvalidTypeInSignal() throws Exception {
		CompilationUnit compilationUnit = prepareAST("InvalidTypeInSignal.java");

		compilationUnit.accept(new ModelVisitor(mockCollector));

		verify(mockCollector, times(2)).report(is(INVALID_ATTRIBUTE_TYPE));

		checkNoOtherErrorRaised();
	}

	@Test
	public void testInvalidClassInModelClass() throws Exception {
		CompilationUnit compilationUnit = prepareAST("InvalidClassInModelClass.java");

		compilationUnit.accept(new ModelVisitor(mockCollector));

		verify(mockCollector, times(2)).report(is(INVALID_MODEL_CLASS_ELEMENT));

		checkNoOtherErrorRaised();
	}

	@Test
	public void testMethodInSignal() throws Exception {
		CompilationUnit compilationUnit = prepareAST("MethodInSignal.java");

		compilationUnit.accept(new ModelVisitor(mockCollector));

		verify(mockCollector, times(1)).report(is(INVALID_SIGNAL_CONTENT));

		checkNoOtherErrorRaised();
	}

	@Test
	public void testWrongNumberOfAssociationEnds() throws Exception {
		CompilationUnit compilationUnit = prepareAST("WrongNumberOfAssociationEnds.java");

		compilationUnit.accept(new ModelVisitor(mockCollector));

		verify(mockCollector, times(3)).report(is(WRONG_NUMBER_OF_ASSOCIATION_ENDS));

		checkNoOtherErrorRaised();
	}

	@Test
	public void testAssociationWrongInnerClass() throws Exception {
		CompilationUnit compilationUnit = prepareAST("AssociationWrongInnerClass.java");

		compilationUnit.accept(new ModelVisitor(mockCollector));

		verify(mockCollector, times(2)).report(is(WRONG_TYPE_IN_ASSOCIATION));

		checkNoOtherErrorRaised();
	}

	@Test
	public void testCompositionNotExactlyOneContainer() throws Exception {
		CompilationUnit compilationUnit = prepareAST("CompositionNotExactlyOneContainer.java");

		compilationUnit.accept(new ModelVisitor(mockCollector));

		verify(mockCollector, times(2)).report(is(WRONG_COMPOSITION_ENDS));

		checkNoOtherErrorRaised();
	}

	@Test
	public void testStateMethodNotCorrect() throws Exception {
		CompilationUnit compilationUnit = prepareAST("StateMethodNotCorrect.java");

		compilationUnit.accept(new ModelVisitor(mockCollector));

		verify(mockCollector).report(is(INVALID_CHILDREN_ELEMENT));
		verify(mockCollector).report(is(UNKNOWN_STATE_METHOD));
		verify(mockCollector).report(is(STATE_METHOD_PARAMETERS));

		checkNoOtherErrorRaised();
	}

	@Test
	public void testStateInnerTypesNotCorrect() throws Exception {
		CompilationUnit compilationUnit = prepareAST("StateInnerTypesNotCorrect.java");

		compilationUnit.accept(new ModelVisitor(mockCollector));

		verify(mockCollector, times(3)).report(is(INVALID_CHILDREN_ELEMENT));
		verify(mockCollector).report(is(UNKNOWN_CLASS_IN_STATE));

		checkNoOtherErrorRaised();
	}

	@Test
	public void testTransitionMethodNotCorrect() throws Exception {
		CompilationUnit compilationUnit = prepareAST("TransitionMethodNotCorrect.java");

		compilationUnit.accept(new ModelVisitor(mockCollector));

		verify(mockCollector).report(is(UNKNOWN_TRANSITION_METHOD));
		verify(mockCollector).report(is(TRANSITION_METHOD_PARAMETERS));

		checkNoOtherErrorRaised();
	}

	@Test
	public void testTransitionsWithoutSourceTargetOrTrigger() throws Exception {
		CompilationUnit compilationUnit = prepareAST("TransitionsWithoutSourceTargetOrTrigger.java");

		compilationUnit.accept(new ModelVisitor(mockCollector));

		verify(mockCollector).report(is(TRIGGER_ON_INITIAL_TRANSITION));
		verify(mockCollector).report(is(MISSING_TRANSITION_SOURCE));
		verify(mockCollector).report(is(MISSING_TRANSITION_TARGET));
		verify(mockCollector).report(is(MISSING_TRANSITION_TRIGGER));

		checkNoOtherErrorRaised();
	}

	@Test
	public void testDataTypeFieldNotFinal() throws Exception {
		CompilationUnit compilationUnit = prepareAST("DataTypeFieldNotFinal.java");

		compilationUnit.accept(new ModelVisitor(mockCollector));

		verify(mockCollector).report(is(MUTABLE_DATA_TYPE_FIELD));

		checkNoOtherErrorRaised();
	}

	@Test
	public void testDataTypeInvalidFieldType() throws Exception {
		CompilationUnit compilationUnit = prepareAST("DataTypeInvalidFieldType.java");

		compilationUnit.accept(new ModelVisitor(mockCollector));

		verify(mockCollector, times(2)).report(is(INVALID_DATA_TYPE_FIELD));

		checkNoOtherErrorRaised();
	}

	private void checkNoOtherErrorRaised() {
		verify(mockCollector, atLeast(0)).getSourceInfo();
		verifyNoMoreInteractions(mockCollector);
	}

	private CompilationUnit prepareAST(String javaFile) throws IOException {
		File projectRoot = new File(VALIDATION_EXAMPLES_ROOT).getAbsoluteFile();
		File sourceFile = new File(projectRoot.getCanonicalPath() + VALIDATION_EXAMPLES_PACKAGE + javaFile);

		ASTParser parser = ASTParser.newParser(AST.JLS8);
		char[] content = SharedUtils.getFileContents(sourceFile);

		String[] classpath = {};
		String[] sourcepath = { projectRoot.getCanonicalPath(), new File(API_SRC_LOC).getCanonicalPath() };
		String[] encodings = { "UTF-8", "UTF-8" };

		parser.setSource(content);

		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		parser.setUnitName(sourceFile.getName());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setEnvironment(classpath, sourcepath, encodings, true);

		return (CompilationUnit) parser.createAST(null);
	}

	private static ModelValidationError is(ModelErrors type) {
		return argThat(new ArgumentMatcher<ModelValidationError>() {
			@Override
			public void describeTo(Description description) {
				description.appendText("Model error of type " + type + " required.");
			}

			@Override
			public boolean matches(Object argument) {
				return ((ModelValidationError) argument).getType() == type;
			}
		});
	}

	public static class ModelProblemCollector extends ProblemCollector {

		public ModelProblemCollector(CompilationUnit unit, IResource resource) throws JavaModelException {
			super(JtxtUMLModelCompilationParticipant.JTXTUML_MODEL_MARKER_TYPE, unit, resource);
		}

		public ModelProblemCollector(ReconcileContext context) throws JavaModelException {
			super(JtxtUMLModelCompilationParticipant.JTXTUML_MODEL_MARKER_TYPE, context);
		}

	}

}
