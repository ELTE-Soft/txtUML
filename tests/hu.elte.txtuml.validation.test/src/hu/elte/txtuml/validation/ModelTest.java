package hu.elte.txtuml.validation;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Optional;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.osgi.framework.Bundle;

import com.google.common.io.Files;

import hu.elte.txtuml.api.model.Model;
import hu.elte.txtuml.export.uml2.utils.SharedUtils;
import hu.elte.txtuml.validation.problems.general.InvalidModifier;
import hu.elte.txtuml.validation.problems.general.InvalidTypeInModel;
import hu.elte.txtuml.validation.problems.modelclass.InvalidModelClassElement;
import hu.elte.txtuml.validation.problems.modelclass.InvalidTypeWithClassAllowed;
import hu.elte.txtuml.validation.problems.modelclass.InvalidTypeWithClassNotAllowed;
import hu.elte.txtuml.validation.problems.signal.InvalidSignalContent;
import hu.elte.txtuml.validation.visitors.ModelVisitor;

public class ModelTest {

	ProblemCollector mockCollector;
	private static String modelPath;
	private static Runnable teardown;

	@BeforeClass
	static public void obtainSources() {
		try {
			teardown = () -> {};
			ClassLoader cloader = ModelTest.class.getClassLoader();
			if (cloader instanceof URLClassLoader) {
				URLClassLoader classLoader = (URLClassLoader) Model.class.getClassLoader();
				Optional<String> modelFolder = Arrays.asList(classLoader.getURLs()).stream().map(URL::getFile)
						.filter(u -> u.contains("hu.elte.txtuml.api.model")).findAny();
				String srcDir = modelFolder.get().replaceAll("/bin", "/src");
				modelPath = new File(srcDir).getCanonicalPath();
			} else {
				Bundle bundle = Platform.getBundle("hu.elte.txtuml.api.model");
				String location = bundle.getLocation();
				if (location.endsWith(".jar")) {
					File myTempDir = Files.createTempDir();
					UnZip.unzipFile(location, myTempDir.getAbsolutePath());
					modelPath = myTempDir.getAbsolutePath() + "/src";
					teardown = () -> myTempDir.delete();
				} else {
					modelPath = location + "src/";
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	static public void releaseExtractedSrc() {
		teardown.run();
	}

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

		verify(mockCollector).setProblemStatus(eq(true), isA(InvalidTypeInModel.class));

		checkNoOtherErrorRaised();
	}

	@Test
	public void testInvalidParameterType() throws Exception {
		CompilationUnit compilationUnit = prepareAST("InvalidParameterType.java");

		compilationUnit.accept(new ModelVisitor(mockCollector));

		verify(mockCollector, times(2)).setProblemStatus(eq(true), isA(InvalidTypeWithClassAllowed.class));

		checkNoOtherErrorRaised();
	}

	@Test
	public void testInvalidTypeInModel() throws Exception {
		CompilationUnit compilationUnit = prepareAST("InvalidTypeInModelClass.java");

		compilationUnit.accept(new ModelVisitor(mockCollector));

		verify(mockCollector).setProblemStatus(eq(true), isA(InvalidTypeWithClassNotAllowed.class));

		checkNoOtherErrorRaised();
	}

	@Test
	public void testInvalidModifier() throws Exception {
		CompilationUnit compilationUnit = prepareAST("InvalidModifier.java");

		compilationUnit.accept(new ModelVisitor(mockCollector));

		verify(mockCollector, times(3)).setProblemStatus(eq(true), isA(InvalidModifier.class));

		checkNoOtherErrorRaised();
	}

	@Test
	public void testFieldType() throws Exception {
		CompilationUnit compilationUnit = prepareAST("InvalidFieldType.java");

		compilationUnit.accept(new ModelVisitor(mockCollector));

		verify(mockCollector, times(2)).setProblemStatus(eq(true), isA(InvalidTypeWithClassNotAllowed.class));

		checkNoOtherErrorRaised();
	}

	@Test
	public void testInvalidTypeInSignal() throws Exception {
		CompilationUnit compilationUnit = prepareAST("InvalidTypeInSignal.java");

		compilationUnit.accept(new ModelVisitor(mockCollector));

		verify(mockCollector, times(2)).setProblemStatus(eq(true), isA(InvalidTypeWithClassNotAllowed.class));

		checkNoOtherErrorRaised();
	}

	@Test
	public void testInvalidClassInModelClass() throws Exception {
		CompilationUnit compilationUnit = prepareAST("InvalidClassInModelClass.java");

		compilationUnit.accept(new ModelVisitor(mockCollector));

		verify(mockCollector, times(2)).setProblemStatus(eq(true), isA(InvalidModelClassElement.class));

		checkNoOtherErrorRaised();
	}

	@Test
	public void testMethodInSignal() throws Exception {
		CompilationUnit compilationUnit = prepareAST("MethodInSignal.java");

		compilationUnit.accept(new ModelVisitor(mockCollector));

		verify(mockCollector, times(1)).setProblemStatus(eq(true), isA(InvalidSignalContent.class));

		checkNoOtherErrorRaised();
	}

	private void checkNoOtherErrorRaised() {
		verify(mockCollector, atLeast(0)).setProblemStatus(eq(false), any());
		verify(mockCollector, atLeast(0)).getSourceInfo();
		verifyNoMoreInteractions(mockCollector);
	}

	private CompilationUnit prepareAST(String javaFile) throws IOException {
		File projectRoot = new File("testmodels").getAbsoluteFile();
		File sourceFile = new File("testmodels/" + javaFile);

		ASTParser parser = ASTParser.newParser(AST.JLS8);
		char[] content = SharedUtils.getFileContents(sourceFile);

		System.out.println("loading API from: " + modelPath);

		String[] classpath = {};
		String[] sourcepath = { projectRoot.getCanonicalPath(), modelPath };
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
