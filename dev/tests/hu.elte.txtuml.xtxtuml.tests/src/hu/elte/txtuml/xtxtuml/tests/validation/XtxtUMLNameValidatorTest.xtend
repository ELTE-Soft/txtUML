package hu.elte.txtuml.xtxtuml.tests.validation;

import com.google.inject.Inject
import hu.elte.txtuml.xtxtuml.XtxtUMLInjectorProvider
import hu.elte.txtuml.xtxtuml.xtxtUML.TUFile
import org.eclipse.xtext.common.types.TypesPackage
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import org.eclipse.xtext.xbase.XbasePackage
import org.junit.Test
import org.junit.runner.RunWith

import static hu.elte.txtuml.xtxtuml.validation.XtxtUMLIssueCodes.*
import static hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage.Literals.*

@RunWith(XtextRunner)
@InjectWith(XtxtUMLInjectorProvider)
class XtxtUMLNameValidatorTest {

	@Inject extension ParseHelper<TUFile>;
	@Inject extension ValidationTestHelper;
	@Inject extension XtxtUMLValidationTestUtils;

	@Test
	def checkPackageNameIsNotReserved() {
		'''
			package test.model;
		'''.parse.assertNoError(RESERVED_NAME);

		val rawFile = '''
			package goto;
		''';

		rawFile.parse.assertError(TU_FILE, RESERVED_NAME, rawFile.indexOf("goto"), 4);
	}

	@Test
	def checkModelElementNameIsNotReserved() {
		'''
			execution E {}
			signal S;
			class A;
			association Assoc {}
			interface I {}
			connector C {}
			enum E {}
		'''.parse.assertNoError(RESERVED_NAME);

		val rawFile = '''
			execution goto {}
			signal assert;
			class native;
			association volatile {}
			interface transient {}
			connector implements {}
			enum const {}
		''';

		val parsedFile = rawFile.parse;
		parsedFile.assertError(TU_MODEL_ELEMENT, RESERVED_NAME, rawFile.indexOf("goto"), 4);
		parsedFile.assertError(TU_MODEL_ELEMENT, RESERVED_NAME, rawFile.indexOf("assert"), 6);
		parsedFile.assertError(TU_MODEL_ELEMENT, RESERVED_NAME, rawFile.indexOf("native"), 6);
		parsedFile.assertError(TU_MODEL_ELEMENT, RESERVED_NAME, rawFile.indexOf("volatile"), 8);
		parsedFile.assertError(TU_MODEL_ELEMENT, RESERVED_NAME, rawFile.indexOf("transient"), 9);
		parsedFile.assertError(TU_MODEL_ELEMENT, RESERVED_NAME, rawFile.indexOf("implements"), 10);
		parsedFile.assertError(TU_MODEL_ELEMENT, RESERVED_NAME, rawFile.indexOf("const"), 5);
	}
	
	@Test
	def checkExecutionAttributeNameIsNotReserved(){
		'''
			execution E{
				int foo;
			}
		'''.parse.assertNoError(RESERVED_NAME);
		
		val rawFile = '''
			execution E{
				int goto;
			}
		''';
		rawFile.parse.assertError(TU_EXECUTION_ATTRIBUTE, RESERVED_NAME, rawFile.indexOf("goto"), 4);
	}

	@Test
	def checkSignalAttributeNameIsNotReserved() {
		'''
			signal S {
				int foo;
			}
		'''.parse.assertNoError(RESERVED_NAME);

		val rawFile = '''
			signal S {
				int goto;
			}
		''';

		rawFile.parse.assertError(TU_SIGNAL_ATTRIBUTE, RESERVED_NAME, rawFile.indexOf("goto"), 4);
	}

	@Test
	def checkAttributeNameIsNotReserved() {
		'''
			class A {
				int foo;
			}
		'''.parse.assertNoError(RESERVED_NAME);

		val rawFile = '''
			class A {
				int goto;
			}
		''';

		rawFile.parse.assertError(TU_ATTRIBUTE, RESERVED_NAME, rawFile.indexOf("goto"), 4);
	}

	@Test
	def checkConstructorNameIsNotReserved() {
		'''
			class A {
				A() {}
			}
		'''.parse.assertNoError(RESERVED_NAME);

		val rawFile = '''
			class goto {
				goto() {}
			}
		''';

		rawFile.parse.assertError(TU_CONSTRUCTOR, RESERVED_NAME, rawFile.indexOfNth("goto", 1), 4);
	}

	@Test
	def checkOperationNameIsNotReserved() {
		'''
			class A {
				void foo() {}
			}
		'''.parse.assertNoError(RESERVED_NAME);

		val rawFile = '''
			class A {
				void goto() {}
			}
		'''

		rawFile.parse.assertError(TU_OPERATION, RESERVED_NAME, rawFile.indexOf("goto"), 4);
	}

	@Test
	def checkStateNameIsNotReserved() {
		'''
			class A {
				initial I;
				state S;
				choice C;
			}
		'''.parse.assertNoError(RESERVED_NAME);

		val rawFile = '''
			class A {
				initial goto;
				state transient;
				choice assert;
			}
		''';

		val parsedFile = rawFile.parse;
		parsedFile.assertError(TU_STATE, RESERVED_NAME, rawFile.indexOf("goto"), 4);
		parsedFile.assertError(TU_STATE, RESERVED_NAME, rawFile.indexOf("transient"), 9);
		parsedFile.assertError(TU_STATE, RESERVED_NAME, rawFile.indexOf("assert"), 6);
	}

	@Test
	def checkTransitionNameIsNotReserved() {
		'''
			class A {
				transition T {}
			}
		'''.parse.assertNoError(RESERVED_NAME);

		val rawFile = '''
			class A {
				transition goto {}
			}
		''';

		rawFile.parse.assertError(TU_TRANSITION, RESERVED_NAME, rawFile.indexOf("goto"), 4);
	}

	@Test
	def checkPortNameIsNotReserved() {
		'''
			class A {
				port P {}
			}
		'''.parse.assertNoError(RESERVED_NAME);

		val rawFile = '''
			class A {
				port goto {}
			}
		''';

		rawFile.parse.assertError(TU_PORT, RESERVED_NAME, rawFile.indexOf("goto"), 4);
	}

	@Test
	def checkAssociationEndNameIsNotReserved() {
		'''
			class A;
			association S {
				A a;
			}
		'''.parse.assertNoError(RESERVED_NAME);

		val rawFile = '''
			class A;
			association S {
				A goto;
			}
		''';

		rawFile.parse.assertError(TU_ASSOCIATION_END, RESERVED_NAME, rawFile.indexOf("goto"), 4);
	}

	@Test
	def checkConnectorEndNameIsNotReserved() {
		'''
			class A { port P {} }
			association AA {
				A a;
			}
			connector C {
				AA.a->A.P ap;
			}
		'''.parse.assertNoError(RESERVED_NAME);

		val rawFile = '''
			class A { port P {} }
			association AA {
				A a;
			}
			connector C {
				AA.a->A.P goto;
			}
		''';

		rawFile.parse.assertError(TU_CONNECTOR_END, RESERVED_NAME, rawFile.indexOf("goto"), 4);
	}

	@Test
	def checkParameterNameIsNotReserved() {
		'''
			class A {
				A(int i) {}
				void foo(int i) {
					for (int j : null) {}
				}
			}
		'''.parse.assertNoError(RESERVED_NAME);

		val rawFile = '''
			class A {
				A(int goto) {}
				void foo(int transient) {
					for (int throws : null) {}
				}
			}
		''';

		val parsedFile = rawFile.parse;
		parsedFile.assertError(TypesPackage.Literals.JVM_FORMAL_PARAMETER, RESERVED_NAME, rawFile.indexOf("goto"), 4);
		parsedFile.assertError(TypesPackage.Literals.JVM_FORMAL_PARAMETER, RESERVED_NAME, rawFile.indexOf("transient"),
			9);
		parsedFile.assertError(TypesPackage.Literals.JVM_FORMAL_PARAMETER, RESERVED_NAME, rawFile.indexOf("throws"), 6);
	}

	@Test
	def checkVariableNameIsNotReserved() {
		'''
			class A {
				void foo() {
					int i = 0;
					for (int j = 0;;) {}
				}
			}
		'''.parse.assertNoError(RESERVED_NAME);

		val rawFile = '''
			class A {
				void foo() {
					int goto = 0;
					for (int transient = 0;;) {}
				}
			}
		''';

		val parsedFile = rawFile.parse;
		parsedFile.assertError(XbasePackage.Literals.XVARIABLE_DECLARATION, RESERVED_NAME, rawFile.indexOf("goto"), 4);
		parsedFile.assertError(XbasePackage.Literals.XVARIABLE_DECLARATION, RESERVED_NAME, rawFile.indexOf("transient"),
			9);
	}

}
