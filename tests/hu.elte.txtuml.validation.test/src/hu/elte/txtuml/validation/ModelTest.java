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

import hu.elte.txtuml.export.uml2.utils.SharedUtils;
import hu.elte.txtuml.validation.problems.association.WrongCompositionEnds;
import hu.elte.txtuml.validation.problems.association.WrongNumberOfAssociationEnds;
import hu.elte.txtuml.validation.problems.association.WrongTypeInAssociation;
import hu.elte.txtuml.validation.problems.general.InvalidChildrenElement;
import hu.elte.txtuml.validation.problems.general.InvalidModifier;
import hu.elte.txtuml.validation.problems.general.InvalidTypeInModel;
import hu.elte.txtuml.validation.problems.modelclass.InvalidModelClassElement;
import hu.elte.txtuml.validation.problems.modelclass.InvalidTypeWithClassAllowed;
import hu.elte.txtuml.validation.problems.modelclass.InvalidTypeWithClassNotAllowed;
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

	private static final String VALIDATION_EXAMPLES_PACKAGE = "/hu/elte/txtuml/examples/validation/";
	private static final String VALIDATION_EXAMPLES_ROOT = "../../examples/hu.elte.txtuml.examples.validation/src";
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

		verify(mockCollector).setProblemStatus(isA(InvalidTypeInModel.class));

		checkNoOtherErrorRaised();
	}

	@Test
	public void testInvalidParameterType() throws Exception {
		CompilationUnit compilationUnit = prepareAST("InvalidParameterType.java");

		compilationUnit.accept(new ModelVisitor(mockCollector));

		verify(mockCollector, times(2)).setProblemStatus(isA(InvalidTypeWithClassAllowed.class));

		checkNoOtherErrorRaised();
	}

	@Test
	public void testInvalidTypeInModel() throws Exception {
		CompilationUnit compilationUnit = prepareAST("InvalidTypeInModelClass.java");

		compilationUnit.accept(new ModelVisitor(mockCollector));

		verify(mockCollector).setProblemStatus(isA(InvalidTypeWithClassNotAllowed.class));

		checkNoOtherErrorRaised();
	}

	@Test
	public void testInvalidModifier() throws Exception {
		CompilationUnit compilationUnit = prepareAST("InvalidModifier.java");

		compilationUnit.accept(new ModelVisitor(mockCollector));

		verify(mockCollector, times(3)).setProblemStatus(isA(InvalidModifier.class));

		checkNoOtherErrorRaised();
	}

	@Test
	public void testFieldType() throws Exception {
		CompilationUnit compilationUnit = prepareAST("InvalidFieldType.java");

		compilationUnit.accept(new ModelVisitor(mockCollector));

		verify(mockCollector, times(2)).setProblemStatus(isA(InvalidTypeWithClassNotAllowed.class));

		checkNoOtherErrorRaised();
	}

	@Test
	public void testInvalidTypeInSignal() throws Exception {
		CompilationUnit compilationUnit = prepareAST("InvalidTypeInSignal.java");

		compilationUnit.accept(new ModelVisitor(mockCollector));

		verify(mockCollector, times(2)).setProblemStatus(isA(InvalidTypeWithClassNotAllowed.class));

		checkNoOtherErrorRaised();
	}

	@Test
	public void testInvalidClassInModelClass() throws Exception {
		CompilationUnit compilationUnit = prepareAST("InvalidClassInModelClass.java");

		compilationUnit.accept(new ModelVisitor(mockCollector));

		verify(mockCollector, times(2)).setProblemStatus(isA(InvalidModelClassElement.class));

		checkNoOtherErrorRaised();
	}

	@Test
	public void testMethodInSignal() throws Exception {
		CompilationUnit compilationUnit = prepareAST("MethodInSignal.java");

		compilationUnit.accept(new ModelVisitor(mockCollector));

		verify(mockCollector, times(1)).setProblemStatus(isA(InvalidSignalContent.class));

		checkNoOtherErrorRaised();
	}
	
	@Test
	public void testWrongNumberOfAssociationEnds() throws Exception {
		CompilationUnit compilationUnit = prepareAST("WrongNumberOfAssociationEnds.java");
		
		compilationUnit.accept(new ModelVisitor(mockCollector));
		
		verify(mockCollector, times(3)).setProblemStatus(isA(WrongNumberOfAssociationEnds.class));
		
		checkNoOtherErrorRaised();
	}
	
	@Test
	public void testAssociationWrongInnerClass() throws Exception {
		CompilationUnit compilationUnit = prepareAST("AssociationWrongInnerClass.java");
		
		compilationUnit.accept(new ModelVisitor(mockCollector));
		
		verify(mockCollector, times(2)).setProblemStatus(isA(WrongTypeInAssociation.class));
		
		checkNoOtherErrorRaised();
	}
	
	@Test
	public void testCompositionNotExactlyOneContainer() throws Exception {
		CompilationUnit compilationUnit = prepareAST("CompositionNotExactlyOneContainer.java");
		
		compilationUnit.accept(new ModelVisitor(mockCollector));
		
		verify(mockCollector, times(2)).setProblemStatus(isA(WrongCompositionEnds.class));
		
		checkNoOtherErrorRaised();
	}
	
	@Test
	public void testStateMethodNotCorrect() throws Exception {
		CompilationUnit compilationUnit = prepareAST("StateMethodNotCorrect.java");
		
		compilationUnit.accept(new ModelVisitor(mockCollector));
		
		verify(mockCollector).setProblemStatus(isA(InvalidChildrenElement.class));
		verify(mockCollector).setProblemStatus(isA(UnknownStateMethod.class));
		verify(mockCollector).setProblemStatus(isA(StateMethodParameters.class));
		
		checkNoOtherErrorRaised();
	}
	
	@Test
	public void testStateInnerTypesNotCorrect() throws Exception {
		CompilationUnit compilationUnit = prepareAST("StateInnerTypesNotCorrect.java");
		
		compilationUnit.accept(new ModelVisitor(mockCollector));
		
		verify(mockCollector, times(3)).setProblemStatus(isA(InvalidChildrenElement.class));
		verify(mockCollector).setProblemStatus(isA(UnknownClassInState.class));
		
		checkNoOtherErrorRaised();
	}
	
	@Test
	public void testTransitionMethodNotCorrect() throws Exception {
		CompilationUnit compilationUnit = prepareAST("TransitionMethodNotCorrect.java");
		
		compilationUnit.accept(new ModelVisitor(mockCollector));
		
		verify(mockCollector).setProblemStatus(isA(UnknownTransitionMethod.class));
		verify(mockCollector).setProblemStatus(isA(TransitionMethodParameters.class));
		
		checkNoOtherErrorRaised();
	}
	
	@Test
	public void testTransitionsWithoutSourceTargetOrTrigger() throws Exception {
		CompilationUnit compilationUnit = prepareAST("TransitionsWithoutSourceTargetOrTrigger.java");
		
		compilationUnit.accept(new ModelVisitor(mockCollector));
		
		verify(mockCollector).setProblemStatus(isA(TriggerOnInitialTransition.class));
		verify(mockCollector).setProblemStatus(isA(MissingTransitionSource.class));
		verify(mockCollector).setProblemStatus(isA(MissingTransitionTarget.class));
		verify(mockCollector).setProblemStatus(isA(MissingTransitionTrigger.class));
		
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
		parser.setEnvironment(classpath, sourcepath, encodings, false);

		return (CompilationUnit) parser.createAST(null);
	}

}
