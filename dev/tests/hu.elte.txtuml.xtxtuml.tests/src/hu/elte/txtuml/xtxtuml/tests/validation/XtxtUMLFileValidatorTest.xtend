package hu.elte.txtuml.xtxtuml.tests.validation;

import com.google.inject.Inject
import hu.elte.txtuml.xtxtuml.XtxtUMLInjectorProvider
import hu.elte.txtuml.xtxtuml.xtxtUML.TUFile
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import org.junit.Test
import org.junit.runner.RunWith

import static hu.elte.txtuml.xtxtuml.validation.XtxtUMLIssueCodes.*
import static hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage.Literals.*

@RunWith(XtextRunner)
@InjectWith(XtxtUMLInjectorProvider)
class XtxtUMLFileValidatorTest {

	@Inject extension ParseHelper<TUFile>;
	@Inject extension ValidationTestHelper;

	@Test
	def checkModelDeclarationIsInModelInfoFile() {
		val rawFile = '''
			model-package test.model;
		''';

		rawFile.parse(URI.createURI("model-info.xtxtuml"), new ResourceSetImpl()).assertNoError(
			MISPLACED_MODEL_DECLARATION);
		rawFile.parse(URI.createURI("model-info.txtuml"), new ResourceSetImpl()).assertNoError(
			MISPLACED_MODEL_DECLARATION);
		rawFile.parse(URI.createURI("modelinfo.xtxtuml"), new ResourceSetImpl()).assertError(TU_MODEL_DECLARATION,
			MISPLACED_MODEL_DECLARATION, 0, 13);
	}

	@Test
	def checkModelInfoContainsModelDeclaration() {
		'''
			model-package test.model;
		'''.parse(URI.createURI("model-info.xtxtuml"), new ResourceSetImpl()).assertNoError(WRONG_MODEL_INFO);

		'''
			package test.model;
		'''.parse(URI.createURI("model-info.xtxtuml"), new ResourceSetImpl()).assertError(TU_FILE, WRONG_MODEL_INFO);
	}

	@Test
	def checkDefaultPackageIsNotUsed() {
		'''
			package test.model;
			class A;
		'''.parse.assertNoError(WRONG_PACKAGE);

		'''
			class A;
		'''.parse.assertError(TU_FILE, WRONG_PACKAGE);
	}

}
