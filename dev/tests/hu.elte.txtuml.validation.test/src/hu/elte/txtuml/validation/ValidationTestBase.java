package hu.elte.txtuml.validation;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.junit.Before;

import hu.elte.txtuml.utils.jdt.SharedUtils;
import hu.elte.txtuml.validation.ModelTest.ModelProblemCollector;
import hu.elte.txtuml.validation.common.ProblemCollector;
import hu.elte.txtuml.validation.common.SourceInfo;

public abstract class ValidationTestBase {

	/*
	 * This test suite relies on the repository having a given structure. The
	 * structure is encoded in these paths:
	 */
	private static final String VALIDATION_EXAMPLES_ROOT = "../../../examples/tests/hu.elte.txtuml.examples.validation/src";
	private static final String API_SRC_LOC = "../../plugins/hu.elte.txtuml.api.model/src/";

	protected ProblemCollector mockCollector;

	protected abstract String getValidationExamplesPackage();

	@Before
	public void before() {
		mockCollector = mock(ModelProblemCollector.class);
		SourceInfo sourceInfo = mock(SourceInfo.class);
		when(mockCollector.getSourceInfo()).thenReturn(sourceInfo);
		when(sourceInfo.getOriginatingFileName()).thenReturn("");
		when(sourceInfo.getSourceLineNumber(anyInt())).thenReturn(0);
	}

	protected CompilationUnit prepareAST(String javaFile) throws IOException {
		File projectRoot = new File(VALIDATION_EXAMPLES_ROOT).getAbsoluteFile();
		File sourceFile = new File(projectRoot.getCanonicalPath() + getValidationExamplesPackage() + javaFile);

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

	protected void checkNoOtherErrorRaised() {
		verify(mockCollector, atLeast(0)).getSourceInfo();
		verifyNoMoreInteractions(mockCollector);
	}

}
