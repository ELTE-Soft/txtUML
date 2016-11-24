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

	@Test
	def checkPackageNameIsNotReserved() {
		'''
			package test.model;
		'''.parse.assertNoError(RESERVED_NAME);

		'''
			package goto;
		'''.parse.assertError(TU_FILE, RESERVED_NAME, 8, 4);
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
		'''.parse.assertNoError(RESERVED_NAME);

		val file = '''
			execution goto {}
			signal assert;
			class native;
			association volatile {}
			interface transient {}
			connector implements {}
		'''.parse;

		file.assertError(TU_MODEL_ELEMENT, RESERVED_NAME, 10, 4);
		file.assertError(TU_MODEL_ELEMENT, RESERVED_NAME, 26, 6);
		file.assertError(TU_MODEL_ELEMENT, RESERVED_NAME, 41, 6);
		file.assertError(TU_MODEL_ELEMENT, RESERVED_NAME, 62, 8);
		file.assertError(TU_MODEL_ELEMENT, RESERVED_NAME, 85, 9);
		file.assertError(TU_MODEL_ELEMENT, RESERVED_NAME, 109, 10);
	}

	@Test
	def checkSignalAttributeNameIsNotReserved() {
		'''
			signal S {
				int foo;
			}
		'''.parse.assertNoError(RESERVED_NAME);

		'''
			signal S {
				int goto;
			}
		'''.parse.assertError(TU_SIGNAL_ATTRIBUTE, RESERVED_NAME, 17, 4);
	}

	@Test
	def checkAttributeNameIsNotReserved() {
		'''
			class A {
				int foo;
			}
		'''.parse.assertNoError(RESERVED_NAME);

		'''
			class A {
				int goto;
			}
		'''.parse.assertError(TU_ATTRIBUTE, RESERVED_NAME, 16, 4);
	}

	@Test
	def checkConstructorNameIsNotReserved() {
		'''
			class A {
				A() {}
			}
		'''.parse.assertNoError(RESERVED_NAME);

		'''
			class goto {
				goto() {}
			}
		'''.parse.assertError(TU_CONSTRUCTOR, RESERVED_NAME, 15, 4);
	}

	@Test
	def checkOperationNameIsNotReserved() {
		'''
			class A {
				void foo() {}
			}
		'''.parse.assertNoError(RESERVED_NAME);

		'''
			class A {
				void goto() {}
			}
		'''.parse.assertError(TU_OPERATION, RESERVED_NAME, 17, 4);
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

		val file = '''
			class A {
				initial goto;
				state transient;
				choice assert;
			}
		'''.parse;

		file.assertError(TU_STATE, RESERVED_NAME, 20, 4);
		file.assertError(TU_STATE, RESERVED_NAME, 34, 9);
		file.assertError(TU_STATE, RESERVED_NAME, 54, 6);
	}

	@Test
	def checkTransitionNameIsNotReserved() {
		'''
			class A {
				transition T {}
			}
		'''.parse.assertNoError(RESERVED_NAME);

		'''
			class A {
				transition goto {}
			}
		'''.parse.assertError(TU_TRANSITION, RESERVED_NAME, 23, 4);
	}

	@Test
	def checkPortNameIsNotReserved() {
		'''
			class A {
				port P {}
			}
		'''.parse.assertNoError(RESERVED_NAME);

		'''
			class A {
				port goto {}
			}
		'''.parse.assertError(TU_PORT, RESERVED_NAME, 17, 4);
	}

	@Test
	def checkAssociationEndNameIsNotReserved() {
		'''
			class A;
			association S {
				A a;
			}
		'''.parse.assertNoError(RESERVED_NAME);

		'''
			class A;
			association S {
				A goto;
			}
		'''.parse.assertError(TU_ASSOCIATION_END, RESERVED_NAME, 30, 4);
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

		'''
			class A { port P {} }
			association AA {
				A a;
			}
			connector C {
				AA.a->A.P goto;
			}
		'''.parse.assertError(TU_CONNECTOR_END, RESERVED_NAME, 77, 4);
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

		val file = '''
			class A {
				A(int goto) {}
				void foo(int transient) {
					for (int throws : null) {}
				}
			}
		'''.parse;

		file.assertError(TypesPackage.Literals.JVM_FORMAL_PARAMETER, RESERVED_NAME, 18, 4);
		file.assertError(TypesPackage.Literals.JVM_FORMAL_PARAMETER, RESERVED_NAME, 42, 9);
		file.assertError(TypesPackage.Literals.JVM_FORMAL_PARAMETER, RESERVED_NAME, 67, 6);
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

		val file = '''
			class A {
				void foo() {
					int goto = 0;
					for (int transient = 0;;) {}
				}
			}
		'''.parse;

		file.assertError(XbasePackage.Literals.XVARIABLE_DECLARATION, RESERVED_NAME, 32, 4);
		file.assertError(XbasePackage.Literals.XVARIABLE_DECLARATION, RESERVED_NAME, 54, 9);
	}

}
