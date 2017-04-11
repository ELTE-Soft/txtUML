package hu.elte.txtuml.validation;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.junit.Before;
import org.junit.Test;

import hu.elte.txtuml.utils.jdt.SharedUtils;
import hu.elte.txtuml.validation.problems.association.WrongCompositionEnds;
import hu.elte.txtuml.validation.problems.association.WrongNumberOfAssociationEnds;
import hu.elte.txtuml.validation.problems.association.WrongTypeInAssociation;
import hu.elte.txtuml.validation.problems.datatype.InvalidDataTypeField;
import hu.elte.txtuml.validation.problems.datatype.MutableDataTypeField;
import hu.elte.txtuml.validation.problems.general.InvalidChildrenElement;
import hu.elte.txtuml.validation.problems.general.InvalidModifier;
import hu.elte.txtuml.validation.problems.general.InvalidParameterType;
import hu.elte.txtuml.validation.problems.general.InvalidTypeInModel;
import hu.elte.txtuml.validation.problems.modelclass.InvalidAttributeType;
import hu.elte.txtuml.validation.problems.modelclass.InvalidModelClassElement;
import hu.elte.txtuml.validation.problems.signal.InvalidSignalContent;
import hu.elte.txtuml.validation.problems.state.StateMethodParameters;
import hu.elte.txtuml.validation.problems.state.UnknownClassInState;
import hu.elte.txtuml.validation.problems.state.UnknownStateMethod;
import hu.elte.txtuml.validation.problems.transition.MissingTransitionSource;
import hu.elte.txtuml.validation.problems.transition.MissingTransitionTarget;
import hu.elte.txtuml.validation.problems.transition.MissingTransitionTrigger;
import hu.elte.txtuml.validation.problems.transition.TransitionMethodParameters;
import hu.elte.txtuml.validation.problems.transition.TriggerOnInitialTransition;
import hu.elte.txtuml.validation.problems.transition.UnknownTransitionMethod;
import hu.elte.txtuml.validation.visitors.ModelVisitor;

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
		mockCollector = mock(ProblemCollector.class);
		SourceInfo sourceInfo = mock(SourceInfo.class);
		when(mockCollector.getSourceInfo()).thenReturn(sourceInfo);
		when(sourceInfo.getOriginatingFileName()).thenReturn("");
		when(sourceInfo.getSourceLineNumber(anyInt())).thenReturn(0);
	}

	@Test
	public void testInvalidClass() throws Exception {
		CompilationUnit compilationUnit = prepareAST("InvalidClass.java");

		compilationUnit.accept(new ModelVisitor(mockCollector));

		verify(mockCollector).report(isA(InvalidTypeInModel.class));

		checkNoOtherErrorRaised();
	}

	@Test
	public void testInvalidParameterType() throws Exception {
		CompilationUnit compilationUnit = prepareAST("InvalidParameterType.java");

		compilationUnit.accept(new ModelVisitor(mockCollector));


		verify(mockCollector, times(4)).report(isA(InvalidParameterType.class));

		checkNoOtherErrorRaised();
	}

	@Test
	public void testInvalidTypeInModel() throws Exception {
		CompilationUnit compilationUnit = prepareAST("InvalidTypeInModelClass.java");

		compilationUnit.accept(new ModelVisitor(mockCollector));

		verify(mockCollector).report(isA(InvalidAttributeType.class));

		checkNoOtherErrorRaised();
	}

	@Test
	public void testInvalidModifier() throws Exception {
		CompilationUnit compilationUnit = prepareAST("InvalidModifier.java");

		compilationUnit.accept(new ModelVisitor(mockCollector));

		verify(mockCollector, times(2)).report(isA(InvalidModifier.class));

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


		verify(mockCollector, times(3)).report(isA(InvalidAttributeType.class));

		checkNoOtherErrorRaised();
	}

	@Test
	public void testInvalidTypeInSignal() throws Exception {
		CompilationUnit compilationUnit = prepareAST("InvalidTypeInSignal.java");

		compilationUnit.accept(new ModelVisitor(mockCollector));

		verify(mockCollector, times(2)).report(isA(InvalidAttributeType.class));

		checkNoOtherErrorRaised();
	}

	@Test
	public void testInvalidClassInModelClass() throws Exception {
		CompilationUnit compilationUnit = prepareAST("InvalidClassInModelClass.java");

		compilationUnit.accept(new ModelVisitor(mockCollector));

		verify(mockCollector, times(2)).report(isA(InvalidModelClassElement.class));

		checkNoOtherErrorRaised();
	}

	@Test
	public void testMethodInSignal() throws Exception {
		CompilationUnit compilationUnit = prepareAST("MethodInSignal.java");

		compilationUnit.accept(new ModelVisitor(mockCollector));

		verify(mockCollector, times(1)).report(isA(InvalidSignalContent.class));

		checkNoOtherErrorRaised();
	}

	@Test
	public void testWrongNumberOfAssociationEnds() throws Exception {
		CompilationUnit compilationUnit = prepareAST("WrongNumberOfAssociationEnds.java");

		compilationUnit.accept(new ModelVisitor(mockCollector));

		verify(mockCollector, times(3)).report(isA(WrongNumberOfAssociationEnds.class));

		checkNoOtherErrorRaised();
	}

	@Test
	public void testAssociationWrongInnerClass() throws Exception {
		CompilationUnit compilationUnit = prepareAST("AssociationWrongInnerClass.java");

		compilationUnit.accept(new ModelVisitor(mockCollector));

		verify(mockCollector, times(2)).report(isA(WrongTypeInAssociation.class));

		checkNoOtherErrorRaised();
	}

	@Test
	public void testCompositionNotExactlyOneContainer() throws Exception {
		CompilationUnit compilationUnit = prepareAST("CompositionNotExactlyOneContainer.java");

		compilationUnit.accept(new ModelVisitor(mockCollector));

		verify(mockCollector, times(2)).report(isA(WrongCompositionEnds.class));

		checkNoOtherErrorRaised();
	}

	@Test
	public void testStateMethodNotCorrect() throws Exception {
		CompilationUnit compilationUnit = prepareAST("StateMethodNotCorrect.java");

		compilationUnit.accept(new ModelVisitor(mockCollector));

		verify(mockCollector).report(isA(InvalidChildrenElement.class));
		verify(mockCollector).report(isA(UnknownStateMethod.class));
		verify(mockCollector).report(isA(StateMethodParameters.class));

		checkNoOtherErrorRaised();
	}

	@Test
	public void testStateInnerTypesNotCorrect() throws Exception {
		CompilationUnit compilationUnit = prepareAST("StateInnerTypesNotCorrect.java");

		compilationUnit.accept(new ModelVisitor(mockCollector));

		verify(mockCollector, times(3)).report(isA(InvalidChildrenElement.class));
		verify(mockCollector).report(isA(UnknownClassInState.class));

		checkNoOtherErrorRaised();
	}

	@Test
	public void testTransitionMethodNotCorrect() throws Exception {
		CompilationUnit compilationUnit = prepareAST("TransitionMethodNotCorrect.java");

		compilationUnit.accept(new ModelVisitor(mockCollector));

		verify(mockCollector).report(isA(UnknownTransitionMethod.class));
		verify(mockCollector).report(isA(TransitionMethodParameters.class));

		checkNoOtherErrorRaised();
	}

	@Test
	public void testTransitionsWithoutSourceTargetOrTrigger() throws Exception {
		CompilationUnit compilationUnit = prepareAST("TransitionsWithoutSourceTargetOrTrigger.java");

		compilationUnit.accept(new ModelVisitor(mockCollector));

		verify(mockCollector).report(isA(TriggerOnInitialTransition.class));
		verify(mockCollector).report(isA(MissingTransitionSource.class));
		verify(mockCollector).report(isA(MissingTransitionTarget.class));
		verify(mockCollector).report(isA(MissingTransitionTrigger.class));

		checkNoOtherErrorRaised();
	}
	
	@Test
	public void testDataTypeFieldNotFinal() throws Exception {
		CompilationUnit compilationUnit = prepareAST("DataTypeFieldNotFinal.java");

		compilationUnit.accept(new ModelVisitor(mockCollector));

		verify(mockCollector).report(isA(MutableDataTypeField.class));

		checkNoOtherErrorRaised();
	}
	
	@Test
	public void testDataTypeInvalidFieldType() throws Exception {
		CompilationUnit compilationUnit = prepareAST("DataTypeInvalidFieldType.java");

		compilationUnit.accept(new ModelVisitor(mockCollector));

		verify(mockCollector, times(2)).report(isA(InvalidDataTypeField.class));

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

}
